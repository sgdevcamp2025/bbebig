import { buttonVariants, CustomButtonVariants } from './variants'

interface Props {
  children: React.ReactNode
  variant: keyof typeof CustomButtonVariants
}

function CustomButton({ children, variant }: Props) {
  return <button className={buttonVariants({ variant })}>{children}</button>
}

export default CustomButton
