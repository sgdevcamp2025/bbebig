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

const avatarSize = cva('rounded-full flex items-center justify-center', {
  variants: {
    size: {
      sm: 'h-[32px] min-w-[32px]',
      md: 'h-[80px] min-w-[80px]',
      lg: 'h-[100px] min-w-[100px]'
    }
  }
})

const imageSize = cva('rounded-full', {
  variants: {
    size: {
      sm: 'h-[32px] w-[32px] min-h-[32px] min-w-[32px]',
      md: 'h-[70px] w-[70px] min-h-[70px] min-w-[70px]',
      lg: 'h-[100px] w-[100px] min-h-[100px] min-w-[100px]'
    }
  }
})

const statusWrapperSize = cva('rounded-full flex items-center justify-center', {
  variants: {
    size: {
      sm: 'h-[12px] w-[12px] min-h-[12px] min-w-[12px]',
      md: 'h-[28px] w-[28px] min-h-[28px] min-w-[28px]',
      lg: 'h-[32px] w-[32px] min-h-[32px] min-w-[32px]'
    }
  }
})

function Avatar({ avatarUrl, size, statusColor = 'black', status, defaultBackgroundColor }: Props) {
  return (
    <div
      aria-label='avatar'
      className={cn(avatarSize({ size }), 'relative')}
      style={{ backgroundColor: defaultBackgroundColor }}>
      <img
        src={avatarUrl || '/image/common/default-avatar.png'}
        alt='avatar'
        className={imageSize({ size })}
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
