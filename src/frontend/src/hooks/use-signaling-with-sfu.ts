import { useEffect, useRef } from 'react'

import { TURN_SERVER_URL } from '@/constants/env'
import { useSignalingStomp } from '@/stores/use-signaling-stomp-store'
import { useUserStatus } from '@/stores/use-user-status-store'

import useGetSelfUser from './queries/user/useGetSelfUser'

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
  participants?: string[]
}

export function useSignalingWithSFU(channelId: number, channelName: string, serverName: string) {
  const subscriptionId = `subscription-${channelId}`
  const { send, subscribe, unsubscribe } = useSignalingStomp()
  const { joinVoiceChannel, leaveVoiceChannel, getCurrentChannelInfo } = useUserStatus()

  const myFaceRef = useRef<HTMLVideoElement | null>(null) // 내 비디오 엘리먼트
  const callRef = useRef<HTMLDivElement | null>(null) // 상대방(원격) 비디오 담을 컨테이너
  const myStreamRef = useRef<MediaStream | null>(null) // 내 로컬 미디어 스트림
  const peersRef = useRef<Record<string, RTCPeerConnection>>({}) // socketId -> RTCPeerConnection
  const userMapRef = useRef<Record<string, string>>({}) // socketId -> userId (상대방 표시용)

  const self = useGetSelfUser()

  useEffect(() => {
    if (getCurrentChannelInfo()?.channelId === Number(channelId)) {
      getMedia()
    }
  }, [channelId, getCurrentChannelInfo])

  function makeConnection(socketId: number) {
    if (peersRef.current[socketId]) return

    const pc = new RTCPeerConnection({
      iceServers: [
        {
          urls: 'stun:stun.l.google.com:19302'
        },
        {
          urls: TURN_SERVER_URL,
          username: 'kurentouser',
          credential: 'kurentopassword'
        }
      ],
      bundlePolicy: 'balanced'
    })

    pc.onicecandidate = (event) => {
      if (event.candidate) {
        send('/pub/stream/group', {
          messageType: 'CANDIDATE',
          channelId,
          senderId: self?.id,
          receiverId: socketId,
          candidate: event.candidate
        })
      }
    }

    pc.ontrack = (event) => {
      const containerId = `container_${socketId}`
      let videoContainer = document.getElementById(containerId)

      if (!videoContainer) {
        videoContainer = document.createElement('div')
        videoContainer.id = containerId
        videoContainer.style.display = 'flex'
        videoContainer.style.flexDirection = 'column'
        videoContainer.style.alignItems = 'center'
        videoContainer.style.margin = '10px'
      }

      let peerVideo = document.getElementById(String(socketId)) as HTMLVideoElement
      if (!peerVideo) {
        peerVideo = document.createElement('video')
        peerVideo.id = String(socketId)
        peerVideo.autoplay = true
        peerVideo.playsInline = true
        peerVideo.width = 400
        peerVideo.height = 300
      }
      peerVideo.srcObject = event.streams[0]

      videoContainer.appendChild(peerVideo)

      if (callRef.current && !document.getElementById(containerId)) {
        callRef.current.appendChild(videoContainer)
      }
    }

    peersRef.current[socketId] = pc
  }

  const handleUserJoined = async (message: SignalingMessage) => {
    // userMapRef에 저장 (표시용)
    userMapRef.current[message.senderId] = String(message.senderId)

    // PeerConnection 생성
    makeConnection(Number(message.senderId))

    if (!peersRef.current[message.senderId]) {
      await getMedia()
    }

    // Offer 생성
    const offer = await peersRef.current[message.senderId].createOffer()
    await peersRef.current[message.senderId].setLocalDescription(offer)

    // 서버에 offer 전송
    send('/pub/stream/group', {
      messageType: 'OFFER',
      channelId,
      senderId: self?.id,
      sdp: offer
    })
  }

  const handleOffer = async (message: SignalingMessage) => {
    if (!message.sdp) return
    // userMapRef에 저장
    userMapRef.current[message.senderId] = String(message.senderId)

    if (!myStreamRef.current) {
      await getMedia()
    }
    makeConnection(Number(message.senderId))

    await peersRef.current[message.senderId].setRemoteDescription(message.sdp)

    // Answer 생성
    const answer = await peersRef.current[message.senderId].createAnswer()
    await peersRef.current[message.senderId].setLocalDescription(answer)

    send('/pub/stream/group', {
      messageType: 'ANSWER',
      channelId,
      senderId: self?.id,
      receiverId: message.senderId,
      sdp: answer
    })
  }

  const handleAnswer = (message: SignalingMessage) => {
    if (!message.sdp) return
    // userMapRef에 저장
    userMapRef.current[message.senderId] = String(message.senderId)

    const pc = peersRef.current[message.senderId]
    if (pc) {
      pc.setRemoteDescription(message.sdp)
    }
  }

  const handleUserLeft = (message: SignalingMessage) => {
    if (!message.senderId) return
    // userMapRef에서 제거
    delete userMapRef.current[message.senderId]

    // PeerConnection 제거
    const pc = peersRef.current[message.senderId]
    if (pc) {
      pc.close()
      delete peersRef.current[message.senderId]
    }
  }

  const handleIce = (message: SignalingMessage) => {
    if (!message.candidate) return
    // userMapRef에 저장 (중복이어도 문제 없음)
    userMapRef.current[message.senderId] = String(message.senderId)

    const pc = peersRef.current[message.senderId]
    if (pc) {
      pc.addIceCandidate(message.candidate)
    }
  }

  async function getMedia(deviceId?: string) {
    const initialConstraints = {
      audio: true,
      video: { facingMode: 'user' }
    }
    const cameraConstraints = {
      audio: true,
      video: { deviceId: { exact: deviceId } }
    }

    try {
      const stream = await navigator.mediaDevices.getUserMedia(
        deviceId ? cameraConstraints : initialConstraints
      )
      myStreamRef.current = stream

      // 내 비디오에 연결
      if (myFaceRef.current) {
        myFaceRef.current.srcObject = stream
        myFaceRef.current.muted = true // 자기 자신의 오디오는 음소거
      }

      // 카메라 목록 가져오기 (처음 한 번만)
      if (!deviceId) {
        // await getCameras()
      }
    } catch (err) {
      console.error('getMedia error:', err)
    }
  }

  // 채널 입장
  const joinChannel = async () => {
    joinVoiceChannel({
      channelId: Number(channelId),
      channelName,
      serverName
    })

    subscribe(
      subscriptionId + 'direct',
      `/sub/stream/direct/${self?.id}`,
      (message: SignalingMessage) => {
        console.log('Direct message received:', message)
        switch (message.messageType) {
          case 'OFFER':
            handleOffer(message)
            break
          case 'CANDIDATE':
            handleIce(message)
            break
          case 'ANSWER':
            handleAnswer(message)
            break
          case 'EXIST_USERS':
            if (message.participants) {
              const uniqueParticipants = [...new Set(message.participants)]

              // 새로운 사용자만 처리
              for (const id of uniqueParticipants) {
                if (id === String(self?.id)) continue
                handleUserJoined({ ...message, senderId: id })
              }
            }
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
            handleUserJoined(message)
            break
          case 'USER_LEFT':
            handleUserLeft(message)
            break
        }
      }
    )

    send('/pub/stream/group', {
      messageType: 'JOIN_CHANNEL',
      channelId,
      senderId: self?.id
    })
  }

  // 채널 퇴장
  const leaveChannel = () => {
    if (myStreamRef.current) {
      myStreamRef.current.getTracks().forEach((track) => track.stop())
      myStreamRef.current = null
    }
    send('/pub/stream/group', {
      messageType: 'LEAVE_CHANNEL',
      channelId,
      senderId: self?.id
    })
    unsubscribe(`/sub/stream/direct/${channelId}`, subscriptionId + 'direct')
    unsubscribe(`/sub/stream/group/${channelId}`, subscriptionId + 'group')

    leaveVoiceChannel()
  }

  const isInVoiceChannel = getCurrentChannelInfo()?.channelId === Number(channelId)

  return {
    callRef,
    myFaceRef,
    joinChannel,
    leaveChannel,
    isInVoiceChannel
  }
}
