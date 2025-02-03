import { ComponentPropsWithoutRef } from 'react'

type Props = {
  onClick: () => void
  icon: string
  isMuted: boolean
} & ComponentPropsWithoutRef<'button'>

const ProfileStatusButton = ({ onClick, icon, isMuted, ...props }: Props) => {
  return (
    <button
      type='button'
      onClick={onClick}
      className='w-8 h-8 flex items-center justify-center rounded-md hover:bg-gray-80'
      {...props}>
      <img
        src={`/icon/menu/${icon}${isMuted ? '-muted' : ''}.svg`}
        alt={icon}
        className='w-5 h-5'
      />
    </button>
  )
}

export default ProfileStatusButton
