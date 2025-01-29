import { ArrowDownToLine } from 'lucide-react'
import deviceHelper from '@/utils/device-helper'

function Bottom() {
  return (
    <div className='flex flex-col items-center justify-center'>
      <div className='flex flex-col items-center justify-center text-white'>
        <h2 className='whitespace-pre pb-6 text-center text-[1.875rem] font-bold leading-[1.2] 2xl:text-[2.9375rem]'>
          {'더는 스크롤 할 수 없어요.\n채팅을 하러 가세요.'}
        </h2>
        <button className='flex flex-row items-center gap-2 rounded-[40px] bg-brand px-[32px] py-[14px] text-[20px]'>
          <ArrowDownToLine className='h-6 w-6' />
          <span>{deviceHelper.isMac ? 'Mac용 다운로드' : 'Windows용 다운로드'}</span>
        </button>
      </div>
      <img
        src='https://cdn.prod.website-files.com/6257adef93867e50d84d30e2/6658cc069d1eb1caf9426914_Footer-Art_cut.webp'
        loading='eager'
        width='1440'
        sizes='(max-width: 1919px) 100vw, 1279px'
        alt="YOU CAN'T SCROLL ANYMORE.BETTER GO CHAT."
        srcSet='https://cdn.prod.website-files.com/6257adef93867e50d84d30e2/6658cc069d1eb1caf9426914_Footer-Art_cut-p-500.webp 500w, https://cdn.prod.website-files.com/6257adef93867e50d84d30e2/6658cc069d1eb1caf9426914_Footer-Art_cut-p-800.webp 800w, https://cdn.prod.website-files.com/6257adef93867e50d84d30e2/6658cc069d1eb1caf9426914_Footer-Art_cut-p-1080.webp 1080w, https://cdn.prod.website-files.com/6257adef93867e50d84d30e2/6658cc069d1eb1caf9426914_Footer-Art_cut-p-1600.webp 1600w, https://cdn.prod.website-files.com/6257adef93867e50d84d30e2/6658cc069d1eb1caf9426914_Footer-Art_cut-p-2000.webp 2000w, https://cdn.prod.website-files.com/6257adef93867e50d84d30e2/6658cc069d1eb1caf9426914_Footer-Art_cut-p-2600.webp 2600w, https://cdn.prod.website-files.com/6257adef93867e50d84d30e2/6658cc069d1eb1caf9426914_Footer-Art_cut.webp 2880w'
      />
      <div className='h-[4px] w-full bg-bottom-border-background' />
      <div className='w-full bg-video-banner-background pb-[64px] pt-[80px] 2xl:h-[525px]' />
    </div>
  )
}

export default Bottom
