import { Suspense, useRef, useState } from 'react'

import Avatar from '@/components/avatar'
import LoadingIcon from '@/components/loading-icon'
import { useGetServerMember } from '@/hooks/queries/server/useGetServerMember'
import { CustomPresenceStatus } from '@/types/user'

import UserProfileCard from '../user-profile-card'

interface StatusSideBarProps {
  channelUserList: ChannelStatusBarUser[]
}

export interface ChannelStatusBarUser {
  memberId: number
  nickName: string
  avatarUrl: string | null
  bannerUrl: string | null
  globalStatus: CustomPresenceStatus
}

function Inner({ channelUserList }: StatusSideBarProps) {
  const onlineUsers = Array.isArray(channelUserList)
    ? channelUserList.filter((user) => user.globalStatus === 'ONLINE')
    : []

  const offlineUsers = Array.isArray(channelUserList)
    ? channelUserList.filter((user) => user.globalStatus !== 'ONLINE')
    : []

  const [selectedUser, setSelectedUser] = useState<ChannelStatusBarUser | null>(null)
  const [selectedUserPosition, setSelectedUserPosition] = useState<{ top: number; left: number }>({
    top: 0,
    left: 0
  })
  const sidebarRef = useRef<HTMLDivElement>(null)

  const handleSendFriendRequest = () => {
    console.log('친구 요청 보내기:', selectedUser?.nickName)
  }

  const handleMoreButtonClick = () => {
    console.log('더보기 메뉴')
  }

  const handleClickUser = (user: ChannelStatusBarUser, event: React.MouseEvent<HTMLDivElement>) => {
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
                  key={user.memberId}
                  className='flex items-center gap-2 cursor-pointer hover:bg-discord-gray-600 rounded p-1'
                  onClick={(event) => handleClickUser(user, event)}>
                  <Avatar
                    name={user.nickName}
                    avatarUrl={user.avatarUrl ?? ''}
                    size='sm'
                    status={user.globalStatus}
                    statusColor={'black'}
                  />
                  <span className='text-sm'>{user.nickName}</span>
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
                  key={user.memberId}
                  className='flex items-center gap-2 cursor-pointer hover:bg-discord-gray-600 rounded p-1'
                  onClick={(event) => handleClickUser(user, event)}>
                  <Avatar
                    name={user.nickName}
                    avatarUrl={user.avatarUrl ?? ''}
                    size='sm'
                    status={user.globalStatus}
                    statusColor={'black'}
                  />
                  <span className='text-sm'>{user.nickName}</span>
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
          <Suspense
            fallback={
              <div className='flex justify-center items-center h-full bg-black-100 p-2 rounded-[8px] border-black-90 border-[4px]'>
                <LoadingIcon />
              </div>
            }>
            <UserProfileCard
              user={selectedUser}
              onSendFriendRequest={handleSendFriendRequest}
              onMoreButtonClick={handleMoreButtonClick}
            />
          </Suspense>
        </div>
      )}
    </div>
  )
}

export function StatusSideBar({ serverId }: { serverId: string }) {
  const serverMemebersData = useGetServerMember(serverId)

  const currentChannelUsers = serverMemebersData.serverMemberInfoList.map((member) => ({
    memberId: member.memberId,
    nickName: member.nickName,
    avatarUrl: member.avatarUrl,
    bannerUrl: member.bannerUrl,
    globalStatus: member.globalStatus
  }))

  return <Inner channelUserList={currentChannelUsers} />
}
