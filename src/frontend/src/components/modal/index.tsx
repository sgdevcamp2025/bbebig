import { useEffect, type PropsWithChildren } from 'react'
import Portal from '../portal'

type Props = {
  isOpen: boolean
  onClose?: () => void
}

function Modal({ children, isOpen, onClose }: PropsWithChildren<Props>) {
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
            className='fixed z-[9000] inset-0 bg-black/70 flex justify-center items-center'
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
