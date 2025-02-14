import { X } from 'lucide-react'
import { ComponentProps, PropsWithChildren } from 'react'

import { cn } from '@/libs/cn'

import Modal from '../modal'

interface HeaderProps {
  onClose: () => void
}

const Header = ({ onClose, children }: PropsWithChildren<HeaderProps>) => {
  return (
    <div className='w-[440px] pt-6 px-4'>
      <div className='absolute top-6 right-4'>
        <button
          aria-label='닫기'
          type='button'
          onClick={onClose}>
          <X className='w-6 h-6 text-gray-10' />
        </button>
      </div>
      {children}
    </div>
  )
}

const Content = ({ children }: { children: React.ReactNode }) => {
  return <div className='my-4 px-4 rounded-lg overflow-hidden'>{children}</div>
}

const Bottom = ({ children }: { children: React.ReactNode }) => {
  return <div className='p-4 bg-gray-20'>{children}</div>
}

type CustomModalProps = ComponentProps<typeof Modal>

function Layout({ children, ...props }: CustomModalProps) {
  return (
    <Modal
      {...props}
      className={cn('bg-black/50', props.className)}>
      <section className='bg-brand-10 rounded-lg overflow-hidden'>{children}</section>
    </Modal>
  )
}

const CustomModal = Object.assign(Layout, {
  Header,
  Content,
  Bottom
})

export default CustomModal
