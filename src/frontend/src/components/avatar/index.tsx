import { cn } from '@/libs/cn'
import { CustomPresenceStatus } from '@/types/user'
import { cva } from 'class-variance-authority'
import { CSSProperties } from 'react'

type Props = {
  avatarUrl: string
  customPresenceStatus: CustomPresenceStatus
  size: 'sm' | 'lg'
  statusColor: CSSProperties['color']
}

const avatarSize = cva('rounded-full', {
  variants: {
    size: {
      sm: 'h-10 w-10',
      lg: 'h-20 w-20'
    }
  }
})

const statusWrapperSize = cva('rounded-full flex items-center justify-center', {
  variants: {
    size: {
      sm: 'h-[12px] w-[12px]',
      lg: 'h-[28px] w-[28px]'
    }
  }
})

const statusIconSize = cva('rounded-full', {
  variants: {
    size: {
      sm: 'h-[8px] w-[8px]',
      lg: 'h-[18px] w-[18px]'
    }
  }
})

function Avatar({ avatarUrl, size, statusColor = 'black', customPresenceStatus }: Props) {
  return (
    <div
      aria-label='avatar'
      className={cn(avatarSize({ size }), 'relative')}>
      <img
        src={avatarUrl}
        alt='avatar'
        className={avatarSize({ size })}
      />
      <div
        className={cn(statusWrapperSize({ size }), 'absolute bottom-0 right-0')}
        style={{ backgroundColor: statusColor }}>
        {customPresenceStatus === 'ONLINE' && (
          <div className={cn(statusIconSize({ size }), 'bg-green-500')} />
        )}
        {customPresenceStatus === 'OFFLINE' && (
          <svg
            className={`${statusIconSize({ size })} -scale-x-100 `}
            fill='#f0b232'
            viewBox='0 0 24 24'>
            <path d='M12 3a6 6 0 0 0 9 9 9 9 0 1 1-9-9Z' />
          </svg>
        )}
        {customPresenceStatus === 'NOT_DISTURB' && (
          <div
            className={cn(
              statusIconSize({ size }),
              'bg-[#f23f43] flex items-center justify-center'
            )}>
            <div
              className={cn(size === 'sm' ? 'h-[2px] w-[6px]' : 'h-[4px] w-[10px]')}
              style={{ backgroundColor: statusColor ?? 'black' }}
            />
          </div>
        )}
        {customPresenceStatus === 'INVISIBLE' && (
          <div
            className={cn(
              statusIconSize({ size }),
              'border-[#80848e] border-2 rounded-full',
              size === 'sm' ? 'border-2' : 'border-4'
            )}
          />
        )}
      </div>
    </div>
  )
}

export default Avatar
