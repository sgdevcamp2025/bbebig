import { useEffect, useState } from 'react'

interface ProfileStatusButtonProps {
  'aria-label': string
  onClick: () => void
  isMuted: boolean
  icon: string
}

const ProfileStatusButton = ({
  onClick,
  'aria-label': ariaLabel,
  isMuted,
  icon
}: ProfileStatusButtonProps) => {
  const [isClient, setIsClient] = useState(false)

  useEffect(() => {
    setIsClient(true)
  }, [])

  const iconSuffix = isClient ? (isMuted ? '-muted' : '') : ''
  const iconPath = `/icon/menu/${icon}${iconSuffix}.svg`

  return (
    <button
      type='button'
      onClick={onClick}
      className='w-8 h-8 flex items-center justify-center rounded-md hover:bg-gray-80'
      aria-label={ariaLabel}>
      {isClient && ( // 클라이언트 사이드에서만 이미지 렌더링
        <img
          src={iconPath}
          className='w-5 h-5'
        />
      )}
    </button>
  )
}

export default ProfileStatusButton
