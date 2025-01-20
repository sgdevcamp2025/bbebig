import { cva } from 'class-variance-authority'

import { CustomButtonVariants } from './variants'

interface Props {
  children: React.ReactNode
  variant: keyof typeof CustomButtonVariants
}

const buttonVariants = cva('', {
  variants: {
    variant: {
      [CustomButtonVariants.primary]: 'bg-red-200',
      [CustomButtonVariants.secondary]: 'bg-blue-200'
    }
  }
})

function CustomButton({ children, variant }: Props) {
  return <button className={buttonVariants({ variant })}>{children}</button>
}

export default CustomButton
