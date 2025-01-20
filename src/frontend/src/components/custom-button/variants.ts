import { cva } from 'class-variance-authority'

export const CustomButtonVariants = {
  primary: 'primary',
  secondary: 'secondary'
} as const

export const buttonVariants = cva('', {
  variants: {
    variant: {
      [CustomButtonVariants.primary]: 'bg-red-200',
      [CustomButtonVariants.secondary]: 'bg-blue-200'
    }
  }
})
