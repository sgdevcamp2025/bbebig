import { cn } from '@/libs/cn'
import { CustomPresenceStatus } from '@/types/user'
import { cva } from 'class-variance-authority'
import { CSSProperties, memo } from 'react'

type Props = {
  status: CustomPresenceStatus
  size: 'sm' | 'lg'
  defaultBackgroundColor?: CSSProperties['color']
  hover?: boolean
  haveHoverAction?: boolean
  hoverBackgroundColor?: CSSProperties['color']
}

const statusIconSize = cva('rounded-full', {
  variants: {
    size: {
      sm: 'h-[8px] w-[8px]',
      lg: 'h-[18px] w-[18px]'
    }
  }
})

function StatusIcon({
  status,
  size,
  defaultBackgroundColor,
  hoverBackgroundColor,
  hover = false
}: Props) {
  if (status === 'ONLINE') {
    return (
      <div
        className={cn(statusIconSize({ size }), 'bg-green-500')}
        style={{
          backgroundColor: hover ? hoverBackgroundColor : undefined
        }}
      />
    )
  }

  if (status === 'OFFLINE') {
    return (
      <svg
        className={`${statusIconSize({ size })} -scale-x-100 `}
        fill={hover ? hoverBackgroundColor : '#f0b232'}
        viewBox='0 0 24 24'>
        <path d='M12 3a6 6 0 0 0 9 9 9 9 0 1 1-9-9Z' />
      </svg>
    )
  }

  if (status === 'NOT_DISTURB') {
    return (
      <div
        className={cn(statusIconSize({ size }), 'bg-[#f23f43] flex items-center justify-center')}
        style={{
          backgroundColor: hover ? hoverBackgroundColor : undefined
        }}>
        <div
          className={cn(size === 'sm' ? 'h-[2px] w-[6px]' : 'h-[4px] w-[10px]')}
          style={{
            backgroundColor: defaultBackgroundColor
          }}
        />
      </div>
    )
  }

  if (status === 'INVISIBLE') {
    return (
      <div
        className={cn(
          statusIconSize({ size }),
          'border-[#80848e] border-2 rounded-full',
          size === 'sm' ? 'border-2' : 'border-4'
        )}
        style={{
          borderColor: hover ? hoverBackgroundColor : undefined
        }}
      />
    )
  }
}

export default memo(StatusIcon)
