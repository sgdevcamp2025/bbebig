import { useNavigate } from 'react-router-dom'

import { CustomPresenceStatus } from '@/types/user'

import Avatar from '../avatar'

interface InnerProps {
  id: number
  avatarUrl: string
  name: string
  description: string
  status?: CustomPresenceStatus
  statusColor?: string
  iconType: 'default' | 'request' | 'response'
  handleNavigateToDM?: () => void
  handleDMIconClick?: (e: React.MouseEvent<HTMLButtonElement>) => void
}

const ICON_PATH = {
  default: ['/icon/friend/dm.svg', '/icon/friend/more.svg'],
  response: ['/icon/friend/accept.svg', '/icon/friend/reject.svg'],
  request: ['/icon/friend/reject.svg']
} as const

export function Inner({
  avatarUrl,
  name,
  description,
  status,
  statusColor,
  iconType = 'default',
  handleNavigateToDM,
  handleDMIconClick
}: InnerProps) {
  return (
    <div
      className='group flex items-center justify-between p-2 hover:bg-discord-gray-500 rounded cursor-pointer'
      onClick={() => handleNavigateToDM?.()}>
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
            onClick={iconPath === '/icon/friend/dm.svg' ? (e) => handleDMIconClick?.(e) : undefined}
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

type UserListItemLinkProps = Omit<InnerProps, 'onClick'>

function UserListItemLink({ ...props }: UserListItemLinkProps) {
  const navigate = useNavigate()

  const handleNavigateToDM = () => {
    navigate(`/channels/@me/${props.id}`)
  }

  const handleDMIconClick = (e: React.MouseEvent<HTMLButtonElement>) => {
    e.stopPropagation()
    handleNavigateToDM()
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
