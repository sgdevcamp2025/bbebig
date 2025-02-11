import { CustomPresenceStatus } from '@/types/user'

import Avatar from '../avatar'

interface UserListItemProps {
  avatarUrl: string
  name: string
  description: string
  status: CustomPresenceStatus
  statusColor?: string
  iconType: 'default' | 'request' | 'response'
  onClick?: () => void
}

const ICON_PATH = {
  default: ['/icon/friend/dm.svg', '/icon/friend/more.svg'],
  response: ['/icon/friend/accept.svg', '/icon/friend/reject.svg'],
  request: ['/icon/friend/reject.svg']
} as const

function UserListItem({
  avatarUrl,
  name,
  description,
  status,
  statusColor,
  iconType = 'default'
}: UserListItemProps) {
  return (
    <div className='group flex items-center justify-between p-2 hover:bg-discord-gray-500 rounded cursor-pointer'>
      <div className='flex items-center gap-3'>
        <Avatar
          avatarUrl={avatarUrl}
          statusColor={statusColor}
          status={status}
          size='sm'
        />

        <div className='flex flex-col'>
          <span className='text-discord-font-color-normal font-medium'>{name}</span>
          <span className='text-discord-font-color-muted text-sm'>{description}</span>
        </div>
      </div>

      <div className='flex items-center gap-1'>
        {ICON_PATH[iconType].map((iconPath) => (
          <button
            key={iconPath}
            type='button'
            aria-label={'icon'}
            className='p-2 bg-discord-gray-700 hover:bg-discord-gray-800 rounded-3xl group-hover:bg-discord-gray-800'>
            <img
              src={iconPath}
              alt={'icon'}
              className='w-5 h-5'
            />
          </button>
        ))}
      </div>
    </div>
  )
}

export default UserListItem
