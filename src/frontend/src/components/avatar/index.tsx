import { cn } from '@/libs/cn'
import { cva } from 'class-variance-authority'
import { CSSProperties, ComponentProps } from 'react'

import StatusIcon from '../status-icon'
import { CustomPresenceStatus } from '@/types/user'

type Props = {
  avatarUrl: string
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

function Avatar({ avatarUrl, size, statusColor = 'black', status }: Props) {
  return (
    <div
      aria-label='avatar'
      className={cn(avatarSize({ size }), 'relative')}>
      <img
        src={avatarUrl}
        alt='avatar'
        className={avatarSize({ size })}
      />
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
