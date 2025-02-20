import { useCallback, useEffect, useRef, useState } from 'react'

import { useSignalingStompStore } from '@/stores/use-signaling-stomp-store'

import useMediaControl from '../use-media-control'

const PC_CONFIG = {
  iceServers: [{ urls: 'stun:stun.l.google.com:19302' }]
}

interface WebRTCUser {
  id: string
  stream: MediaStream
}

function useSignalingWithSFU(roomId: string) {
  const { startStream, getStream } = useMediaControl()
  const { send, subscribe, unsubscribe } = useSignalingStompStore()

  const senderPcRef = useRef<RTCPeerConnection | null>(null)
  const recivePcRef = useRef<Record<string, RTCPeerConnection>>({})
  const [_pc, setPc] = useState<RTCPeerConnection | null>(null)
  const [users, setUsers] = useState<WebRTCUser[]>([])

  useEffect(
    function init() {
      joinChannel(roomId)

      // 채널 참여 정보를 얻기 위한 구독
      subscribe('userJoin', handleUserJoin)
      subscribe('allUsers', handleAllUsers)
      subscribe('userLeave', handleUserLeave)

      // stream 전송을 위한 구독
      subscribe('/sub/stream', handleStream)

      subscribe('receiverOffer', handleReceiverOffer)
      subscribe('receiverAnswer', handleReceiverAnswer)
      subscribe('receiverIceCandidate', handleReceiverIceCandidate)

      return function cleanup() {
        unsubscribe('userJoin')
        unsubscribe('allUsers')
        unsubscribe('userLeave')

        unsubscribe('/sub/stream')

        unsubscribe('receiverOffer')
        unsubscribe('receiverAnswer')
        unsubscribe('receiverIceCandidate')
      }
    },
    [handleAllUsers, handleUserJoin, handleUserLeave, joinChannel, roomId, subscribe, unsubscribe]
  )

  async function joinChannel(roomId: string) {
    try {
      await createSenderPeerConnection()
      await createSenderOffer()

      send('joinChannel', { roomId })
    } catch (err) {
      console.error(err)
    }
  }

  const createSenderPeerConnection = useCallback(async () => {
    const peerConnection = new RTCPeerConnection(PC_CONFIG)
    setPc(peerConnection)
    await startStream()
    const stream = await getStream()

    stream.getTracks().forEach((track) => {
      peerConnection.addTrack(track, stream)
    })

    peerConnection.onicecandidate = (event) => {
      send('iceCandidate', { candidate: event.candidate })
    }

    peerConnection.onnegotiationneeded = async () => {
      console.log('negotiationneeded')
    }

    senderPcRef.current = peerConnection
  }, [send, getStream, startStream])

  const createReceiverPeerConnection = useCallback(
    async (sessionId: string) => {
      try {
        const pc = new RTCPeerConnection(PC_CONFIG)
        recivePcRef.current = { ...recivePcRef.current, [sessionId]: pc }

        pc.onicecandidate = (event) => {
          send('iceCandidate', { candidate: event.candidate })
        }

        pc.onnegotiationneeded = async (e) => {
          console.log('negotiationneeded', e)
        }

        pc.ontrack = (event) => {
          setUsers((oldUsers) =>
            oldUsers
              .filter((user) => user.id !== sessionId)
              .concat({
                id: sessionId,
                stream: event.streams[0]
              })
          )
        }

        return pc
      } catch (err) {
        console.error(err)
        return undefined
      }
    },
    [send]
  )

  const createReceiverOffer = useCallback(
    async (peerConnection: RTCPeerConnection, roomId: string) => {
      try {
        const offer = await peerConnection.createOffer({
          offerToReceiveAudio: true,
          offerToReceiveVideo: true
        })

        await peerConnection.setLocalDescription(new RTCSessionDescription(offer))

        send('receiverOffer', {
          sdp: offer,
          roomId: roomId
        })
      } catch (error) {
        console.error('Receiver offer 생성 실패:', error)
      }
    },
    [send]
  )

  const createReciverPeerConnection = useCallback(
    async (roomId: string) => {
      try {
        const peerConnection = await createReceiverPeerConnection(roomId)
        if (!peerConnection) return

        await createReceiverOffer(peerConnection, roomId)
      } catch (err) {
        console.error(err)
      }
    },
    [createReceiverOffer, createReceiverPeerConnection]
  )

  const createSenderOffer = useCallback(async () => {
    const offer = await senderPcRef.current?.createOffer()
    send('senderOffer', { offer })
  }, [send])

  const closeReciverPeerConnection = useCallback((userId: string) => {
    if (!recivePcRef.current?.[userId]) return
    recivePcRef.current[userId].close()
    delete recivePcRef.current[userId]
  }, [])

  const handleStream = useCallback(
    (req: { type: 'answer' | 'offer' | 'iceCandidate'; data: any }) => {
      if (req.type === 'answer') {
        handleSenderAnswer(req.data)
      } else if (req.type === 'offer') {
        handleSenderOffer(req.data)
      } else if (req.type === 'iceCandidate') {
        handleSenderIceCandidate(req.data)
      }
    },
    []
  )

  function handleUserJoin(data: { userId: string }) {
    createReciverPeerConnection(data.userId)
  }

  function handleAllUsers(
    data: {
      userId: string
    }[]
  ) {
    data.forEach((user) => {
      createReciverPeerConnection(user.userId)
    })
  }

  function handleUserLeave(data: { userId: string }) {
    closeReciverPeerConnection(data.userId)
    setUsers((prev) => prev.filter((user) => user.id !== data.userId))
  }

  function handleSenderOffer(data: { userId: string; offer: RTCSessionDescriptionInit }) {
    console.log(data)
  }

  async function handleSenderAnswer(data: { id: string; sdp: RTCSessionDescription }) {
    try {
      const senderPc = senderPcRef.current
      if (!senderPc) return
      await senderPc.setRemoteDescription(new RTCSessionDescription(data.sdp))
    } catch (err) {
      console.error(err)
    }
  }

  async function handleSenderIceCandidate(data: { id: string; candidate: RTCIceCandidate }) {
    try {
      if (!data.candidate) return
      await senderPcRef.current?.addIceCandidate(new RTCIceCandidate(data.candidate))
      console.log('cadidate add success')
    } catch (err) {
      console.error(err)
    }
  }

  async function handleReceiverIceCandidate(data: { id: string; candidate: RTCIceCandidate }) {
    try {
      if (!data.candidate) return
      await recivePcRef.current[data.id]?.addIceCandidate(new RTCIceCandidate(data.candidate))
    } catch (err) {
      console.error(err)
    }
  }

  async function handleReceiverOffer(data: { id: string; offer: RTCSessionDescription }) {
    try {
      if (!data.offer) return
      await recivePcRef.current[data.id]?.setRemoteDescription(
        new RTCSessionDescription(data.offer)
      )
    } catch (err) {
      console.error(err)
    }
  }

  async function handleReceiverAnswer(data: { id: string; sdp: RTCSessionDescription }) {
    try {
      if (!data.sdp) return
      await recivePcRef.current[data.id]?.setRemoteDescription(new RTCSessionDescription(data.sdp))
    } catch (err) {
      console.error(err)
    }
  }

  useEffect(
    function init() {
      joinChannel(roomId)

      // 채널 참여 정보를 얻기 위한 구독
      subscribe('userJoin', handleUserJoin)
      subscribe('allUsers', handleAllUsers)
      subscribe('userLeave', handleUserLeave)

      // stream 전송을 위한 구독
      subscribe('/sub/stream', handleStream)

      subscribe('receiverOffer', handleReceiverOffer)
      subscribe('receiverAnswer', handleReceiverAnswer)
      subscribe('receiverIceCandidate', handleReceiverIceCandidate)

      return function cleanup() {
        unsubscribe('userJoin')
        unsubscribe('allUsers')
        unsubscribe('userLeave')

        unsubscribe('/sub/stream')

        unsubscribe('receiverOffer')
        unsubscribe('receiverAnswer')
        unsubscribe('receiverIceCandidate')
      }
    },
    [handleAllUsers, handleUserJoin, handleUserLeave, joinChannel, roomId, subscribe, unsubscribe]
  )

  return {
    joinChannel,
    users
  }
}

export default useSignalingWithSFU
