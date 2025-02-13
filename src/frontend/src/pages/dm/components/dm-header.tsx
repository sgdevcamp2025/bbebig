import Avatar from '@/components/avatar'
import { Friend } from '@/types/friend'

interface DmHeaderProps {
  friend: Friend
}

function DmHeader({ friend }: DmHeaderProps) {
  return (
    <div className='flex h-12 items-center gap-2 '>
      <Avatar
        statusColor='black'
        avatarUrl={friend.avatarUrl}
        status={friend.status}
        size='sm'
      />
      <span className='text-discord-font-color-normal text-base font-semibold'>{friend.name}</span>
    </div>
  )
}

export default DmHeader
