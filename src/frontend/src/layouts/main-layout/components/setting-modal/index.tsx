import { useEffect, useState } from 'react'

import CloseButton from '@/components/close-button'
import Modal from '@/components/modal'
import { cn } from '@/libs/cn'

import { AlarmSetting } from './alarm-setting'
import { MyAccount } from './my-account'
import { MyProfile } from './my-profile'
import { VoiceSetting } from './voice-setting'

interface Props {
  itemId: number
  isOpen: boolean
  onClose: () => void
}

export const SettingModalTabsID = {
  none: -1,
  myAccount: 101,
  myProfile: 102,
  voiceSetting: 201,
  alarmSetting: 202
}

const tabs = [
  {
    name: '사용자 설정',
    items: [
      {
        id: SettingModalTabsID.myAccount,
        name: '내 계정'
      },
      {
        id: SettingModalTabsID.myProfile,
        name: '프로필'
      }
    ]
  },
  {
    name: '앱 설정',
    items: [
      {
        id: SettingModalTabsID.voiceSetting,
        name: '음성 및 비디오'
      },
      {
        id: SettingModalTabsID.alarmSetting,
        name: '알림'
      }
    ]
  }
] as const

function SettingModal({ itemId, isOpen, onClose }: Props) {
  const [currentItemId, setCurrentItemId] = useState(SettingModalTabsID.none)

  useEffect(
    function initialCurrentItemId() {
      handleClickItem(itemId)
    },
    [itemId]
  )

  const handleClickItem = (itemId: number) => () => {
    setCurrentItemId(itemId)
  }

  const handleProfileEditClick = () => {
    setCurrentItemId(SettingModalTabsID.myProfile)
  }

  return (
    <Modal
      isOpen={isOpen}
      onClose={onClose}>
      <section className='w-dvw h-dvh bg-brand-10 motion-opacity-in-20 motion-scale-in-90 animate-in fade-in-0 slide-in-from-bottom-10 duration-400 ease-in-out'>
        <div className='flex h-full'>
          <div className='flex justify-end flex-[1_0_auto] bg-gray-20 pr-2'>
            <nav className='py-[60px] px-[6px] mt-[56px] flex flex-col gap-4'>
              {tabs.map((tab) => (
                <div key={tab.name}>
                  <h3
                    className={cn(
                      'text-[12px] leading-4 font-bold h-7 w-[192px] py-[6px] px-[10px] text-white-20'
                    )}>
                    {tab.name}
                  </h3>
                  <ul>
                    {tab.items.map((item) => (
                      <li
                        key={item.id}
                        className={cn(
                          'text-gray-90 flex items-center font-medium h-8 w-[192px] py-[6px] px-[10px] mt-[2px] rounded-md',
                          currentItemId === item.id && 'text-white-100 bg-gray-80'
                        )}>
                        <button
                          type='button'
                          onClick={handleClickItem(item.id)}
                          className='w-full text-left leading-[20px] text-[16px]'>
                          {item.name}
                        </button>
                      </li>
                    ))}
                  </ul>
                </div>
              ))}
            </nav>
          </div>
          <div className='flex flex-[1_1_800px] bg-brand-10'>
            <div className='max-w-[740px] w-full relative'>
              <div className='overflow-y-auto h-full'>
                {currentItemId === SettingModalTabsID.myAccount && (
                  <MyAccount onProfileEditClick={handleProfileEditClick} />
                )}
                {currentItemId === SettingModalTabsID.myProfile && <MyProfile />}
                {currentItemId === SettingModalTabsID.voiceSetting && <VoiceSetting />}
                {currentItemId === SettingModalTabsID.alarmSetting && <AlarmSetting />}
              </div>
              <div className='absolute right-[-40px] top-[60px] text-white'>
                <CloseButton onClick={onClose} />
              </div>
            </div>
          </div>
        </div>
      </section>
    </Modal>
  )
}

export default SettingModal
