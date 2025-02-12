import Avatar from '@/components/avatar'
import { Friend } from '@/types/friend'

interface DmHeaderProps {
  friend: Friend
}

function DmHeader({ friend }: DmHeaderProps) {
  return (
    <div className='flex items-center px-3 py-3 border-b border-discord-gray-800'>
      <div className='flex items-center gap-2 '>
        <Avatar
          statusColor='black'
          avatarUrl={friend.avatarUrl}
          status={friend.status}
          size='sm'
        />
        <span className='text-discord-font-color-normal text-base font-semibold'>
          {friend.name}
        </span>
      </div>

      <div className='flex items-center gap-4 ml-auto'>
        <button type='button'>
          <img
            className='w-[24px] h-[24px]'
            src='/icon/dm/call.svg'
          />
        </button>
        <button type='button'>
          <img
            className='w-[24px] h-[24px]'
            src='/icon/dm/video.svg'
          />
        </button>
      </div>
    </div>
  )
}

export default DmHeader
