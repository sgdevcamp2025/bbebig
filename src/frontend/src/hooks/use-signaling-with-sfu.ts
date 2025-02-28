import { useRef } from 'react'

import { useSignalingStomp } from '@/stores/use-signaling-stomp-store'

import useUserStatus from '../stores/use-user-status'
import useMediaControl from './use-media-control'

// WebRTC 설정
const PC_CONFIG: RTCConfiguration = {
  iceServers: [
    { urls: 'stun:stun.l.google.com:19302' },
    {
      urls: [
        'turn:openrelay.metered.ca:80?transport=udp', // UDP 우선
        'turn:openrelay.metered.ca:80?transport=tcp', // TCP 백업
        'turn:openrelay.metered.ca:443?transport=tcp' // TLS 백업
      ],
      username: 'openrelayproject',
      credential: 'openrelayproject'
    }
  ],
  iceTransportPolicy: 'all' as RTCIceTransportPolicy,
  iceCandidatePoolSize: 5, // 후보 수집 확장
  bundlePolicy: 'max-bundle' as RTCBundlePolicy,
  rtcpMuxPolicy: 'require' as RTCRtcpMuxPolicy
}

export interface WebRTCUser {
  id: string
  stream: MediaStream | null
  audioEnabled: boolean
  videoEnabled: boolean
  connected: boolean
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
  const { joinVoiceChannel, leaveVoiceChannel } = useUserStatus()

  const senderPcRef = useRef<RTCPeerConnection | null>(null)
  const receiverPcsRef = useRef<Map<string, RTCPeerConnection>>(new Map())
  const candidateRef = useRef<Record<string, RTCIceCandidate[]>>({})

  const paintPeerFace = (peerStream: MediaStream, id: string) => {
    const stream = document.querySelector('#streams')
    const video = document.createElement('video')
    video.srcObject = peerStream
    video.id = id
    video.autoplay = true
    video.playsInline = true
    video.style.width = '100%'
    video.style.height = '100%'
    stream?.appendChild(video)
  }
  // Sender PeerConnection 생성
  const createSenderPeerConnection = async () => {
    try {
      if (senderPcRef.current?.signalingState === 'have-local-offer') {
        console.error('이미 오퍼를 보낸 상태입니다. 재연결 필요')
        await restartICE()
        return
      }

      // 신규 피어커넥션 생성 전 기존 연결 정리
      if (senderPcRef.current) {
        senderPcRef.current.close()
        senderPcRef.current = null
      }

      const pc = new RTCPeerConnection(PC_CONFIG)

      senderPcRef.current = pc

      // 신호 상태 모니터링 추가
      pc.onsignalingstatechange = () => {
        console.log('Signaling State:', pc.signalingState)
        if (pc.signalingState === 'stable') {
          console.log('협상 완료 상태 - 새로운 오퍼 금지')
        }
      }

      // ICE 상태 종합 모니터링
      pc.addEventListener('iceconnectionstatechange', () => {
        console.log(
          'ICE 상태:',
          `Connection: ${pc.connectionState}, ` +
            `ICE: ${pc.iceConnectionState}, ` +
            `Gathering: ${pc.iceGatheringState}`
        )
      })

      pc.addEventListener('icegatheringstatechange', () => {
        console.log('ICE 수집 상태:', pc.iceGatheringState, new Date().toISOString())
      })

      await startStream({ video: true, audio: true })

      const stream = await getStream()

      if (!stream) {
        console.error('Failed to get media stream')
        return
      }

      stream.getTracks().forEach((track) => {
        const sender = pc.addTrack(track, stream)
        if (sender.track) {
          sender.track.enabled = true
        }
      })

      pc.onconnectionstatechange = () => {
        console.log('Sender connection state:', pc.connectionState)
      }

      // ICE 상태 모니터링 추가
      pc.oniceconnectionstatechange = () => {
        console.log('ICE 상태 변화:', {
          iceState: pc.iceConnectionState,
          gatheringState: pc.iceGatheringState,
          signalingState: pc.signalingState,
          connectionState: pc.connectionState,
          timestamp: new Date().toISOString()
        })
      }

      try {
        const offer = await pc.createOffer({
          offerToReceiveAudio: true,
          offerToReceiveVideo: true,
          iceRestart: true
        })

        const offerMessage = {
          messageType: 'OFFER',
          channelId,
          senderId: userId,
          sdp: offer
        }

        send('/pub/stream/group', offerMessage)
      } catch (error) {
        console.error('Sender offer creation failed:', error)
      }

      // 최종 연결 상태 체크
      pc.addEventListener('connectionstatechange', () => {
        if (pc.connectionState === 'connected') {
          pc.getStats().then((stats) => {
            stats.forEach((report) => {
              if (report.type === 'inbound-rtp') {
                console.log('수신 미디어 상태:', {
                  kind: report.kind,
                  bytesReceived: report.bytesReceived,
                  packetsReceived: report.packetsReceived
                })
              }
            })
          })
        }
      })

      // 5초마다 ICE 상태 체크
      setInterval(() => {
        pc.getStats().then((stats) => {
          stats.forEach((report) => {
            if (report.type === 'transport') {
              console.log('ICE 후보 쌍:', report.selectedCandidatePair)
            }
          })
        })
      }, 5000)
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
    console.log('Answer 수신:', message.sdp)
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

      // 이미 존재하는 연결 체크
      const existingPc = receiverPcsRef.current.get(message.senderId)
      if (existingPc && existingPc.signalingState !== 'closed') {
        console.log('이미 연결이 존재합니다:', message.senderId)
        return
      }

      // 기존 연결 정리
      if (existingPc) {
        existingPc.close()
        receiverPcsRef.current.delete(message.senderId)
      }

      // 새 연결 생성
      const pc = new RTCPeerConnection(PC_CONFIG)

      // 스트림 처리를 위한 빈 스트림 생성
      const remoteStream = new MediaStream()
      console.log('새로운 미디어 스트림을 생성했습니다.', message.senderId)

      // 트랙 이벤트 핸들러
      pc.ontrack = (event) => {
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

        paintPeerFace(remoteStream, message.senderId)
      }

      // ICE 후보 처리
      pc.onicecandidate = ({ candidate }) => {
        if (candidate) {
          send('/pub/stream/group', {
            messageType: 'CANDIDATE',
            channelId,
            senderId: userId,
            receiverId: message.senderId,
            candidate
          })
        }

        console.log(candidateRef.current, 'candidateRef.current')
      }

      try {
        const offer = await pc.createOffer({
          offerToReceiveAudio: true,
          offerToReceiveVideo: true
        })

        await pc.setLocalDescription(offer)

        const offerMessage = {
          messageType: 'OFFER',
          channelId,
          senderId: userId,
          receiverId: message.senderId,
          sdp: offer
        }

        send('/pub/stream/group', offerMessage)
      } catch (error) {
        console.error('Offer creation failed:', error)
      }
    } catch (error) {
      console.error('User join handling failed:', error)
    }
  }

