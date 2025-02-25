import { cva } from 'class-variance-authority'
import { ComponentProps } from 'react'

import { cn } from '@/libs/cn'

type Props = ComponentProps<'div'> & {
  variant?: 'rounded' | 'circle' | 'square'
}

const SkeletonVariant = cva('bg-discord-gray-800 animate-pulse', {
  variants: {
    variant: {
      rounded: 'rounded-md',
      circle: 'rounded-full',
      square: 'rounded-none'
    }
  }
})

export function Skeleton({ variant = 'rounded', ...props }: Props) {
  return <div className={cn(SkeletonVariant({ variant }), props.className)} />
}
