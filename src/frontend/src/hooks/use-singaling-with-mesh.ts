import { useEffect, useRef } from 'react'
import io, { Socket } from 'socket.io-client'

import { SIGNALING_NODE_SERVER_URL, TURN_SERVER_URL } from '@/constants/env'
import useUserStatus from '@/stores/use-user-status'

import useGetSelfUser from './queries/user/useGetSelfUser'

export function useSingalingWithMeshSocket(
  channelId: number,
  channelName: string,
  serverName: string
) {
  const { getCurrentChannelInfo, joinVoiceChannel, leaveVoiceChannel } = useUserStatus()
  const selfUser = useGetSelfUser()

  // 레퍼런스
  const myFaceRef = useRef<HTMLVideoElement | null>(null) // 내 비디오 엘리먼트
  const callRef = useRef<HTMLDivElement | null>(null) // 상대방(원격) 비디오 담을 컨테이너
  const myStreamRef = useRef<MediaStream | null>(null) // 내 로컬 미디어 스트림
  const peersRef = useRef<Record<string, RTCPeerConnection>>({}) // socketId -> RTCPeerConnection
  const userMapRef = useRef<Record<string, string>>({}) // socketId -> userId (상대방 표시용)
  const socket = useRef<Socket>(
    io(SIGNALING_NODE_SERVER_URL, {
      // path: '/socket.io',
      // transports: ['websocket'],
      withCredentials: true
    })
  ).current

  // -----------------------------
  // (1) 미디어 스트림 획득
  // -----------------------------
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

  // -----------------------------
  // (3) user_left 처리
  // -----------------------------
  useEffect(() => {
    const handleUserLeft = (socketId: string) => {
      // 상대방 비디오 태그 제거
      const videoContainer = document.getElementById(`container_${socketId}`)
      if (videoContainer && callRef.current) {
        callRef.current.removeChild(videoContainer)
      }
      // PeerConnection 종료
      if (peersRef.current[socketId]) {
        peersRef.current[socketId].close()
        // eslint-disable-next-line @typescript-eslint/no-dynamic-delete
        delete peersRef.current[socketId]
      }
      // userMapRef에서도 제거
      if (userMapRef.current[socketId]) {
        // eslint-disable-next-line @typescript-eslint/no-dynamic-delete
        delete userMapRef.current[socketId]
      }
    }

    socket.on('user_left', handleUserLeft)

    return () => {
      socket.off('user_left', handleUserLeft)
    }
  }, [socket])

  // -----------------------------
  // (4) 소켓 이벤트: welcome/offer/answer/ice
  // -----------------------------
  useEffect(() => {
    // -----------------------------
    // (6) PeerConnection 생성
    // -----------------------------
    function makeConnection(socketId: string) {
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
        ]
      })

      // ICE candidate
      pc.addEventListener('icecandidate', (event) => {
        if (event.candidate) {
          socket.emit('ice', event.candidate, socketId)
        }
      })

      // track 이벤트 (상대방 비디오)
      pc.addEventListener('track', (event) => {
        // userId 라벨 표시를 위해 userMapRef 조회
        const containerId = `container_${socketId}`
        let videoContainer = document.getElementById(containerId)

        // 비디오+라벨 묶음을 담을 div
        if (!videoContainer) {
          videoContainer = document.createElement('div')
          videoContainer.id = containerId
          videoContainer.style.display = 'flex'
          videoContainer.style.flexDirection = 'column'
          videoContainer.style.alignItems = 'center'
          videoContainer.style.margin = '10px'
        }

        let peerVideo = document.getElementById(socketId) as HTMLVideoElement
        if (!peerVideo) {
          peerVideo = document.createElement('video')
          peerVideo.id = socketId
          peerVideo.autoplay = true
          peerVideo.playsInline = true
          peerVideo.width = 400
          peerVideo.height = 300
        }
        peerVideo.srcObject = event.streams[0]

        videoContainer.appendChild(peerVideo)

        // callRef에 최종 삽입
        if (callRef.current && !document.getElementById(containerId)) {
          callRef.current.appendChild(videoContainer)
        }
      })

      // 로컬 스트림 트랙을 PeerConnection에 추가
      if (myStreamRef.current) {
        myStreamRef.current.getTracks().forEach((track) => {
          if (myStreamRef.current) pc.addTrack(track, myStreamRef.current)
        })
      }

      peersRef.current[socketId] = pc
    }

    // 새 유저가 들어옴 (내가 먼저 방에 있었을 때)
    const handleWelcome = async (newSocketId: string, newUserId: string) => {
      // userMapRef에 저장 (표시용)
      userMapRef.current[newSocketId] = newUserId

      // PeerConnection 생성
      makeConnection(newSocketId)

      // Offer 생성
      const offer = await peersRef.current[newSocketId].createOffer()
      await peersRef.current[newSocketId].setLocalDescription(offer)

      // 서버에 offer 전송
      socket.emit('offer', offer, newSocketId)
    }

    // offer 수신 (내가 나중에 들어왔을 때)
    const handleOffer = async (
      offer: RTCSessionDescriptionInit,
      remoteId: string,
      remoteUserId: string
    ) => {
      // userMapRef에 저장
      userMapRef.current[remoteId] = remoteUserId

      if (!myStreamRef.current) {
        await getMedia()
      }
      makeConnection(remoteId)

      await peersRef.current[remoteId].setRemoteDescription(offer)

      // Answer 생성
      const answer = await peersRef.current[remoteId].createAnswer()
      await peersRef.current[remoteId].setLocalDescription(answer)

      socket.emit('answer', answer, remoteId)
    }

    // answer 수신
    const handleAnswer = (
      answer: RTCSessionDescriptionInit,
      remoteId: string,
      remoteUserId: string
    ) => {
      // userMapRef에 저장
      userMapRef.current[remoteId] = remoteUserId

      const pc = peersRef.current[remoteId]
      if (pc) {
        pc.setRemoteDescription(answer)
      }
    }

    // ICE candidate 수신
    const handleIce = (ice: RTCIceCandidate, remoteId: string, remoteUserId: string) => {
      // userMapRef에 저장 (중복이어도 문제 없음)
      userMapRef.current[remoteId] = remoteUserId

      const pc = peersRef.current[remoteId]
      if (pc) {
        pc.addIceCandidate(ice)
      }
    }

    socket.on('welcome', handleWelcome)
    socket.on('offer', handleOffer)
    socket.on('answer', handleAnswer)
    socket.on('ice', handleIce)

    return () => {
      socket.off('welcome', handleWelcome)
      socket.off('offer', handleOffer)
      socket.off('answer', handleAnswer)
      socket.off('ice', handleIce)
    }
  }, [socket])

  // -----------------------------
  // (5) 방 입장
  // -----------------------------
  useEffect(() => {
    if (getCurrentChannelInfo()?.channelId === channelId) {
      // 로컬 미디어 가져오기
      getMedia()
      // 서버에 join_room
    }
  }, [channelId, getCurrentChannelInfo])

  function joinChannel() {
    socket.connect()

    getMedia()

    joinVoiceChannel({
      channelId,
      channelName,
      serverName
    })

    socket.emit('join_room', { roomName: channelId, userId: selfUser.id })
  }

  function leaveChannel() {
    // 로컬 스트림 정지
    if (myStreamRef.current) {
      myStreamRef.current.getTracks().forEach((track) => track.stop())
      myStreamRef.current = null
    }
    // 소켓 해제
    socket.disconnect()

    // 내 비디오 제거
    if (myFaceRef.current) {
      myFaceRef.current.srcObject = null
    }

    // 원격 PeerConnection 종료
    Object.values(peersRef.current).forEach((pc) => pc.close())
    peersRef.current = {}
    userMapRef.current = {}

    leaveVoiceChannel()
  }

  // -----------------------------
  // (10) 페이지 떠날 때 소켓 해제
  // -----------------------------
  useEffect(() => {
    return function cleanup() {
      socket.disconnect()
    }
  }, [socket])

  const isInVoiceChannel = getCurrentChannelInfo()?.channelId === channelId

  return {
    callRef,
    myFaceRef,
    joinChannel,
    leaveChannel,
    isInVoiceChannel
  }
}