  const handleCandidate = async (message: SignalingMessage) => {
    console.log(message, 'handleCandidate')
    if (!message.candidate) return

    candidateRef.current[message.senderId] = [
      ...(candidateRef.current[message.senderId] || []),
      message.candidate
    ]

    try {
      const pc = receiverPcsRef.current.get(message.senderId) || senderPcRef.current
      if (!pc || pc.iceConnectionState === 'closed') {
        console.error('유효하지 않은 피어커넥션 상태')
        return
      }

      if (pc.signalingState !== 'stable') {
        console.warn('아직 협상이 완료되지 않아 후보 추가 불가')
        return
      }

      await pc.addIceCandidate(new RTCIceCandidate(message.candidate))
      console.log('ICE 후보 추가 성공')
    } catch (error) {
      console.error('ICE 후보 추가 실패:', error)
    }
  }

  const handleUserLeft = (message: SignalingMessage) => {
    console.log('사용자가 나갔습니다.', message.senderId)
  }

  // 채널 입장
  const joinChannel = async () => {
    initialize()

    try {
      subscribe(
        subscriptionId + 'direct',
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

                // 새로운 사용자만 처리
                for (const id of uniqueParticipants) {
                  if (id !== Number(userId)) {
                    handleUserJoined({ ...message, senderId: String(id) })
                  }
                }
              }
              break
            case 'ANSWER':
              handleAnswer(message)
              break
          }
        }
      )

      subscribe(
        subscriptionId + 'group',
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
              handleUserLeft(message)
              break
          }
        }
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
  const leaveChannel = () => {
    send('/pub/stream/group', {
      messageType: 'LEAVE_CHANNEL',
      channelId,
      senderId: userId
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
  }

  // ICE 재시작 로직 추가
  const restartICE = async () => {
    if (senderPcRef.current) {
      await senderPcRef.current.restartIce()
      console.log('ICE 재시작 완료')
    }
  }

  return {
    joinChannel,
    leaveChannel
  }
}
