import { cva, VariantProps } from 'class-variance-authority'
import { ComponentPropsWithoutRef } from 'react'

import { cn } from '@/libs/cn'

type Props = VariantProps<typeof buttonVariants> & ComponentPropsWithoutRef<'button'>

const buttonVariants = cva('', {
  variants: {
    variant: {
      primary: 'bg-brand text-white-100',
      secondary: 'bg-[#248045] text-white-100'
    },
    size: {
      small: 'h-10 text-[14px] leading-[24px] rounded-[3px]',
      medium: 'h-12',
      large: 'h-14'
    },
    width: {
      full: 'w-full',
      half: 'w-1/2'
    }
  },
  defaultVariants: {
    variant: 'primary',
    size: 'small',
    width: 'full'
  }
})

function CustomButton({ children, variant, size, width, className, ...props }: Props) {
  return (
    <button
      className={cn(buttonVariants({ variant, size, width }), className)}
      {...props}>
      <span>{children}</span>
    </button>
  )
}

export default CustomButton
