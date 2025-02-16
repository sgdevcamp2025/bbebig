import { useNavigate } from 'react-router'

import { CustomPresenceStatus } from '@/types/user'

import Avatar from '../avatar'

interface UserListItemProps {
  id: number
  avatarUrl: string
  name: string
  description: string
  status: CustomPresenceStatus
  statusColor?: string
  iconType: 'default' | 'request' | 'response'
  handleNavigateToDM: (id: number) => void
  handleDMIconClick: (e: React.MouseEvent<HTMLButtonElement>, id: number) => void
}

const ICON_PATH = {
  default: ['/icon/friend/dm.svg', '/icon/friend/more.svg'],
  response: ['/icon/friend/accept.svg', '/icon/friend/reject.svg'],
  request: ['/icon/friend/reject.svg']
} as const

export function Inner({
  id,
  avatarUrl,
  name,
  description,
  status,
  statusColor,
  iconType = 'default',
  handleNavigateToDM,
  handleDMIconClick
}: UserListItemProps) {
  return (
    <div
      className='group flex items-center justify-between p-2 hover:bg-discord-gray-500 rounded cursor-pointer'
      onClick={() => handleNavigateToDM(id)}>
      <div className='flex items-center gap-3'>
        <Avatar
          name={name}
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
            onClick={
              iconPath === '/icon/friend/dm.svg' ? (e) => handleDMIconClick?.(e, id) : undefined
            }
            aria-label={'icon'}
            className='p-2 bg-discord-gray-700 hover:bg-discord-gray-800 rounded-3xl group-hover:bg-discord-gray-800'>
            <img
              src={iconPath}
              className='w-5 h-5'
            />
          </button>
        ))}
      </div>
    </div>
  )
}

type UserListItemLinkProps = Omit<UserListItemProps, 'onClick'>

function UserListItemLink({ ...props }: UserListItemLinkProps) {
  const navigate = useNavigate()

  const handleNavigateToDM = (id: number) => {
    navigate(`/channels/@me/${id}`)
  }

  const handleDMIconClick = (e: React.MouseEvent<HTMLButtonElement>, id: number) => {
    e.stopPropagation()
    handleNavigateToDM(id)
  }

  return (
    <Inner
      {...props}
      handleNavigateToDM={handleNavigateToDM}
      handleDMIconClick={handleDMIconClick}
    />
  )
}
export default UserListItemLink
