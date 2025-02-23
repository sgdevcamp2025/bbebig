import { useRef, useState } from 'react'

import { useSignalingStomp } from '@/stores/use-signaling-stomp-store'

import useMediaControl from '../use-media-control'
import useUserStatus from './use-user-status'

// WebRTC 설정
const PC_CONFIG = {
  iceServers: [{ urls: 'stun:stun.l.google.com:19302' }, { urls: 'stun:stun1.l.google.com:19302' }],
  encodedInsertableStreams: true,
  qos: {
    dscp: true,
    maxPacketSize: 2500000
  }
}

export interface WebRTCUser {
  id: string
  stream: MediaStream | null
  audioEnabled: boolean
  videoEnabled: boolean
}

interface SignalingMessage {
  messageType: string
  channelId: string
  senderId: string
  receiverId?: string
  sdp?: RTCSessionDescriptionInit
  candidate?: RTCIceCandidate
  participants?: number[]
}

export function useSignalingWithSFU(
  userId: string,
  channelId: string,
  channelName: string,
  serverName: string
) {
  const subscriptionId = `subscription-${userId}-${channelId}`
  const { startStream, getStream } = useMediaControl()
  const { send, subscribe, unsubscribe } = useSignalingStomp()
  const [users, setUsers] = useState<WebRTCUser[]>([])
  const { joinVoiceChannel, leaveVoiceChannel, channelInfo } = useUserStatus()

  const senderPcRef = useRef<RTCPeerConnection | null>(null)
  const receiverPcsRef = useRef<Map<string, RTCPeerConnection>>(new Map())

  // 로컬 사용자 추가
  const addLocalUser = async (stream: MediaStream) => {
    setUsers((prev) => {
      const existingUser = prev.find((user) => user.id === userId)
      if (!existingUser) {
        return [
          ...prev,
          {
            id: userId,
            stream,
            audioEnabled: true,
            videoEnabled: true
          }
        ]
      }
      return prev
    })
  }

  // 원격 사용자 추가
  const addRemoteUser = (remoteUserId: string, stream: MediaStream) => {
    console.log('원격 사용자가 참여했습니다.', remoteUserId)
    setUsers((prev) => {
      const existingUser = prev.find((user) => user.id === remoteUserId)
      if (existingUser) {
        return prev.map((user) => (user.id === remoteUserId ? { ...user, stream } : user))
      }
      return [
        ...prev,
        {
          id: remoteUserId,
          stream,
          audioEnabled: true,
          videoEnabled: true
        }
      ]
    })
  }

  // 사용자 제거
  const removeUser = (remoteUserId: string) => {
    console.log('사용자가 나갔습니다.', remoteUserId)
    const pc = receiverPcsRef.current.get(remoteUserId)
    if (pc) {
      pc.close()
      receiverPcsRef.current.delete(remoteUserId)
    }
    setUsers((prev) => prev.filter((user) => user.id !== remoteUserId))
  }

  // Sender PeerConnection 생성
  const createSenderPeerConnection = async () => {
    try {
      // 기존 연결 체크 및 정리
      if (senderPcRef.current) {
        console.log('Closing existing sender connection')
        senderPcRef.current.close()
        senderPcRef.current = null
      }

      console.log('Creating new sender connection')
      const pc = new RTCPeerConnection(PC_CONFIG)

      senderPcRef.current = pc

      await startStream({ video: true, audio: true })

      const stream = await getStream()

      if (!stream) {
        console.error('Failed to get media stream')
        return
      }

      stream.getTracks().forEach((track) => {
        pc.addTrack(track, stream)
      })

      await addLocalUser(stream)

      pc.onicecandidate = ({ candidate }) => {
        if (candidate && pc.signalingState !== 'closed') {
          send('/pub/stream/group', {
            messageType: 'CANDIDATE',
            channelId,
            senderId: userId,
            candidate
          })
        }
      }

      pc.onconnectionstatechange = () => {
        console.log('Sender connection state:', pc.connectionState)
      }

      try {
        console.log('생성자 제안 생성')
        const offer = await pc.createOffer({
          offerToReceiveAudio: true,
          offerToReceiveVideo: true
        })

        // SDP 필터링
        const filteredSdp = offer.sdp
          ?.split('\n')
          .filter(
            (line) =>
              !line.includes('AMR/8000') && // 지원되지 않는 오디오 코덱 제거
              !line.includes('CN/') // 컴포트 노이즈 코덱 제거
          )
          .join('\n')

        await pc.setLocalDescription({
          type: offer.type,
          sdp: filteredSdp
        })

        if (pc.signalingState === 'have-local-offer') {
          console.log('Sending offer to SFU')
          send('/pub/stream/group', {
            messageType: 'OFFER',
            channelId,
            senderId: userId,
            sdp: offer
          })
        }
      } catch (error) {
        console.error('Sender offer creation failed:', error)
      }

      // 비디오 트랜시버 설정
      pc.addTransceiver('video', {
        direction: 'sendrecv',
        streams: [stream],
        sendEncodings: [
          {
            rid: 'high',
            maxBitrate: 2500000,
            scaleResolutionDownBy: 1.0
          }
        ]
      })
    } catch (error) {
      console.error('Sender connection creation failed:', error)
    }
  }

  const handleOffer = async (message: SignalingMessage) => {
    const pc = receiverPcsRef.current.get(message.senderId)

    if (!pc || !message.sdp) {
      console.error('No PeerConnection or SDP found for:', message.senderId)
      return
    }

    try {
      await pc.setRemoteDescription(new RTCSessionDescription(message.sdp))

      if (pc.signalingState === 'stable') {
        console.log('Creating new offer before setting answer')
        const offer = await pc.createOffer({
          offerToReceiveAudio: true,
          offerToReceiveVideo: true
        })
        await pc.setLocalDescription(offer)
      }
    } catch (error) {
      console.error('Offer 설정 실패:', error)
    }
  }

  const handleAnswer = async (message: SignalingMessage) => {
    console.log('Answer 설정 시작:', message)
    const pc = receiverPcsRef.current.get(message.senderId) || senderPcRef.current

    if (!pc || !message.sdp) {
      console.error('Invalid PeerConnection or SDP')
      return
    }

    if (pc.signalingState === 'stable') {
      return
    }

    try {
      await pc.setRemoteDescription(new RTCSessionDescription(message.sdp))
      console.log('Answer 설정 성공:', pc.signalingState)
    } catch (error) {
      console.error('Answer 설정 실패:', error)
      // 실패 시 연결 재설정
      if (message.senderId === 'SFU_SERVER') {
        await createSenderPeerConnection()
      }
    }
  }

  const handleUserJoined = async (message: SignalingMessage) => {
    if (message.senderId === userId) return

    try {
      console.log('새로운 사용자가 참여했습니다.', message.senderId)

      const pc = new RTCPeerConnection(PC_CONFIG)

      // 스트림 처리를 위한 빈 스트림 생성
      const remoteStream = new MediaStream()
      console.log('새로운 미디어 스트림을 생성했습니다.', message.senderId)

      // 트랙 이벤트 핸들러
      pc.ontrack = (event) => {
        console.log('새로운 트래킹 이벤트가 발생했습니다.', {
          kind: event.track.kind,
          userId: message.senderId,
          trackId: event.track.id,
          readyState: event.track.readyState
        })

        // 트랙을 즉시 remoteStream에 추가
        remoteStream.addTrack(event.track)

        // 트랙 상태 변경 감지
        event.track.onmute = () => console.log('트랙이 뮤트되었습니다.', event.track.id)

        event.track.onunmute = () => {
          console.log('트랙이 뮤트되지 않았습니다.', {
            trackId: event.track.id,
            streamTracks: remoteStream.getTracks().length
          })
        }

        addRemoteUser(message.senderId, remoteStream)
      }

      // 연결 상태 모니터링
      pc.onconnectionstatechange = () => {
        console.log('연결 상태:', {
          userId: message.senderId,
          state: pc.connectionState,
          iceState: pc.iceConnectionState
        })

        if (pc.connectionState === 'connected') {
          const receivers = pc.getReceivers()
          console.log(
            '활성 수신기:',
            receivers.map((r) => ({
              kind: r.track?.kind,
              trackId: r.track?.id,
              enabled: r.track?.enabled
            }))
          )
        }
      }

      // ICE 후보 처리
      pc.onicecandidate = ({ candidate }) => {
        send('/pub/stream/group', {
          messageType: 'CANDIDATE',
          channelId,
          senderId: userId,
          receiverId: message.senderId,
          candidate
        })
      }

      // 기존 연결 정리
      const oldPc = receiverPcsRef.current.get(message.senderId)

      if (oldPc) {
        oldPc.close()
      }

      receiverPcsRef.current.set(message.senderId, pc)

      // 새 사용자 추가 (초기 스트림 설정)
      setUsers((prev) => {
        const filtered = prev.filter((user) => user.id !== message.senderId)
        return [
          ...filtered,
          {
            id: message.senderId,
            stream: remoteStream, // 빈 스트림으로 초기화
            audioEnabled: true,
            videoEnabled: true
          }
        ]
      })

      // 트랜시버 설정
      pc.addTransceiver('audio', { direction: 'recvonly' })
      pc.addTransceiver('video', { direction: 'recvonly' })

      try {
        const offer = await pc.createOffer({
          offerToReceiveAudio: true,
          offerToReceiveVideo: true
        })

        console.log('Created offer:', {
          userId: message.senderId,
          type: offer.type,
          sdpLines: offer.sdp?.split('\n').length
        })

        await pc.setLocalDescription(offer)

        send('/pub/stream/group', {
          messageType: 'OFFER',
          channelId,
          senderId: userId,
          receiverId: message.senderId,
          sdp: offer
        })
      } catch (error) {
        console.error('Offer creation failed:', error)
      }
    } catch (error) {
      console.error('User join handling failed:', error)
    }
  }

  const handleCandidate = async (message: SignalingMessage) => {
    const pc = receiverPcsRef.current.get(message.senderId)
    if (!pc || !message.candidate) return

    try {
      if (pc.remoteDescription) {
        await pc.addIceCandidate(new RTCIceCandidate(message.candidate))
      } else {
        console.log('Waiting for remote description before adding candidate')
      }
    } catch (error) {
      console.error('ICE candidate 추가 실패:', error)
    }
  }

  // 채널 입장
  const joinChannel = async () => {
    initialize()

    try {
      subscribe(
        `/sub/stream/direct/${userId}`,
        (message: SignalingMessage) => {
          console.log('Direct message received:', message)
          switch (message.messageType) {
            case 'OFFER':
              handleOffer(message)
              break
            case 'CANDIDATE':
              handleCandidate(message)
              break
            case 'EXIST_USERS':
              if (message.participants) {
                const uniqueParticipants = [...new Set(message.participants)]
                const currentUsers = new Set(users.map((u) => u.id))

                // 새로운 사용자만 처리
                for (const id of uniqueParticipants) {
                  if (id !== Number(userId) && !currentUsers.has(String(id))) {
                    handleUserJoined({ ...message, senderId: String(id) })
                  }
                }
              }
              break
            case 'ANSWER':
              handleAnswer(message)
              break
          }
        },
        subscriptionId + 'direct'
      )

      subscribe(
        `/sub/stream/group/${channelId}`,
        (message: SignalingMessage) => {
          console.log('Group message received:', message)
          switch (message.messageType) {
            case 'USER_JOINED':
              if (message.senderId !== userId) {
                handleUserJoined(message)
              }
              break
            case 'USER_LEFT':
              removeUser(message.senderId)
              break
          }
        },
        subscriptionId + 'group'
      )
    } catch (err) {
      console.error('채널 입장 실패:', err)
    }

    try {
      // 먼저 채널 입장 메시지 전송
      send('/pub/stream/group', {
        messageType: 'JOIN_CHANNEL',
        channelId,
        senderId: userId
      })

      // 그 다음 연결 생성
      await createSenderPeerConnection()

      joinVoiceChannel({
        channelId: Number(channelId),
        channelName,
        serverName
      })
    } catch (err) {
      console.error('채널 입장 실패:', err)
    }
  }

  // 채널 퇴장
  const leaveChannel = (senderId: string) => {
    send('/pub/stream/group', {
      messageType: 'LEAVE_CHANNEL',
      channelId,
      senderId
    })

    leaveVoiceChannel()

    unsubscribe(`/sub/stream/direct/${channelId}`, subscriptionId + 'direct')
    unsubscribe(`/sub/stream/group/${channelId}`, subscriptionId + 'group')

    initialize()
  }

  const initialize = () => {
    if (senderPcRef.current) {
      senderPcRef.current.close()
      senderPcRef.current = null
    }

    receiverPcsRef.current.forEach((pc) => pc.close())
    receiverPcsRef.current.clear()
    setUsers([])
  }

  return {
    users,
    joinChannel,
    leaveChannel
  }
}
