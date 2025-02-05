import { useEffect, type PropsWithChildren, type CSSProperties } from 'react'
import Portal from '../portal'
import { cn } from '@/libs/cn'

type Props = {
  isOpen: boolean
  onClose?: () => void
  backgroundColor?: CSSProperties['backgroundColor']
  className?: string
}

function Modal({ children, isOpen, onClose, className }: PropsWithChildren<Props>) {
  useEffect(
    function ScrollLock() {
      if (isOpen) {
        document.body.style.overflow = 'hidden'
      } else {
        document.body.style.overflow = 'auto'
      }
    },
    [isOpen]
  )

  useEffect(
    function CloseModal() {
      if (isOpen) {
        document.addEventListener('keydown', handleKeyDown)
      }
    },
    [isOpen]
  )

  const handleKeyDown = (e: KeyboardEvent) => {
    if (e.key === 'Escape') {
      e.preventDefault()
      e.stopPropagation()
      onClose?.()
    }
  }

  return (
    <Portal>
      {isOpen && (
        <>
          <div
            onClick={onClose}
            className={cn('fixed z-[9000] inset-0 flex justify-center items-center', className)}
          />
          <div className='absolute z-[9999] top-1/2 left-1/2 -translate-x-1/2 -translate-y-1/2'>
            {children}
          </div>
        </>
      )}
    </Portal>
  )
}

export default Modal
