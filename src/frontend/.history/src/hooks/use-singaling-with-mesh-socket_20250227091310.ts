import { useEffect, useRef, useState } from 'react'
import io from 'socket.io-client'

import { SIGNALING_NODE_SERVER_URL, TURN_SERVER_URL } from '@/constants/env'
import useUserStatus from '@/stores/use-user-status'

const socket = io(SIGNALING_NODE_SERVER_URL)
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
  iceCandidatePoolSize: 10
} as RTCConfiguration

export const useSignalingWithMeshSocket = ({
  myUserId,
  channelId,
  channelName,
  serverName
}: {
  myUserId: string
  channelId: string
  channelName: string
  serverName: string
}) => {
  const myFaceRef = useRef<HTMLVideoElement | null>(null) // 내 비디오 엘리먼트
  const callRef = useRef<HTMLDivElement | null>(null) // 상대방(원격) 비디오 담을 컨테이너
  const myStreamRef = useRef<MediaStream | null>(null) // 내 로컬 미디어 스트림
  const [cameras, setCameras] = useState<MediaDeviceInfo[]>([]) // 카메라 목록
  const peersRef = useRef<Record<string, RTCPeerConnection>>({}) // socketId -> RTCPeerConnection
  const userMapRef = useRef<Record<string, string>>({}) // socketId -> userId (상대방 표시용)
  const { getCurrentChannelInfo, joinVoiceChannel, leaveVoiceChannel } = useUserStatus()

  const joined = (getCurrentChannelInfo()?.channelId ?? 0) === Number(channelId)

  // -----------------------------
  // (1) 미디어 스트림 획득
  // -----------------------------
  async function getMedia(deviceId) {
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
        await getCameras()
      }
    } catch (err) {
      console.error('getMedia error:', err)
    }
  }
  // -----------------------------
  // (2) 카메라 목록
  // -----------------------------
  async function getCameras() {
    try {
      const devices = await navigator.mediaDevices.enumerateDevices()
      const videoInputs = devices.filter((d) => d.kind === 'videoinput')
      setCameras(videoInputs)
    } catch (err) {
      console.error('getCameras error:', err)
    }
  }
  // -----------------------------
  // (3) user_left 처리
  // -----------------------------
  useEffect(() => {
    const handleUserLeft = (socketId) => {
      // 상대방 비디오 태그 제거
      const videoContainer = document.getElementById(`container_${socketId}`)
      if (videoContainer && callRef.current) {
        callRef.current.removeChild(videoContainer)
      }
      // PeerConnection 종료
      if (peersRef.current[socketId]) {
        peersRef.current[socketId].close()
        delete peersRef.current[socketId]
      }
      // userMapRef에서도 제거
      if (userMapRef.current[socketId]) {
        delete userMapRef.current[socketId]
      }
    }
    socket.on('user_left', handleUserLeft)
    return () => {
      socket.off('user_left', handleUserLeft)
    }
  }, [])
  // -----------------------------
  // (4) 소켓 이벤트: welcome/offer/answer/ice
  // -----------------------------
  useEffect(() => {
    // 새 유저가 들어옴 (내가 먼저 방에 있었을 때)
    const handleWelcome = async (newSocketId, newUserId) => {
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
    const handleOffer = async (offer, remoteId, remoteUserId) => {
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
    const handleAnswer = (answer, remoteId, remoteUserId) => {
      // userMapRef에 저장
      userMapRef.current[remoteId] = remoteUserId
      const pc = peersRef.current[remoteId]
      if (pc) {
        pc.setRemoteDescription(answer)
      }
    }
    // ICE candidate 수신
    const handleIce = (ice, remoteId, remoteUserId) => {
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
  }, [])
  // -----------------------------
  // (5) 방 입장
  // -----------------------------
  useEffect(() => {
    if (joined && roomId && userId) {
      // 로컬 미디어 가져오기
      getMedia()
      // 서버에 join_room
      socket.emit('join_room', { roomId, userId })
    }
  }, [joined, roomId, userId])
  // -----------------------------
  // (6) PeerConnection 생성
  // -----------------------------
  function makeConnection(socketId) {
    if (peersRef.current[socketId]) return
    const pc = new RTCPeerConnection({
      iceServers: [{ urls: ['stun:stun.l.google.com:19302'] }]
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
      const remoteUserId = userMapRef.current[socketId] || 'Unknown'
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
      let peerVideo = document.getElementById(socketId)
      if (!peerVideo) {
        peerVideo = document.createElement('video')
        peerVideo.id = socketId
        peerVideo.autoplay = true
        peerVideo.playsInline = true
        peerVideo.width = 400
        peerVideo.height = 300
      }
      peerVideo.srcObject = event.streams[0]
      // 아래에 userId 라벨 표시
      let label = document.getElementById(`label_${socketId}`)
      if (!label) {
        label = document.createElement('div')
        label.id = `label_${socketId}`
        label.style.color = 'blue'
        label.style.fontWeight = 'bold'
      }
      label.innerText = `User ID: ${remoteUserId}`
      videoContainer.appendChild(peerVideo)
      videoContainer.appendChild(label)
      // callRef에 최종 삽입
      if (callRef.current && !document.getElementById(containerId)) {
        callRef.current.appendChild(videoContainer)
      }
    })
    // 로컬 스트림 트랙을 PeerConnection에 추가
    if (myStreamRef.current) {
      myStreamRef.current.getTracks().forEach((track) => {
        pc.addTrack(track, myStreamRef.current)
      })
    }
    peersRef.current[socketId] = pc
  }
  // -----------------------------
  // (7) 음소거 / 카메라 Off
  // -----------------------------
  function handleMuteClick() {
    if (!myStreamRef.current) return
    myStreamRef.current.getAudioTracks().forEach((track) => (track.enabled = !track.enabled))
    setMuted((prev) => !prev)
  }
  function handleCameraClick() {
    if (!myStreamRef.current) return
    myStreamRef.current.getVideoTracks().forEach((track) => (track.enabled = !track.enabled))
    setCameraOff((prev) => !prev)
  }
  // -----------------------------
  // (8) 카메라 변경
  // -----------------------------
  async function handleCameraChange(e) {
    await getMedia(e.target.value)
  }
  // -----------------------------
  // (9) 방 입장 / 퇴장
  // -----------------------------
  function handleSubmit() {
    joinVoiceChannel({
      channelId: Number(channelId),
      channelName: channelName,
      serverName: serverName
    })

    socket.emit('join_room', { roomId: channelId, userId: myUserId })
  }
  function handleLeaveClick() {
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
    // setJoined(false)
    leaveVoiceChannel()
  }
  // -----------------------------
  // (10) 페이지 떠날 때 소켓 해제
  // -----------------------------
  useEffect(() => {
    const onBeforeUnload = () => {
      socket.disconnect()
    }
    window.addEventListener('beforeunload', onBeforeUnload)
    return () => {
      window.removeEventListener('beforeunload', onBeforeUnload)
    }
  }, [])

  return { handleSubmit, handleLeaveClick, myFaceRef, callRef }
}
