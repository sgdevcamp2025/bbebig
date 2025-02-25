import { useEffect, useRef } from 'react'

import { useSignalingStomp } from '@/stores/use-signaling-stomp-store'
import useUserStatus from '@/stores/use-user-status'

const RTC_CONFIGURATION: RTCConfiguration = {
  iceServers: [
    { urls: 'stun:stun.l.google.com:19302' },
    {
      urls: 'turn:13.125.13.209:3478?transport=udp',
      username: 'kurentouser',
      credential: 'kurentopassword'
    }
  ],
  iceTransportPolicy: 'relay',
  iceCandidatePoolSize: 0
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
    const pc = new RTCPeerConnection(RTC_CONFIGURATION)
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

    senderPcRef.current.onicecandidate = (event) => {
      if (event?.candidate) {
        send('CANDIDATE', {
          channelId: channelId,
          senderId: userId,
          candidate: event.candidate
        })
      }
    }
  }

  const createOffer = async (pc: RTCPeerConnection, receiverId: string) => {
    console.log(`${receiverId} 에게 제안 보내기`)

    const offer = await pc.createOffer({
      offerToReceiveAudio: true,
      offerToReceiveVideo: true
    })

    await pc.setRemoteDescription(offer)

    send('/pub/stream/mesh-group', {
      messageType: 'OFFER',
      channelId: channelId,
      senderId: userId,
      receiverId,
      sdp: offer
    })
  }

  const handleOffer = async (message: SignalingMessage) => {
    console.log(`${message.senderId} 으로부터 offer 수신`)
    const pc = receiverPcsRef.current.get(message.senderId) || senderPcRef.current

    if (!pc || !message.sdp) return

    await pc.setRemoteDescription(new RTCSessionDescription(message.sdp))
    const answer = await pc.createAnswer()
    await pc.setLocalDescription(answer)

    send('/pub/stream/mesh-group', {
      messageType: 'ANSWER',
      channelId: channelId,
      senderId: userId,
      receiverId: message.receiverId,
      sdp: answer
    })
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
    if (!message.sdp || !senderPcRef.current) return
    createSenderPeerConnection(message.senderId)
    const remoteDescription = new RTCSessionDescription(message.sdp)
    senderPcRef.current?.setRemoteDescription(remoteDescription)

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
    const pc = receiverPcsRef.current.get(message.senderId)
    if (!pc) return
    pc.close()
    receiverPcsRef.current.delete(message.senderId)
  }

  const handleChannelFull = (message: SignalingMessage) => {
    cleanup()
    leaveVoiceChannel()
  }

  const handleExistUsers = async (message: SignalingMessage) => {
    const participants = message.participants

    const offer = await senderPcRef.current?.createOffer({
      offerToReceiveAudio: true,
      offerToReceiveVideo: true,
      iceRestart: true
    })

    await senderPcRef.current?.setLocalDescription(offer)

    participants?.forEach((participant) => {
      if (participant.toString() === userId && !senderPcRef.current) return

      createSenderPeerConnection(participant.toString())

      if (senderPcRef.current) {
        createOffer(senderPcRef.current, participant.toString())
      }
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
        console.log('서버 이벤트 수신, message-group', message)
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
        console.log('서버 이벤트 수신, message-direct', message)
        switch (message.messageType) {
          case 'CHANNEL_FULL':
            handleChannelFull(message)
            break
          case 'EXIST_USERS':
            handleExistUsers(message)
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
