import { CSSProperties, useState } from 'react'

import AvatarCard from '@/components/avatar-card'
import CustomButton from '@/components/custom-button'
import { cn } from '@/libs/cn'
import { User } from '@/types/user'

import ChatArea from './chat-area'

interface Props {
  channelId: number
  channelName: string
}

const userList = [
  {
    id: '1',
    name: '홍길동',
    avatarUrl: '/image/common/default-avatar.png',
    bannerUrl: '/image/common/default-background.png',
    backgroundColor: 'grey',
    micStatus: false,
    headphoneStatus: true
  },
  {
    id: '2',
    name: '이순신',
    avatarUrl: '/image/common/default-avatar.png',
    bannerUrl: '/image/common/default-background.png',
    backgroundColor: 'grey',
    micStatus: true,
    headphoneStatus: true
  }
] as (Pick<User, 'id' | 'name' | 'avatarUrl' | 'bannerUrl'> & {
  micStatus: boolean
  headphoneStatus: boolean
  backgroundColor: CSSProperties['backgroundColor']
})[]

function VideoComponent({ channelId, channelName }: Props) {
  const [sideBar, setSideBar] = useState(true)

  const isEmptyText = userList.length === 0

  return (
    <div className='flex flex-1 h-screen'>
      <div
        className={cn('flex w-full bg-black', {
          'rounded-r-lg mr-2': sideBar
        })}>
        <div className='w-full h-full flex flex-col'>
          <section className='h-12 px-4 w-full flex items-center justify-between'>
            <div className='flex items-center gap-2'>
              <img
                src={`/icon/channel/type-voice.svg`}
                className='w-6 h-6'
              />
              <span className='text-white-10 text-md font-bold'>{channelName}</span>
            </div>
            <button
              type='button'
              onClick={() => setSideBar(true)}>
              <img
                src='/icon/channel/threads.svg'
                className='w-6 h-6'
                alt='스레드'
              />
            </button>
          </section>
          <div />
          <section className='flex-1 flex flex-col items-center justify-center gap-2'>
            <ul className='flex gap-2 flex-wrap mb-8'>
              {userList.map((user) => (
                <li key={user.id}>
                  <AvatarCard
                    name={user.name}
                    avatarUrl={user.avatarUrl}
                    backgroundUrl={user.bannerUrl}
                    backgroundColor={user.backgroundColor}
                    size='sm'
                    micStatus={user.micStatus}
                    headphoneStatus={user.headphoneStatus}
                  />
                </li>
              ))}
            </ul>
            <h4 className='text-gray-10 text-3xl font-bold'>{channelName}</h4>
            <span className='text-gray-90 text-sm font-semibold'>
              {isEmptyText
                ? '현재 채널에 아무도 없어요'
                : `${userList.map((user) => user.name).join(', ')} 님이 현재 음성 채널에 있어요.`}
            </span>
            <CustomButton
              className='w-fit px-4 py-2 mt-5 mb-10'
              variant='secondary'>
              음성 채널 참가하기
            </CustomButton>
          </section>
        </div>
      </div>
      {sideBar && (
        <div className='flex flex-col min-w-[480px] h-screen bg-brand-10 rounded-l-lg'>
          <ChatArea
            channelId={channelId}
            onClose={() => setSideBar(false)}
          />
        </div>
      )}
    </div>
  )
}

export default VideoComponent
