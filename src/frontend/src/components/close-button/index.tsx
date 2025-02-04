import { cn } from '@/libs/cn'
import { X } from 'lucide-react'
import { ComponentProps } from 'react'

type Props = ComponentProps<'button'> & {
  onClick?: () => void
}

function CloseButton({ className, onClick, ...props }: Props) {
  return (
    <button
      onClick={onClick}
      className={cn('flex flex-col items-center gap-2 group', className)}
      {...props}>
      <div
        aria-label='닫기'
        className='w-9 h-9 rounded-full flex items-center justify-center border-gray-10 group-hover:border-white-100 border-2 transition-all duration-300'>
        <X className='w-[18px] h-[18px] text-gray-10 group-hover:text-white-100 transition-all duration-300' />
      </div>
      <div className='text-[13px] font-medium group-hover:text-white-100 transition-all duration-300'>
        ESC
      </div>
    </button>
  )
}

export default CloseButton
