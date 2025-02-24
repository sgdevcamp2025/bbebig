import kurentoUtils, { WebRtcPeer } from 'kurento-utils'
import { useEffect, useRef } from 'react'

import { useSignalingStomp } from '@/stores/use-signaling-stomp-store'
import { useUserChannelStatus } from '@/stores/use-user-status-store'

const SUBSCRIPTION_ID = 'subscription-kurento'

interface MessageType {
  messageType:
    | 'EXISTING_PARTICPANTS'
    | 'NEW_PARTICIPANT_ARRIVED'
    | 'PARTICIPANT_LEFT'
    | 'RECEIVE_VIDEO_ANSWER'
    | 'ICE_CANDIDATE'
  channelId: string
  senderId: string
  receiverId?: string
  sdp?: RTCSessionDescriptionInit
  candidate?: RTCIceCandidate
  participant?: number[]
}

class Participant {
  name: string
  rtcPeer: WebRtcPeer | null
  video: HTMLVideoElement
  sendMessage: (destination: string, body: any) => void

  constructor(name: string, send: (destination: string, body: any) => void) {
    this.name = name
    this.rtcPeer = null
    this.video = document.createElement('video')
    this.video.autoplay = true
    this.video.id = `video-${name}`
    this.sendMessage = send
  }

  getVideoElement() {
    return this.video
  }
  generateOffer(callback: (sdp: RTCSessionDescriptionInit, error: Error | null) => void) {
    this.rtcPeer?.generateOffer((error: string | undefined, sdp: string) => {
      if (error) {
        callback({ type: 'offer', sdp: '' }, new Error(error))
      } else {
        callback({ type: 'offer', sdp }, null)
      }
    })
  }
  onIceCandidate(candidate: RTCIceCandidate) {
    this.sendMessage('/pub/stream/direct', {
      messageType: 'ICE_CANDIDATE',
      channelId: this.name,
      senderId: this.name,
      candidate
    })
  }

  offerToReceiveVideo(offerSdp: RTCSessionDescriptionInit, error: Error | null) {
    if (error) return console.error('sdp offer error')
    this.sendMessage('/pub/stream/direct', {
      messageType: 'RECEIVE_VIDEO_ANSWER',
      channelId: this.name,
      senderId: this.name,
      sdp: offerSdp
    })
  }

  dispose() {
    this.rtcPeer?.dispose()
    this.video.srcObject = null
  }
}

export function useSignalingWithKurento(
  userId: string,
  channelId: string,
  channelName: string,
  serverName: string,
  initialParticipants = new Map<string, Participant>()
) {
  const { send, subscribe, unsubscribe } = useSignalingStomp()
  const participants = useRef<Map<string, Participant>>(initialParticipants)
  const { joinVoiceChannel, leaveVoiceChannel } = useUserChannelStatus()

  useEffect(() => {
    onSubscribe()
    return () => {
      onUnsubscribe()
    }
  }, [])

  const onSubscribe = () => {
    subscribe(SUBSCRIPTION_ID, `/sub/stream/direct/${userId}`, (message) => {
      switch (message.messageType) {
        case 'EXISTING_PARTICPANTS':
          handleExistingParticipants(message)
          break
        case 'NEW_PARTICIPANT_ARRIVED':
          handleNewParticipant(message)
          break
        case 'PARTICIPANT_LEFT':
          handleParticipantLeft(message)
          break
        case 'RECEIVE_VIDEO_ANSWER':
          handleReceiveVideoAnswer(message)
          break
        case 'ICE_CANDIDATE':
          handleIceCandidate(message)
          break
      }
    })
  }

  const onUnsubscribe = () => {
    unsubscribe(SUBSCRIPTION_ID, `/sub/stream/direct/${userId}`)
  }

  const handleExistingParticipants = async (message: MessageType) => {
    const constraints = {
      audio: true,
      video: {
        width: { min: 1280, max: 1280 },
        height: { min: 720, max: 720 }
      }
    }

    console.log(message.senderId + ' register in room ' + channelId)

    const participant = new Participant(message.senderId, send)
    const oldParticipant = participants.current.get(message.senderId)

    if (oldParticipant) {
      oldParticipant.dispose()
    }

    participants.current.set(message.senderId, participant)

    const video = participant.getVideoElement()

    const options = {
      localVideo: video,
      mediaConstraints: constraints,
      onicecandidate: participant.onIceCandidate.bind(participant)
    }

    participant.rtcPeer = kurentoUtils.WebRtcPeer.WebRtcPeerSendonly(options, function (error) {
      if (error) {
        return console.error(error)
      }
      participant.generateOffer(participant.offerToReceiveVideo.bind(participant))
    })

    message.participant?.forEach(recivedVideo)
  }

  const handleNewParticipant = (message: MessageType) => {
    if (message.senderId) {
      recivedVideo(Number(message.senderId))
    }
  }

  const handleParticipantLeft = (message: MessageType) => {
    console.log('Participant left ' + message.senderId + ' left')
    const participant = participants.current.get(message.senderId)
    if (participant) {
      participant.dispose()
    }
    participants.current.delete(message.senderId)
  }

  const handleReceiveVideoAnswer = (message: MessageType) => {
    const participant = participants.current.get(message.senderId)

    if (participant && message.sdp?.sdp) {
      participant.rtcPeer?.processAnswer(message.sdp.sdp, function (error) {
        if (error) return console.error(error)
      })
    }
  }

  const handleIceCandidate = (message: MessageType) => {
    const participant = participants.current.get(message.senderId)
    if (participant && message.candidate) {
      participant.rtcPeer?.addIceCandidate(message.candidate, function (error) {
        if (error) return console.error(error)
      })
    }
  }

  const recivedVideo = (number: number) => {
    const participant = new Participant(number.toString(), send)
    const oldParticipant = participants.current.get(number.toString())
    if (oldParticipant) {
      oldParticipant.dispose()
    }
    participants.current.set(number.toString(), participant)

    const video = participant.getVideoElement()

    const options = {
      remoteVideo: video,
      onicecandidate: participant.onIceCandidate.bind(participant)
    }

    participant.rtcPeer = kurentoUtils.WebRtcPeer.WebRtcPeerRecvonly(options, function (error) {
      if (error) {
        return console.error(error)
      }
      participant.generateOffer(participant.offerToReceiveVideo.bind(participant))
    })
  }

  const joinChannel = () => {
    send(`/pub/stream/direct/${userId}`, {
      messageType: 'JOIN_CHANNEL',
      channelId,
      senderId: userId
    })

    joinVoiceChannel({
      channelId: Number(channelId),
      channelName,
      serverName
    })
  }

  const leaveChannel = () => {
    send(`/pub/stream/direct/${userId}`, {
      messageType: 'LEAVE_CHANNEL',
      channelId,
      senderId: userId
    })

    for (const participant of participants.current.values()) {
      participant.dispose()
    }

    leaveVoiceChannel()
  }

  return {
    participants,
    joinChannel,
    leaveChannel
  }
}
