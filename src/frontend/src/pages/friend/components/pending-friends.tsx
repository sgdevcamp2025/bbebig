import { ChangeEvent, useEffect, useState } from 'react'

import SearchInput from '@/components/search-input'
import UserListItem from '@/components/user-list-item'
import { useAcceptFriendRequest } from '@/hooks/queries/user/useAcceptFriendRequest'
import { useDeclineFriendRequest } from '@/hooks/queries/user/useDeclineFriendRequest'
import { useGetFriendPendingList } from '@/hooks/queries/user/useGetFriendPendingList'

function PendingFriends() {
  const friendPendingList = useGetFriendPendingList()
  const acceptFriendRequest = useAcceptFriendRequest()
  const declineFriendRequest = useDeclineFriendRequest()
  const [searchValue, setSearchValue] = useState('')

  const [responsePendingFriends, setResponsePendingFriends] = useState(
    friendPendingList.receivePendingFriends
  )

  const [requestPendingFriends, setRequestPendingFriends] = useState(
    friendPendingList.sendPendingFriends
  )

  useEffect(() => {
    setResponsePendingFriends(friendPendingList.receivePendingFriends)
    setRequestPendingFriends(friendPendingList.sendPendingFriends)
  }, [friendPendingList])

  const handleSearch = (e: ChangeEvent<HTMLInputElement>) => {
    setSearchValue(e.target.value)
  }

  const allPendingList = [...responsePendingFriends, ...requestPendingFriends]

  const filteredFriends = searchValue
    ? allPendingList.filter((friend) => new RegExp(searchValue, 'i').test(friend.memberName))
    : allPendingList

  const handleClear = () => {
    setSearchValue('')
  }

  const handleAcceptFriend = (friendId: number) => {
    setResponsePendingFriends((prev) => prev.filter((friend) => friend.friendId !== friendId))
    acceptFriendRequest.mutate({ friendId })
  }

  const handleDeclineFriend = (friendId: number) => {
    setResponsePendingFriends((prev) => prev.filter((friend) => friend.friendId !== friendId))
    declineFriendRequest.mutate({ friendId })
  }

  return (
    <div className='flex flex-col gap-6 p-4'>
      <SearchInput
        value={searchValue}
        onChange={handleSearch}
        handleClear={handleClear}
        placeholder='검색하기'
      />

      <div className='flex flex-col gap-4'>
        {responsePendingFriends.length > 0 && (
          <div className='flex flex-col gap-2'>
            <div className='text-discord-font-color-muted text-xs font-semibold mb-2'>
              받음 — {friendPendingList.receivePendingFriendsCount}
            </div>
            {responsePendingFriends.map(
              (friend: {
                friendId: number
                memberId: number
                memberName: string
                memberNickname: string
                memberAvatarUrl: string | null
                memberBannerUrl: string | null
                memberIntroduce: string | null
                memberEmail: string
              }) => (
                <UserListItem
                  key={friend.friendId}
                  id={friend.memberId}
                  avatarUrl={friend.memberAvatarUrl ?? '/image/common/default-avatar.png'}
                  name={friend.memberName}
                  description={friend.memberEmail}
                  statusColor='black'
                  iconType='response'
                  handleAcceptFriendRequest={() => handleAcceptFriend(friend.friendId)}
                  handleDeclineFriendRequest={() => handleDeclineFriend(friend.friendId)}
                />
              )
            )}
          </div>
        )}

        {requestPendingFriends.length > 0 && (
          <div className='flex flex-col gap-2'>
            <div className='text-discord-font-color-muted text-xs font-semibold mb-2'>
              보냄 — {friendPendingList.pendingFriendsCount}
            </div>
            {requestPendingFriends.map(
              (friend: {
                friendId: number
                memberId: number
                memberName: string
                memberNickname: string
                memberAvatarUrl: string | null
                memberBannerUrl: string | null
                memberIntroduce: string | null
                memberEmail: string
              }) => (
                <UserListItem
                  key={friend.friendId}
                  id={friend.memberId}
                  avatarUrl={friend.memberAvatarUrl ?? '/image/common/default-avatar.png'}
                  name={friend.memberName}
                  description={friend.memberEmail}
                  statusColor='black'
                  iconType='request'
                />
              )
            )}
          </div>
        )}

        {filteredFriends.length === 0 && (
          <div className='text-discord-font-color-muted text-sm'>검색 결과가 없습니다.</div>
        )}
      </div>
    </div>
  )
}

export default PendingFriends
