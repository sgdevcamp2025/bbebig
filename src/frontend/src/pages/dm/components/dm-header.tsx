import Avatar from '@/components/avatar'
import { User } from '@/types/user'

interface DmHeaderProps {
  member: User
}

function DmHeader({ member }: DmHeaderProps) {
  return (
    <div className='flex h-12 items-center gap-2 '>
      <Avatar
        statusColor='black'
        avatarUrl={member.avatarUrl}
        size='sm'
        name={member.nickname}
      />
      <span className='text-discord-font-color-normal text-base font-semibold'>
        {member.nickname}
      </span>
    </div>
  )
}

export default DmHeader
