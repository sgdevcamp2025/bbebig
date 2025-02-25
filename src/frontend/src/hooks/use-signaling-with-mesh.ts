import { useEffect, useRef } from 'react'

import { TURN_SERVER_URL } from '@/constants/env'
import { useSignalingStomp } from '@/stores/use-signaling-stomp-store'
import useUserStatus from '@/stores/use-user-status'

const PC_CONFIG: RTCConfiguration = {
  iceServers: [
    { urls: 'stun:stun.l.google.com:19302' },
    {
      urls: TURN_SERVER_URL,
      username: 'kurentouser',
      credential: 'kurentouser'
    }
  ],
  iceTransportPolicy: 'relay' as RTCIceTransportPolicy,
  iceCandidatePoolSize: 0,
  bundlePolicy: 'max-bundle' as RTCBundlePolicy,
  rtcpMuxPolicy: 'require' as RTCRtcpMuxPolicy
}

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
  const myStreamRef = useRef<MediaStream | null>(null)
  const senderPcRef = useRef<RTCPeerConnection | null>(null)
  const receiverPcsRef = useRef<Map<string, RTCPeerConnection>>(new Map())

  useEffect(() => {
    return () => {
      cleanup()
    }
  }, [])

  const createSenderPeerConnection = async (senderId: string) => {
    const pc = new RTCPeerConnection(PC_CONFIG)
    pc.ontrack = (event) => {
      paintPeerFace(event.streams[0], senderId)
    }

    if (myStreamRef.current) {
      myStreamRef.current.getTracks().forEach((track) => {
        if (myStreamRef.current) pc.addTrack(track, myStreamRef.current)
      })
    }

    const oldPc = senderPcRef.current

    if (oldPc) {
      oldPc.close()
    }

    senderPcRef.current = pc

    const offer = await pc.createOffer({
      offerToReceiveAudio: true,
      offerToReceiveVideo: true,
      iceRestart: true
    })

    await pc.setLocalDescription(offer)
  }

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

  const handleUserJoined = (message: SignalingMessage) => {
    if (message.messageType !== 'USER_JOINED' || !message.sdp || !senderPcRef.current) return
    createSenderPeerConnection(message.senderId)

    senderPcRef.current?.setRemoteDescription(new RTCSessionDescription(message.sdp))
    senderPcRef.current?.createAnswer({
      offerToReceiveAudio: true,
      offerToReceiveVideo: true,
      iceRestart: true
    })
    senderPcRef.current.onicecandidate = (event) => {
      if (event?.candidate) {
        send('CANDIDATE', {
          channelId: channelId,
          senderId: userId,
          receiverId: message.senderId,
          candidate: event.candidate
        })
      }
    }
  }

  const handleUserLeft = (message: SignalingMessage) => {
    if (message.messageType !== 'USER_LEFT') return
  }

  const handleOffer = async (message: SignalingMessage) => {
    if (message.messageType !== 'OFFER' || !message.sdp) return

    const pc = new RTCPeerConnection(PC_CONFIG)
    await pc.setRemoteDescription(new RTCSessionDescription(message.sdp))

    const answer = await pc.createAnswer({
      offerToReceiveAudio: true,
      offerToReceiveVideo: true,
      iceRestart: true
    })

    await pc.setLocalDescription(answer)

    send('/pub/stream/mesh-group', {
      messageType: 'ANSWER',
      channelId: channelId,
      senderId: userId,
      receiverId: message.senderId,
      sdp: answer
    })

    pc.onicecandidate = (event) => {
      if (event.candidate) {
        send('/pub/stream/mesh-group', {
          messageType: 'CANDIDATE',
          channelId: channelId,
          senderId: userId,
          receiverId: message.senderId,
          candidate: event.candidate
        })
      }
    }
  }

  const handleChannelFull = (message: SignalingMessage) => {
    if (message.messageType !== 'CHANNEL_FULL') return
    cleanup()
    leaveVoiceChannel()
  }

  const handleExistUsers = async (message: SignalingMessage) => {
    if (message.messageType !== 'EXIST_USERS') return
    const participants = message.participants
    const offer = await senderPcRef.current?.createOffer({
      offerToReceiveAudio: true,
      offerToReceiveVideo: true,
      iceRestart: true
    })

    if (!offer) return

    await senderPcRef.current?.setLocalDescription(offer)

    participants?.forEach((participant) => {
      if (participant.toString() === userId) return
      createSenderPeerConnection(participant.toString())
      senderPcRef.current?.setRemoteDescription(new RTCSessionDescription(offer))
      send('/pub/stream/mesh-group', {
        messageType: 'OFFER',
        channelId: channelId,
        senderId: userId,
        receiverId: participant.toString(),
        sdp: senderPcRef.current?.localDescription
      })
    })
  }

  const handleAnswer = (message: SignalingMessage) => {
    if (message.messageType !== 'ANSWER' || !message.sdp) return
    const pc = receiverPcsRef.current.get(message.senderId)
    if (!pc) return
    pc.setRemoteDescription(new RTCSessionDescription(message.sdp))
  }

  const handleCandidate = (message: SignalingMessage) => {
    if (message.messageType !== 'CANDIDATE' || !message.candidate) return
    const pc = receiverPcsRef.current.get(message.senderId)
    if (!pc) return
    pc.addIceCandidate(message.candidate)
  }

  const initialize = () => {
    subscribe(
      SUBSCRIPTION_ID + '-mesh-group',
      DESTINATION_GROUP + `/${channelId}`,
      (message: SignalingMessage) => {
        console.log(message, 'message')
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
    )

    subscribe(
      SUBSCRIPTION_ID + '-mesh-direct',
      DESTINATION_DIRECT + `/${userId}`,
      (message: SignalingMessage) => {
        console.log(message, 'message')
        switch (message.messageType) {
          case 'CHANNEL_FULL':
            handleChannelFull(message)
            break
          case 'EXIST_USERS':
            handleExistUsers(message)
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
    )
  }

  const cleanup = () => {
    unsubscribe(SUBSCRIPTION_ID + '-mesh-group', DESTINATION_GROUP)
    unsubscribe(SUBSCRIPTION_ID + '-mesh-direct', DESTINATION_DIRECT)
    unsubscribe(SUBSCRIPTION_ID + '-direct', '/pub/stream/direct')
    unsubscribe(SUBSCRIPTION_ID + '-group', '/pub/stream/group')

    receiverPcsRef.current.forEach((pc) => {
      pc.close()
    })
    receiverPcsRef.current.clear()
  }

  const joinChannel = () => {
    initialize()

    const message = {
      messageType: 'JOIN_CHANNEL',
      channelId,
      senderId: userId
    }

    send('/pub/stream/mesh-group', message)

    createSenderPeerConnection(userId)

    joinVoiceChannel({
      channelId: parseInt(channelId),
      channelName: channelName,
      serverName: serverName
    })
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
