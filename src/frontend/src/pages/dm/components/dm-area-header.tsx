import { Friend } from '@/types/friend'

interface DmAreaHeaderProps {
  friend: Friend
}

function DmAreaHeader({ friend }: DmAreaHeaderProps) {
  return (
    <div className='flex flex-col gap-4 justify-center p-8 pb-6'>
      <div
        className='relative group'
        role='button'
        tabIndex={0}
        aria-label={`${friend.name}의 프로필 사진`}>
        <img
          src={friend.avatarUrl}
          alt={`${friend.name}의 프로필`}
          className='w-20 h-20 rounded-full'
        />
      </div>
      <div className='flex flex-col gap-1'>
        <h2 className='text-2xl font-bold text-discord-font-color-normal'>{friend.name}</h2>
        <p className='text-discord-gray-200 text-sm'>
          <span className='text-discord-gray-100 font-medium'>{friend.name}</span>
          님과 나눈 다이렉트 메시지의 첫 부분이에요
        </p>
      </div>
    </div>
  )
}

export default DmAreaHeader
