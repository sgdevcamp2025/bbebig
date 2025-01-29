import { cva, VariantProps } from 'class-variance-authority'
import { ComponentPropsWithoutRef } from 'react'

import { cn } from '@/libs/cn'

type Props = VariantProps<typeof buttonVariants> & ComponentPropsWithoutRef<'button'>

const buttonVariants = cva('', {
  variants: {
    variant: {
      primary: 'bg-brand text-white-100',
      secondary: 'bg-blue-200'
    },
    size: {
      small: 'h-10 leading-[24px] rounded-[3px]',
      medium: 'h-12',
      large: 'h-14'
    },
    width: {
      full: 'w-full',
      half: 'w-1/2'
    }
  }
})

function CustomButton({ children, variant, size, width, className, ...props }: Props) {
  return (
    <button
      className={cn(buttonVariants({ variant, size, width }), className)}
      {...props}>
      {children}
    </button>
  )
}

export default CustomButton
