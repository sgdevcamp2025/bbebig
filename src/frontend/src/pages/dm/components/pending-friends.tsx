import { useState } from 'react'

import SearchInput from '@/components/search-input'
import UserListItem from '@/components/user-list-item'
import { statusKo } from '@/constants/status'
import { Friend } from '@/types/friend'

const DUMMY_FRIENDS: Friend[] = [
  {
    id: 1,
    avatarUrl: '/image/common/default-avatar.png',
    name: '이지형',
    status: 'ONLINE',
    friendStatus: 'RESPONSE_PENDING'
  },
  {
    id: 2,
    avatarUrl: '/image/common/default-avatar.png',
    name: '김예지',
    status: 'OFFLINE',
    friendStatus: 'REQUEST_PENDING'
  }
]

function PendingFriends() {
  const [searchValue, setSearchValue] = useState('')
  const filteredFriends = DUMMY_FRIENDS.filter((friend) =>
    friend.name.toLowerCase().includes(searchValue.toLowerCase())
  )

  const responsePendingFriends = filteredFriends.filter(
    (friend) => friend.friendStatus === 'RESPONSE_PENDING'
  )
  const requestPendingFriends = filteredFriends.filter(
    (friend) => friend.friendStatus === 'REQUEST_PENDING'
  )

  const handleSearch = (value: string) => {
    setSearchValue(value)
  }
  return (
    <div className='flex flex-col gap-6 p-4'>
      <SearchInput
        onSearch={handleSearch}
        placeholder='검색'
      />

      <div className='flex flex-col gap-4'>
        {responsePendingFriends.length > 0 && (
          <div className='flex flex-col gap-2'>
            <div className='text-discord-font-color-muted text-xs font-semibold mb-2'>
              받음 — {responsePendingFriends.length}
            </div>
            {responsePendingFriends.map((friend) => (
              <UserListItem
                key={friend.id}
                avatarUrl={friend.avatarUrl}
                name={friend.name}
                status={friend.status}
                description={statusKo[friend.status]}
                statusColor='black'
                iconType='response'
              />
            ))}
          </div>
        )}

        {requestPendingFriends.length > 0 && (
          <div className='flex flex-col gap-2'>
            <div className='text-discord-font-color-muted text-xs font-semibold mb-2'>
              요청 — {requestPendingFriends.length}
            </div>
            {requestPendingFriends.map((friend) => (
              <UserListItem
                key={friend.id}
                avatarUrl={friend.avatarUrl}
                name={friend.name}
                status={friend.status}
                description={statusKo[friend.status]}
                statusColor='black'
                iconType='request'
              />
            ))}
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
