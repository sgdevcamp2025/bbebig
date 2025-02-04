import Modal from '@/components/modal'
import { cn } from '@/libs/cn'
import { useState } from 'react'

type Props = {
  tabId: number
  itemId: number
  isOpen: boolean
  onClose: () => void
}

const tabs = [
  {
    id: 1,
    name: '사용자 설정',
    items: [
      {
        id: 101,
        name: '내 계정'
      },
      {
        id: 102,
        name: '프로필'
      }
    ]
  },
  {
    id: 2,
    name: '앱 설정',
    items: [
      {
        id: 201,
        name: '음성 및 비디오'
      },
      {
        id: 202,
        name: '알림'
      }
    ]
  }
] as const

function SettingModal({ itemId, isOpen, onClose }: Props) {
  const [currentItemId, setCurrentItemId] = useState(itemId)

  const handleClickItem = (itemId: number) => {
    console.log('itemId', itemId)
    setCurrentItemId(itemId)
  }

  return (
    <Modal
      isOpen={isOpen}
      onClose={onClose}>
      <section className='w-dvw h-dvh bg-brand-10 motion-scale-in-90 motion-duration-[400ms] motion-ease-[cubic-bezier(1,-0.4,0.35,0.95)]'>
        <div className='flex h-full'>
          <div className='flex justify-end flex-[1_0_auto] bg-gray-20'>
            <nav className='py-[60px] px-[6px] mt-[56px] flex flex-col gap-4'>
              {tabs.map((tab) => (
                <div key={tab.id}>
                  <h3
                    className={cn(
                      'text-sm font-medium h-7 w-[192px] py-[6px] px-[10px] text-white-20'
                    )}>
                    {tab.name}
                  </h3>
                  <ul>
                    {tab.items.map((item) => (
                      <li
                        key={item.id}
                        className={cn(
                          'text-gray-90 flex items-center text-sm font-medium h-7 w-[192px] py-[6px] px-[10px]',
                          currentItemId === item.id && 'text-white-100 bg-gray-80'
                        )}>
                        <button
                          type='button'
                          onClick={() => handleClickItem(item.id)}
                          className='w-full h-full text-left leading-4'>
                          {item.name}
                        </button>
                      </li>
                    ))}
                  </ul>
                </div>
              ))}
            </nav>
          </div>
          <div className='flex flex-[1_1_800px] bg-brand-10'></div>
        </div>
      </section>
    </Modal>
  )
}

export default SettingModal
