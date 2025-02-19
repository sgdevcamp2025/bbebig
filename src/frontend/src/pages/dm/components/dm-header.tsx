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
        avatarUrl={friend.memberAvatarUrl}
        size='sm'
        name={friend.memberNickname}
      />
      <span className='text-discord-font-color-normal text-base font-semibold'>
        {friend.memberName}
      </span>
    </div>
  )
}

export default DmHeader
