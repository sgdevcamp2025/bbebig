import { cva } from 'class-variance-authority'
import { useEffect, useRef } from 'react'

import AvatarCard from '@/components/avatar-card'
import { WebRTCUser } from '@/hooks/store/use-signaling-with-sfu'
import { cn } from '@/libs/cn'
import { CustomPresenceStatus } from '@/types/user'

interface Props {
  size: 'sm' | 'md' | 'lg'
  id: string
  user: WebRTCUser
  memberId?: number
  nickName?: string
  avatarUrl?: string
  bannerUrl?: string
  globalStatus?: CustomPresenceStatus
}

const cardSize = cva(
  'relative flex items-center justify-center rounded-md overflow-hidden rounded-[8px]',
  {
    variants: {
      size: {
        sm: 'w-[160px] h-[90px]',
        md: 'w-[628px] h-[346px]',
        lg: 'w-[1000px] h-[562px]'
      }
    }
  }
)

function VideoCard({ size, user, nickName, avatarUrl, bannerUrl }: Props) {
  const videoRef = useRef<HTMLVideoElement>(null)
  const isMounted = useRef(true)

  useEffect(() => {
    const videoElement = videoRef.current
    if (!videoElement || !user.stream) return

    videoElement.srcObject = user.stream

    // 안정적인 재생 처리
    const playVideo = async () => {
      try {
        await videoElement.play()
        if (isMounted.current) {
          // 성공적으로 재생 후 필요한 상태 업데이트
        }
      } catch (err) {
        console.error('비디오 재생 오류:', err)
      }
    }

    // 미디어 준비 상태 확인
    if (videoElement.readyState >= HTMLMediaElement.HAVE_METADATA) {
      playVideo()
    } else {
      videoElement.onloadedmetadata = playVideo
    }

    return () => {
      videoElement.onloadedmetadata = null
      videoElement.srcObject = null
    }
  }, [user.stream])

  return (
    <li className={cn(cardSize({ size }))}>
      {user.stream ? (
        <video
          ref={videoRef}
          className='w-full h-full object-cover'
          autoPlay
          playsInline
          muted={user.audioEnabled}
        />
      ) : (
        <AvatarCard
          name={nickName ?? ''}
          avatarUrl={avatarUrl ?? '/image/common/default-avatar.png'}
          backgroundUrl={bannerUrl ?? '/image/common/default-background.png'}
          size={size}
          micStatus={true}
          headphoneStatus={true}
        />
      )}
    </li>
  )
}

export default VideoCard
