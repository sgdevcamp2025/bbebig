import { cva } from 'class-variance-authority'
import { ComponentProps, CSSProperties } from 'react'

import { cn } from '@/libs/cn'
import { CustomPresenceStatus } from '@/types/user'

import StatusIcon from '../status-icon'

type Props = {
  name: string
  avatarUrl: string | null
  statusColor: CSSProperties['color']
  status?: CustomPresenceStatus
} & Omit<ComponentProps<typeof StatusIcon>, 'status'>

const avatarSize = cva('rounded-full', {
  variants: {
    size: {
      sm: 'h-8 w-8',
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

function Avatar({ name, avatarUrl, size, statusColor = 'black', status }: Props) {
  return (
    <div
      aria-label='avatar'
      className={cn(avatarSize({ size }), 'relative')}>
      {avatarUrl ? (
        <img
          src={avatarUrl}
          alt='avatar'
          className={avatarSize({ size })}
        />
      ) : (
        <div className={avatarSize({ size })}>{name.slice(0, 2)}</div>
      )}
      {status ? (
        <div
          className={cn(statusWrapperSize({ size }), 'absolute bottom-0 right-0')}
          style={{ backgroundColor: statusColor }}>
          <StatusIcon
            status={status}
            size={size}
            defaultBackgroundColor={statusColor}
          />
        </div>
      ) : null}
    </div>
  )
}

export default Avatar
