import { useRef } from 'react'

import { TURN_SERVER_URL } from '@/constants/env'
import { useSignalingStomp } from '@/stores/use-signaling-stomp-store'
import useUserStatus from '@/stores/use-user-status'
import { errorLog, log } from '@/utils/log'

const RTC_CONFIGURATION = {
  iceServers: [
    {
      urls: [
        'stun:stun.l.google.com:19302',
        'stun:stun1.l.google.com:19302',
        'stun:stun2.l.google.com:19302'
      ]
    },
    { urls: TURN_SERVER_URL, username: 'kurentouser', credential: 'kurentopassword' }
  ],
  iceTransportPolicy: 'all',
  iceCandidatePoolSize: 3
} as RTCConfiguration

const SUBSCRIPTION_ID = 'signaling-with'
const DESTINATION_GROUP = '/sub/stream/mesh-group'
const DESTINATION_DIRECT = '/sub/stream/mesh-direct'

interface SignalingMessage {
  messageType: string
  channelId: string
  senderId: string
  receiverId?: string
  sdp?: RTCSessionDescriptionInit
  candidate?: RTCIceCandidate
  participants?: number[]
}

export const useSignalingWithMesh = (
  userId: string,
  channelId: string,
  channelName: string,
  serverName: string
) => {
  const { send, subscribe, unsubscribe } = useSignalingStomp()
  const { joinVoiceChannel, leaveVoiceChannel } = useUserStatus()
  const pcObj = useRef<
    Record<string, { senderId: string; pc: RTCPeerConnection; stream: MediaStream | null }>
  >({})
  const pendingCandidatesRef = useRef(new Map<string, RTCIceCandidate[] | undefined>())

  const createConnection = (senderId: string) => {
    log('Creating sender peer connection for:', senderId)
    if (pcObj.current[senderId]) {
      log('Sender peer connection already exists for:', senderId)
      return pcObj.current[senderId]
    }
    const myPeerConnection = new RTCPeerConnection(RTC_CONFIGURATION)

    myPeerConnection.oniceconnectionstatechange = () => {
      log(`❄️ ICE Connection State Changed: ${myPeerConnection.iceConnectionState}`)
    }

    myPeerConnection.ontrack = ({ streams }) => {
      log('Received ontrack event:', streams)
      streams.forEach((stream) => stream.getTracks().forEach((track) => (track.enabled = true)))
      paintPeerFace(streams[0], senderId)
    }

    myPeerConnection.onicecandidate = ({ candidate }) => {
      if (candidate) {
        send('/pub/stream/mesh-group', {
          messageType: 'CANDIDATE',
          channelId,
          senderId: userId,
          candidate
        })
      }
    }

    pcObj.current[senderId] = { senderId, pc: myPeerConnection, stream: null }

    return pcObj.current[senderId]
  }

  const getMedia = async (deviceId?: string) => {
    const initialConstraints = {
      audio: true,
      video: { facingMode: 'user' }
    }
    const cameraConstraints = {
      audio: true,
      video: { deviceId: { exact: deviceId } }
    }

    try {
      if (!pcObj.current[userId]) {
        pcObj.current[userId] = createConnection(userId)
      }

      pcObj.current[userId].stream = await navigator.mediaDevices.getUserMedia(
        deviceId ? cameraConstraints : initialConstraints
      )

      if (!deviceId) {
        // mute default
        pcObj.current[userId].stream //
          .getAudioTracks()
          .forEach((track) => (track.enabled = false))
      }
    } catch (error) {
      log(error as string)
    }
  }

  const createOffer = async (pc: RTCPeerConnection, receiverId: string) => {
    console.log(`[Offer] Creating for ${receiverId}, current state:`, {
      signalingState: pc.signalingState,
      connectionState: pc.connectionState
    })

    try {
      const offer = await pc.createOffer({
        offerToReceiveAudio: true,
        offerToReceiveVideo: true
      })

      await pc.setLocalDescription(offer)
      console.log('[Offer] Local description set')

      send('/pub/stream/mesh-group', {
        messageType: 'OFFER',
        channelId,
        senderId: userId,
        receiverId,
        sdp: offer
      })
      console.log('[Offer] Sent to:', receiverId)
    } catch (error) {
      console.error('[Offer] Error creating offer:', error)
    }
  }

  const handleChannelFull = () => {
    cleanup()
    leaveVoiceChannel()
  }

  const handleUserLeft = (message: SignalingMessage) => {
    const senderId = message.senderId
    const leftUser = pcObj.current[senderId]
    if (!leftUser) return
    log(`👋 User ${senderId} left. Cleaning up their connection...`)
    leftUser.pc.getSenders().forEach((sender) => {
      if (sender.track) {
        sender.track.stop()
      }
    })
    leftUser.pc.close()
    delete pcObj.current[senderId]
    const videoElement = document.getElementById(`peer-${senderId}`)
    if (videoElement) {
      videoElement.remove()
      log(`🧹 Removed video element for ${senderId}`)
    }
    log(`✅ Cleaned up resources for user: ${senderId}`)
  }

  const handleUserJoined = async (message: SignalingMessage) => {
    console.log(`[Join] User joined: ${message.senderId}`)

    // 새로 들어온 유저가 자신이 아닐 경우에만 offer 생성
    if (message.senderId !== userId) {
      const pc = createConnection(message.senderId)
      await createOffer(pc.pc, message.senderId)
      console.log(`[Join] Created offer for new user: ${message.senderId}`)
    }
  }

  const handleExistUsers = async (message: SignalingMessage) => {
    if (!message.participants) return
    console.log('[ExistUsers] Current participants:', message.participants)

    // 자신을 제외한 기존 참가자들에 대해 offer 생성
    for (const participantId of message.participants) {
      if (participantId.toString() === userId) continue

      console.log(`[ExistUsers] Creating connection for: ${participantId}`)
      const pc = createConnection(participantId.toString())
      await createOffer(pc.pc, participantId.toString())
    }
  }

  const handleOffer = async (message: SignalingMessage) => {
    if (!message.sdp || message.senderId === userId) return
    log(`[Offer] Received from ${message.senderId}, current state:`, {
      signalingState: pcObj.current[message.senderId]?.pc.signalingState,
      connectionState: pcObj.current[message.senderId]?.pc.connectionState
    })
    let answer: RTCSessionDescriptionInit | undefined
    const newUser = createConnection(message.senderId)

    try {
      await newUser.pc.setRemoteDescription(message.sdp)
      log('[Offer] Remote description set')

      if (newUser.pc.signalingState !== 'stable') {
        answer = await newUser.pc.createAnswer()
        await newUser.pc.setLocalDescription(answer)
        log('[Offer] Local description (answer) set')
      } else {
        log('[Offer] Already in stable state')
      }

      await processPendingCandidates(message.senderId)
      log('[Offer] Processed pending candidates')

      send('/pub/stream/mesh-group', {
        messageType: 'ANSWER',
        channelId,
        senderId: userId,
        sdp: answer
      })
      log('[Offer] Answer sent')
    } catch (error) {
      errorLog('[Offer] Error:', error)
    }
  }

  const handleAnswer = async (message: SignalingMessage) => {
    if (message.messageType !== 'ANSWER' || !message.sdp || message.senderId === userId) return
    log(`[Answer] Processing from ${message.senderId}`)

    const currentUser = pcObj.current[message.senderId]
    if (!currentUser) {
      errorLog('[Answer] No PC found for sender:', message.senderId)
      return
    }

    try {
      log('[Answer] Current state:', {
        signalingState: currentUser.pc.signalingState,
        connectionState: currentUser.pc.connectionState,
        iceConnectionState: currentUser.pc.iceConnectionState
      })

      // stable 상태가 아닐 때만 answer 처리
      if (currentUser.pc.signalingState !== 'stable') {
        await currentUser.pc.setRemoteDescription(new RTCSessionDescription(message.sdp))
        log('[Answer] Remote description set successfully')
        await processPendingCandidates(message.senderId)
      } else {
        log('[Answer] Already in stable state')
      }
    } catch (error) {
      errorLog('[Answer] Error setting remote description:', error)
      if (currentUser.pc.signalingState !== 'closed') {
        currentUser.pc.close()
        delete pcObj.current[message.senderId]
        createConnection(message.senderId)
      }
    }
  }

  const handleCandidate = async (message: SignalingMessage) => {
    if (message.messageType !== 'CANDIDATE' || !message.candidate || message.senderId === userId)
      return

    const currentUser = pcObj.current[message.senderId]

    if (!currentUser) return

    try {
      // SDP가 설정되어 있는지 확인
      if (!currentUser.pc.remoteDescription || !currentUser.pc.localDescription) {
        log('SDP가 아직 설정되지 않음, candidate 저장')

        // 해당 peer의 candidate 배열이 없으면 생성
        if (!pendingCandidatesRef.current.has(message.senderId)) {
          pendingCandidatesRef.current.set(message.senderId, [])
        }

        // candidate 임시 저장
        pendingCandidatesRef.current.get(message.senderId)?.push(message.candidate)
        return
      }

      await currentUser.pc.addIceCandidate(message.candidate)
      log('ICE Candidate 추가 성공')
    } catch (error) {
      errorLog('ICE Candidate 추가 오류:', error)
    }
  }

  // SDP 설정이 완료된 후 저장된 candidate 처리
  const processPendingCandidates = async (senderId: string) => {
    const currentUser = pcObj.current[senderId]
    if (!currentUser || !pendingCandidatesRef.current.has(senderId)) return

    const candidates = pendingCandidatesRef.current.get(senderId) || []
    log(`Processing ${candidates.length} pending candidates for ${senderId}`)

    for (const candidate of candidates) {
      try {
        await currentUser.pc.addIceCandidate(candidate)
        log('Pending ICE Candidate 추가 성공')
      } catch (error) {
        errorLog('Pending ICE Candidate 추가 오류:', error)
      }
    }

    // 처리 완료된 candidate 제거
    pendingCandidatesRef.current.delete(senderId)
  }

  const paintPeerFace = (peerStream: MediaStream, id: string) => {
    log(`Painting peer face for ${id}`, peerStream)
    const existingVideo = document.getElementById(`peer-${id}`)
    if (existingVideo) existingVideo.remove()

    const streamContainer = document.querySelector('#streams')
    if (!streamContainer) return errorLog('Stream container not found')

    const videoContainer = document.createElement('div')
    videoContainer.className = 'peer-video-container'
    videoContainer.id = `peer-${id}`
    videoContainer.style.width = '300px'
    videoContainer.style.height = '200px'
    videoContainer.style.margin = '10px'

    const video = document.createElement('video')
    video.srcObject = peerStream
    video.autoplay = true
    video.playsInline = true
    video.muted = id === userId
    video.style.width = '100%'
    video.style.height = '100%'
    video.style.objectFit = 'cover'

    video.onloadedmetadata = () => {
      log(`Video loaded for peer ${id}`)
      video.play().catch((e) => errorLog('Video playback failed:', e))
    }

    videoContainer.appendChild(video)
    streamContainer.appendChild(videoContainer)
  }

  const initialize = () => {
    subscribe(
      SUBSCRIPTION_ID + '-mesh-group',
      `${DESTINATION_GROUP}/${channelId}`,
      (message: SignalingMessage) => {
        log('🔥 Received message:', message)
        if (filterMessage(message)) {
          switch (message.messageType) {
            case 'USER_JOINED':
              handleUserJoined(message)
              break
            case 'USER_LEFT':
              handleUserLeft(message)
              break
            case 'OFFER':
              handleOffer(message)
              break
            case 'ANSWER':
              handleAnswer(message)
              break
            case 'CANDIDATE':
              handleCandidate(message)
              break
            default:
              throw new Error(`Unknown message type: ${message.messageType}`)
          }
        }
      }
    )
    subscribe(
      SUBSCRIPTION_ID + '-mesh-direct',
      `${DESTINATION_DIRECT}/${userId}`,
      (message: SignalingMessage) => {
        log('🔥 Received message:', message)
        if (filterMessage(message)) {
          switch (message.messageType) {
            case 'CHANNEL_FULL':
              handleChannelFull()
              break
            case 'EXIST_USERS':
              handleExistUsers(message)
              break
            default:
              throw new Error(`Unknown message type: ${message.messageType}`)
          }
        }
      }
    )

    getMedia()
  }

  const filterMessage = (message: SignalingMessage) => {
    if (
      message.messageType === 'OFFER' ||
      message.messageType === 'ANSWER' ||
      message.messageType === 'CANDIDATE' ||
      message.messageType === 'USER_JOINED' ||
      message.messageType === 'USER_LEFT'
    ) {
      return message.senderId !== userId
    }
    return true
  }

  const cleanup = () => {
    log('🧹 Cleaning up WebRTC connections...')

    // 🔹 STOMP 구독 취소
    unsubscribe(`${SUBSCRIPTION_ID}-mesh-group`, DESTINATION_GROUP)
    unsubscribe(`${SUBSCRIPTION_ID}-mesh-direct`, DESTINATION_DIRECT)

    // 🔹 모든 수신 PeerConnection 정리
    Object.values(pcObj.current).forEach((user) => {
      user.pc.getSenders().forEach((sender) => {
        if (sender.track) {
          sender.track.stop() // 🎯 트랙 정리
        }
      })
      user.pc.close() // 🎯 연결 종료
      log(`🛑 Closed receiver PC for: ${user.pc}`)
    })
    pcObj.current = {}

    log('✅ Cleanup completed.')
  }

  const joinChannel = () => {
    initialize()
    send('/pub/stream/mesh-group', { messageType: 'JOIN_CHANNEL', channelId, senderId: userId })
    createConnection(userId)
    joinVoiceChannel({ channelId: parseInt(channelId), channelName, serverName })
  }

  const leaveChannel = () => {
    send('/pub/stream/mesh-group', {
      messageType: 'LEAVE_CHANNEL',
      channelId: parseInt(channelId),
      senderId: userId
    })
    cleanup()
    leaveVoiceChannel()
  }

  return { joinChannel, leaveChannel }
}
