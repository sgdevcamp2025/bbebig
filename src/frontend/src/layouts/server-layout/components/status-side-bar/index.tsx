import { useRef, useState } from 'react'

import Avatar from '@/components/avatar'
import { ChannelUser } from '@/types/server'
import { User } from '@/types/user'

import UserProfileCard from '../user-profile-card'

interface StatusSideBarProps {
  channelUserList: ChannelUser[]
}

function StatusSideBar({ channelUserList }: StatusSideBarProps) {
  const onlineUsers =
    channelUserList?.filter((user) => user.customPresenceStatus === 'ONLINE') ?? []
  const offlineUsers =
    channelUserList?.filter((user) => user.customPresenceStatus !== 'ONLINE') ?? []
  const [selectedUser, setSelectedUser] = useState<User | null>(null)
  const [selectedUserPosition, setSelectedUserPosition] = useState<{ top: number; left: number }>({
    top: 0,
    left: 0
  })
  const sidebarRef = useRef<HTMLDivElement>(null)

  const handleSendFriendRequest = () => {
    console.log('친구 요청 보내기:', selectedUser?.name)
  }

  const handleMoreButtonClick = () => {
    console.log('더보기 메뉴')
  }

  const handleClickUser = (user: User, event: React.MouseEvent<HTMLDivElement>) => {
    setSelectedUser(user)

    if (sidebarRef.current) {
      const sidebarRect = sidebarRef.current.getBoundingClientRect()
      const userRect = event.currentTarget.getBoundingClientRect()

      setSelectedUserPosition({
        top: userRect.top - sidebarRect.top,
        left: -310
      })
    }
  }

  return (
    <div
      className='relative'
      ref={sidebarRef}>
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
                  className='flex items-center gap-2 cursor-pointer hover:bg-discord-gray-600 rounded p-1'
                  onClick={(event) => handleClickUser(user, event)}>
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
                  className='flex items-center gap-2 cursor-pointer hover:bg-discord-gray-600 rounded p-1'
                  onClick={(event) => handleClickUser(user, event)}>
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

      {selectedUser && (
        <div
          className='absolute'
          style={{
            top: `${selectedUserPosition.top}px`,
            left: `${selectedUserPosition.left}px`
          }}>
          <UserProfileCard
            user={selectedUser}
            onSendFriendRequest={handleSendFriendRequest}
            onMoreButtonClick={handleMoreButtonClick}
          />
        </div>
      )}
    </div>
  )
}

export default StatusSideBar
