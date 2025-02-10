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
    name: '차단',
    component: 'BLOCK'
  },
  {
    name: '대기 중',
    component: 'PENDING'
  },
  {
    name: '차단 목록',
    component: 'BLOCK_FRIENDS'
  }
] as const

type TabType = (typeof navigation)[number]['component']

interface HeaderProps {
  onTabChange: (tab: TabType) => void
  currentTab: TabType
}

function Header({ onTabChange, currentTab }: HeaderProps) {
  return (
    <div className='flex items-center px-3 py-3 border-b border-discord-gray-800'>
      <div className='flex items-center gap-2'>
        <img
          src='/icon/dm/friend.svg'
          alt='친구'
          className='w-[20px] h-[20px]'
        />
        <span className='text-discord-font-color-normal text-lg font-semibold'>친구</span>
        <div className='h-5 border-r border-discord-gray-500 mr-4' />
      </div>
      <div className='flex items-center gap-5'>
        {navigation.map((item) => (
          <button
            type='button'
            key={item.component}
            onClick={() => onTabChange(item.component)}
            className={cn(
              'px-1 py-0.5 rounded-md text-base font-medium cursor-pointer',
              'hover:bg-discord-gray-500 hover:text-discord-font-color-normal text-discord-font-color-muted',
              currentTab === item.component && 'bg-discord-gray-500 text-discord-font-color-normal'
            )}>
            {item.name}
          </button>
        ))}
      </div>
      <button
        type='button'
        className='ml-3 px-1 py-0.5 rounded-md bg-[#3BA55D] text-sm text-white font-medium'>
        친구 추가하기
      </button>
    </div>
  )
}

export default Header
