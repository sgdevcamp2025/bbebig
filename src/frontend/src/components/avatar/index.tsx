import { User } from '@/types/user'
import { cva, type VariantProps } from 'class-variance-authority'

type Props = Pick<User, 'avatarUrl' | 'customPresenceStatus'> & VariantProps<typeof avatar>

const avatar = cva('rounded-full', {
  variants: {
    size: {
      sm: 'h-[24px] w-[24px]',
      md: 'h-[40px] w-[40px]',
      lg: 'h-[64px] w-[64px]'
    },
    status: {
      ONLINE: 'bg-green-500',
      OFFLINE: 'bg-gray-500',
      NOT_DISTURB: 'bg-red-500',
      INVISIBLE: 'bg-gray-500'
    }
  },
  defaultVariants: {
    size: 'md',
    status: 'OFFLINE'
  }
})

function Avatar({ avatarUrl, size, customPresenceStatus }: Props) {
  return (
    <div
      aria-label='avatar'
      className={avatar({ size })}>
      <img
        src={avatarUrl}
        alt='avatar'
        className={avatar({ size })}
      />
      <div className='absolute bottom-0 right-0'>
        <div className={avatar({ status: customPresenceStatus })}></div>
      </div>
    </div>
  )
}

export default Avatar
