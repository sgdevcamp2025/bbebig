import { useState } from 'react'

import CloseButton from '@/components/close-button'
import Modal from '@/components/modal'
import { cn } from '@/libs/cn'

interface Props {
  channelName: string
  isOpen: boolean
  onClose: () => void
}

const tabs = [
  {
    id: 1,
    name: '일반',
    settings: [
      {
        id: 101,
        name: '채널 이름'
      }
    ]
  },
  {
    id: 2,
    name: '권한',
    settings: [
      {
        id: 201,
        name: '채널 권한'
      },
      {
        id: 202,
        name: '고급 권한'
      }
    ]
  },
  {
    id: 3,
    name: '초대',
    settings: [
      {
        id: 301,
        name: '초대'
      }
    ]
  },
  {
    id: 4,
    name: '연동',
    settings: [
      {
        id: 401,
        name: '연동'
      }
    ]
  }
] as const

function SettingModal({ channelName, isOpen, onClose }: Props) {
  const [currentTabId, setCurrentTabId] = useState<number>(tabs[0].id)
  const currentTab = tabs.find((tab) => tab.id === currentTabId) || tabs[0]

  return (
    <Modal
      isOpen={isOpen}
      onClose={onClose}>
      <section className='w-dvw h-dvh bg-brand-10 motion-scale-in-90 motion-duration-[400ms] motion-ease-[cubic-bezier(1,-0.4,0.35,0.95)]'>
        <div className='flex h-full'>
          <div className='flex justify-end flex-[1_0_auto] bg-gray-20'>
            <nav className='py-[60px] px-[50px] flex flex-col gap-2'>
              <span className='flex items-center text-sm font-bold text-discord-gray-300 gap-1'>
                <img
                  src='/icon/channel/type-text.svg'
                  alt='#'
                />
                {channelName}
                <span className='text-white-20'>채널</span>
              </span>
              {tabs.map((tab) => (
                <button
                  key={tab.id}
                  type='button'
                  onClick={() => setCurrentTabId(tab.id)}
                  className={cn(
                    'flex items-center px-3 text-base font-medium h-8 w-[192px] text-white-20 rounded-[8px]',
                    currentTabId === tab.id && 'text-white-100 bg-gray-80'
                  )}>
                  {tab.name}
                </button>
              ))}

              <button
                type='button'
                className='flex items-center px-3 text-base font-medium h-10 w-[192px] text-white-20 gap-2 border-t-[0.2px] border-discord-gray-400'>
                채널 삭제하기
                <img
                  className='w-[15px] h-[15px]'
                  src='/icon/channel/delete.svg'
                  alt='삭제'
                />
              </button>
            </nav>
          </div>

          <div className='relative flex flex-[1_1_800px] bg-brand-10 p-6'>
            <div className='flex flex-col gap-5 w-full'>
              <div className='flex items-center justify-between pr-[40px]'>
                <h2 className='text-lg font-bold text-white'>{currentTab?.name}</h2>
                <CloseButton onClick={onClose} />
              </div>
              <div className='flex flex-col gap-3'>
                {currentTab?.settings.map((setting) => (
                  <span
                    key={setting.id}
                    className='text-xs text-white-20'>
                    {setting.name}
                  </span>
                ))}
              </div>
            </div>
          </div>
        </div>
      </section>
    </Modal>
  )
}

export default SettingModal
