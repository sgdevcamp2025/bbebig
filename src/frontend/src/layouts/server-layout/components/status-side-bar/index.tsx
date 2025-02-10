import Avatar from '@/components/avatar'
import { User } from '@/types/user'

interface StatusSideBarProps {
  users: User[]
}

function StatusSideBar({ users }: StatusSideBarProps) {
  const onlineUsers = users?.filter((user) => user.customPresenceStatus === 'ONLINE') ?? []
  const offlineUsers = users?.filter((user) => user.customPresenceStatus !== 'ONLINE') ?? []

  return (
    <div className='w-60 bg-discord-gray-700 h-screen flex flex-col p-4 text-white'>
      {onlineUsers.length > 0 && (
        <>
          <h3 className='text-xs font-semibold text-gray-300 mb-2'>
            온라인 - {onlineUsers.length}
          </h3>
          <div className='flex flex-col gap-2 mb-8'>
            {onlineUsers.map((user) => (
              <div
                key={user.id}
                className='flex items-center gap-2 cursor-pointer hover:bg-discord-gray-600 rounded p-1'>
                <Avatar
                  avatarUrl={user.avatarUrl}
                  size='sm'
                  status={user.customPresenceStatus}
                  statusColor={'black'}
                />
                <span className='text-sm'>{user.name}</span>
              </div>
            ))}
          </div>
        </>
      )}

      {offlineUsers.length > 0 && (
        <>
          <h3 className='text-xs font-semibold text-gray-300 mb-2'>
            오프라인 - {offlineUsers.length}
          </h3>
          <div className='flex flex-col gap-2 opacity-50'>
            {offlineUsers.map((user) => (
              <div
                key={user.id}
                className='flex items-center gap-2 cursor-pointer hover:bg-discord-gray-600 rounded p-1'>
                <Avatar
                  avatarUrl={user.avatarUrl}
                  size='sm'
                  status={user.customPresenceStatus}
                  statusColor={'black'}
                />
                <span className='text-sm'>{user.name}</span>
              </div>
            ))}
          </div>
        </>
      )}
    </div>
  )
}

export default StatusSideBar
