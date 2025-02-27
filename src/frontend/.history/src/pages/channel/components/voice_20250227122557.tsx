import { useEffect, useRef, useState } from 'react'
import { io } from 'socket.io-client'

import ChatArea from '@/components/chat-area'
import CustomButton from '@/components/custom-button'
import useGetSelfUser from '@/hooks/queries/user/useGetSelfUser'
import { cn } from '@/libs/cn'
import useUserStatus from '@/stores/use-user-status'
import { ChatUser } from '@/types/user'

interface Props {
  channelId: number
  channelName: string
  serverName: string
  serverMemberList: ChatUser[]
  currentUser: ChatUser
  targetUser: ChatUser[]
}

const socket = io('ws://localhost:9000')

function VideoComponent({
  channelId,
  serverName,
  channelName,
  currentUser,
  targetUser,
  serverMemberList
}: Props) {
  const [sideBar, setSideBar] = useState(false)
  const selfUser = useGetSelfUser()
  const { getCurrentChannelInfo, joinVoiceChannel, leaveVoiceChannel } = useUserStatus()

  // 레퍼런스
  const myFaceRef = useRef<HTMLVideoElement | null>(null) // 내 비디오 엘리먼트
  const callRef = useRef<HTMLDivElement | null>(null) // 상대방(원격) 비디오 담을 컨테이너
  const myStreamRef = useRef<MediaStream | null>(null) // 내 로컬 미디어 스트림
  const peersRef = useRef<Record<string, RTCPeerConnection>>({}) // socketId -> RTCPeerConnection
  const userMapRef = useRef<Record<string, string>>({}) // socketId -> userId (상대방 표시용)

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
      // setCameras(videoInputs)
    } catch (err) {
      console.error('getCameras error:', err)
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
  }, [])

  // -----------------------------
  // (5) 방 입장
  // -----------------------------
  useEffect(() => {
    if (getCurrentChannelInfo()?.channelId === channelId) {
      // 로컬 미디어 가져오기
      getMedia()
      // 서버에 join_room
    }
  }, [getCurrentChannelInfo])

  // -----------------------------
  // (6) PeerConnection 생성
  // -----------------------------
  function makeConnection(socketId: string) {
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

      let peerVideo = document.getElementById(socketId) as HTMLVideoElement
      if (!peerVideo) {
        peerVideo = document.createElement('video')
        peerVideo.id = socketId
        peerVideo.muted = selfUser.id === Number(remoteUserId)
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

  // -----------------------------
  // (7) 음소거 / 카메라 Off
  // -----------------------------
  // function handleMuteClick() {
  //   if (!myStreamRef.current) return
  //   myStreamRef.current.getAudioTracks().forEach((track) => (track.enabled = !track.enabled))
  //   setMuted((prev) => !prev)
  // }

  // function handleCameraClick() {
  //   if (!myStreamRef.current) return
  //   myStreamRef.current.getVideoTracks().forEach((track) => (track.enabled = !track.enabled))
  //   setCameraOff((prev) => !prev)
  // }

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
    socket.connect()

    getMedia()

    joinVoiceChannel({
      channelId,
      channelName,
      serverName
    })

    socket.emit('join_room', { roomName: channelId, userId: selfUser.id })
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

  const handleJoinVoiceChannel = () => {
    handleSubmit()
  }

  const handleLeaveVoiceChannel = () => {
    handleLeaveClick()
  }

  const isInVoiceChannel = getCurrentChannelInfo()?.channelId === channelId

  const voiceUsers = serverMemberList.map((user) => {
    return { ...user }
  })

  return (
    <div className='flex flex-1 h-screen'>
      <div
        className={cn('flex w-full group bg-black', {
          'rounded-r-lg mr-2': sideBar
        })}>
        <div className='w-full h-full flex flex-col'>
          <section
            className={cn(
              'opacity-0 h-12 px-4 w-full flex items-center justify-between transition-all duration-300 group-hover:opacity-100 group-hover:translate-y-0 group-hover:blur-none'
            )}>
            <div className='flex items-center gap-2'>
              <img
                src={`/icon/channel/type-voice.svg`}
                className='w-6 h-6'
              />
              <span className='text-white-10 text-md font-bold'>{channelName}</span>
            </div>
            <button
              type='button'
              onClick={() => setSideBar(true)}>
              <img
                src='/icon/channel/threads.svg'
                className='w-6 h-6'
                alt='스레드'
              />
            </button>
          </section>
          <div />
          <section className='flex-1 flex flex-col items-center justify-center gap-2'>
            {!isInVoiceChannel && (
              <>
                <h4 className='text-gray-10 text-3xl font-bold'>{channelName}</h4>
                <span className='text-gray-90 text-sm font-semibold'>
                  {voiceUsers.length === 0
                    ? '현재 채널에 아무도 없어요'
                    : `${voiceUsers.map((user) => user.nickName).join(', ')} 님이 현재 음성 채널에 있어요.`}
                </span>
                <CustomButton
                  className='w-fit px-4 py-2 mt-5 mb-10'
                  variant='secondary'
                  onClick={handleJoinVoiceChannel}>
                  음성 채널 참가하기
                </CustomButton>
              </>
            )}
            {isInVoiceChannel && (
              <>
                <div
                  ref={callRef}
                  style={{
                    display: 'flex',
                    flexWrap: 'wrap',
                    gap: '10px',
                    marginTop: '20px'
                  }}>
                  <div className='m-[10px] relative top-[-48px]'>
                    <video
                      className='w-[400px] h-[300px]'
                      ref={myFaceRef}
                      autoPlay
                      playsInline
                    />
                  </div>
                </div>
                <div
                  className={cn(
                    'absolute bottom-4 opacity-0 flex items-center justify-center gap-4 transition-all duration-300 group-hover:opacity-100 group-hover:translate-y-0 group-hover:blur-none'
                  )}>
                  <button
                    type='button'
                    onClick={() => {
                      console.log(voiceUsers, 'voiceUsers')
                    }}
                    className='w-14 h-14 rounded-full bg-[#282d31] flex items-center justify-center'>
                    <img
                      alt='음성 채널 소리 끄기'
                      src='/icon/channel/microphone-muted.svg'
                      className='w-6 h-6'
                    />
                  </button>
                  <button
                    type='button'
                    onClick={handleLeaveVoiceChannel}
                    className='w-14 h-14 rounded-full bg-red-100 flex items-center justify-center'>
                    <img
                      alt='음성 채널 나가기'
                      src='/icon/channel/voice.svg'
                      className='w-6 h-6'
                    />
                  </button>
                </div>
              </>
            )}
          </section>
        </div>
      </div>
      {sideBar && (
        <div className='flex flex-col min-w-[480px] h-screen bg-brand-10 rounded-l-lg'>
          <ChatArea
            isVoice={true}
            chatKey={channelId}
            users={{
              currentUser: currentUser,
              targetUsers: targetUser
            }}
            channelName={channelName}
            onClose={() => setSideBar(false)}
          />
        </div>
      )}
    </div>
  )
}

export default VideoComponent
