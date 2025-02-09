import { useLayoutEffect, useState } from 'react'

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

  useLayoutEffect(() => {
    setIsClient(true)
  }, [])

  if (!isClient) return null

  const iconSuffix = isMuted ? '-muted' : ''
  const iconPath = `/icon/menu/${icon}${iconSuffix}.svg`

  return (
    <button
      type='button'
      onClick={onClick}
      className='w-8 h-8 flex items-center justify-center rounded-md hover:bg-gray-80'
      aria-label={ariaLabel}>
      <img
        src={iconPath}
        className='w-5 h-5'
      />
    </button>
  )
}

export default ProfileStatusButton
