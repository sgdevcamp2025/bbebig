import { useEffect, useState } from 'react'

import Modal from '../modal'

function LoadingModal() {
  const [isOpen, setIsOpen] = useState(true)

  useEffect(() => {
    return () => {
      setIsOpen(false)
    }
  }, [])

  const onClose = () => {
    setIsOpen(false)
  }

  return (
    <Modal
      className='bg-[#292B32]'
      isOpen={isOpen}
      onClose={onClose}>
      <section className='w-full h-full bg-br flex flex-col items-center justify-center gap-4'>
        <video
          autoPlay
          loop
          playsInline
          muted
          className='w-[200px] h-[200px]'>
          <source
            src='/video/loading/spinner.mp4'
            type='video/mp4'
          />
          <source
            src='/video/loading/spinner.webm'
            type='video/webm'
          />
          <source
            src='/video/loading/spinner.png'
            type='image/png'
          />
        </video>

        <div className='text-white text-[12px] font-bold'>알고 계셨나요?</div>
        <div className='text-white text-center whitespace-pre-wrap'>
          {`접근성 설정에서 텍스트 음성 변환(TTS) 속도를 \n바꿀 수 있습니다.`}
        </div>
        <div className='text-white text-[12px]'>
          Made By{' '}
          <a
            className='text-brand-20'
            href='https://github.com/sgdevcamp2025/bbebig'
            target='_blank'
            rel='noreferrer'>
            @BBeBig
          </a>
        </div>
      </section>
    </Modal>
  )
}

export default LoadingModal
