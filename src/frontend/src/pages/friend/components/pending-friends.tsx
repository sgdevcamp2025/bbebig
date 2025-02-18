import { ChangeEvent, useState } from 'react'

import SearchInput from '@/components/search-input'
import UserListItem from '@/components/user-list-item'
import { statusKo } from '@/constants/status'
import { useGetFriendPendingList } from '@/hooks/queries/user/useGetFriendPendingList'
import { CustomPresenceStatus } from '@/types/user'

function PendingFriends() {
  const friendPendingList = useGetFriendPendingList()

  const [searchValue, setSearchValue] = useState('')

  const filteredFriends = friendPendingList.filter((friend) =>
    friend.name.toLowerCase().includes(searchValue.toLowerCase())
  )

  const responsePendingFriends = filteredFriends.filter(
    (friend: { friendStatus: string }) => friend.friendStatus === 'RESPONSE_PENDING'
  )
  const requestPendingFriends = filteredFriends.filter(
    (friend: { friendStatus: string }) => friend.friendStatus === 'REQUEST_PENDING'
  )

  const handleSearch = (e: ChangeEvent<HTMLInputElement>) => {
    setSearchValue(e.target.value)
  }

  const handleClear = () => {
    setSearchValue('')
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
              받음 — {responsePendingFriends.length}
            </div>
            {responsePendingFriends.map(
              (friend: { id: number; avatarUrl: string; name: string; status: string }) => (
                <UserListItem
                  key={friend.id}
                  id={friend.id}
                  avatarUrl={friend.avatarUrl}
                  name={friend.name}
                  status={friend.status.toString() as CustomPresenceStatus}
                  description={statusKo[friend.status.toString() as CustomPresenceStatus]}
                  statusColor='black'
                  iconType='response'
                />
              )
            )}
          </div>
        )}

        {requestPendingFriends.length > 0 && (
          <div className='flex flex-col gap-2'>
            <div className='text-discord-font-color-muted text-xs font-semibold mb-2'>
              요청 — {requestPendingFriends.length}
            </div>
            {requestPendingFriends.map(
              (friend: { id: number; avatarUrl: string; name: string; status: string }) => (
                <UserListItem
                  key={friend.id}
                  id={friend.id}
                  avatarUrl={friend.avatarUrl}
                  name={friend.name}
                  status={friend.status.toString() as CustomPresenceStatus}
                  description={statusKo[friend.status.toString() as CustomPresenceStatus]}
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
