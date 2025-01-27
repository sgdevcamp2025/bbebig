import { ArrowDownToLine } from 'lucide-react'

import deviceHelper from '@/libs/device-helper'

function HomeHero() {
  return (
    <div className='h-dvh-screen bg-landing-shine-1 desktop-large:bg-[length:96%_96%] desktop-large:bg-[position:100%_-33%] desktop-xlarge:pb-[13rem] bg-cover bg-no-repeat pb-[7.25rem] pt-[8rem]'>
      <div className='padding-global'>
        <div className='tablet-range:flex tablet-range:flex-col tablet-range:items-center mx-auto mb-14 w-[90%] max-w-[1080px]'>
          <div className='desktop-range:hidden w-full max-w-[43rem]'>
            <div className='mx-auto mb-[1rem]'>
              <div className='bg-landing-hero-background min-h-[401px] w-full bg-contain bg-center bg-no-repeat' />
            </div>
          </div>
          <div className='mobile-hidden flex justify-between'>
            <div className='desktop-small:mt-[4rem] desktop-small:max-w-[28rem] desktop-xlarge:min-w-[448px] mb-auto w-full text-white'>
              <div className='pb-[6px]'>
                <h1 className='desktop-small:text-[2.75rem] desktop-large:mb-[0.5rem] desktop-large:text-[3.125rem] mobile-range:text-center mobile-range:text-[1.68375rem] mobile-range:leading-[120%] tablet-range:mb-[2.25rem] tablet-range:mt-[3rem] tablet-range:text-center tablet-range:text-[35px] tablet-range:leading-[120%] w-full font-extrabold uppercase leading-[120%] tracking-[-0.01em]'>
                  {`재미와 게임으로 가득\n한 그룹 채팅`}
                </h1>
              </div>
              <div className='desktop-large:mt-[0.5rem] mt-[0.25rem] overflow-hidden'>
                <p className='desktop:text-[1.5rem] desktop:leading-[130%] desktop-small:text-[1.5rem] mobile-range:text-center tablet-range:text-center pr-[2.7rem] text-[18px] font-medium leading-[130%] tracking-[0.02rem]'>
                  Discord는 친구들과 게임을 플레이하며 놀거나 글로벌 커뮤니티를 만들기에 좋습니다.
                  나만의 공간을 만들어 대화하고, 게임을 플레이하며, 어울려 보세요.
                </p>
              </div>
            </div>
            <div className='desktop-small:right-[-3%] desktop-small:ml-0 desktop-large:right-[-6%] desktop-xlarge:right-[-11.5%] mobile-range:hidden tablet-range:hidden relative w-full'>
              <div className='desktop-small:mr-auto desktop-large:left-[17px] desktop-large:mt-[-14px] desktop-xlarge:left-3 relative'>
                <div className='desktop-small:-left-[-14%]] bg-landing-hero-background desktop-small:min-h-[480px] desktop-small:min-w-[46rem] desktop-small:max-w-none desktop-large:ml-[-2vw] desktop-large:mr-[-2vw] desktop-large:min-h-[557px] desktop-large:min-w-[52rem] bg-contain bg-no-repeat' />
              </div>
            </div>
          </div>
        </div>
      </div>
      <aside className='flex h-20 flex-col justify-center'>
        <div className='mobile-range:flex-col mx-auto mt-6 flex flex-row items-center justify-center gap-4'>
          <button className='flex w-fit gap-2 rounded-full bg-white px-8 py-4 font-semibold text-black'>
            <ArrowDownToLine className='h-6 w-6' />
            <span>{deviceHelper.isMac ? 'Mac용 다운로드' : 'Windows용 다운로드'}</span>
          </button>
          <button className='rounded-full bg-[#161cbb] px-8 py-4 font-semibold text-white'>
            웹브라우저에서 Discord 열기
          </button>
        </div>
      </aside>
    </div>
  )
}

export default HomeHero
