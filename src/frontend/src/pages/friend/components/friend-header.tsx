import CommonHeader from '@/components/common-header'
import { cn } from '@/libs/cn'

const navigation = [
  {
    name: '온라인',
    component: 'ONLINE'
  },
  {
    name: '모두',
    component: 'ALL'
  },
  {
    name: '대기 중',
    component: 'PENDING'
  }
] as const

type TabType = (typeof navigation)[number]['component']

interface HeaderProps {
  onTabChange: (tab: TabType) => void
  currentTab: TabType
}

function FriendHeader({ onTabChange, currentTab }: HeaderProps) {
  return (
    <CommonHeader type='FRIEND'>
      <>
        <img
          src='/icon/dm/friend.svg'
          alt='친구'
          className='w-[20px] h-[20px]'
        />
        <span className='text-discord-font-color-normal text-lg font-semibold'>친구</span>
        <div className='h-5 border-r border-discord-gray-500 mr-4' />

        <div className='flex items-center gap-5'>
          {navigation.map((item) => (
            <button
              type='button'
              key={item.component}
              onClick={() => onTabChange(item.component)}
              className={cn(
                'px-1 py-0.5 rounded-md text-base font-medium cursor-pointer',
                'hover:bg-discord-gray-500 hover:text-discord-font-color-normal text-discord-font-color-muted',
                currentTab === item.component &&
                  'bg-discord-gray-500 text-discord-font-color-normal'
              )}>
              {item.name}
            </button>
          ))}
        </div>
        <button
          type='button'
          className='ml-5 px-1 py-0.5 rounded-md bg-[#248045] text-base text-white font-medium'>
          친구 추가하기
        </button>
      </>
    </CommonHeader>
  )
}

export default FriendHeader
