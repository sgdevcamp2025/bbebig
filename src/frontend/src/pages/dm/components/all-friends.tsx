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
    status: 'ONLINE'
  },
  {
    id: 2,
    avatarUrl: '/image/common/default-avatar.png',
    name: '김예지',
    status: 'OFFLINE'
  },
  {
    id: 3,
    avatarUrl: '/image/common/default-avatar.png',
    name: '서정우',
    status: 'OFFLINE'
  }
]

function AllFriends() {
  const [searchValue, setSearchValue] = useState('')
  const filteredFriends = DUMMY_FRIENDS.filter((friend) =>
    friend.name.toLowerCase().includes(searchValue.toLowerCase())
  )

  const handleSearch = (value: string) => {
    setSearchValue(value)
  }
  return (
    <div className='flex flex-col gap-6 p-4'>
      <div>
        <SearchInput
          onSearch={handleSearch}
          placeholder='검색'
        />
      </div>

      <div className='flex flex-col gap-2'>
        {filteredFriends.length > 0 && (
          <div className='text-discord-font-color-muted text-xs font-semibold mb-2'>
            모든 친구 — {filteredFriends.length}
          </div>
        )}
        {filteredFriends.map((friend) => (
          <UserListItem
            key={friend.id}
            avatarUrl={friend.avatarUrl}
            name={friend.name}
            status={friend.status}
            description={statusKo[friend.status]}
            statusColor='black'
            iconType='default'
          />
        ))}

        {filteredFriends.length === 0 && (
          <div className='text-discord-font-color-muted text-sm'>검색 결과가 없습니다.</div>
        )}
      </div>
    </div>
  )
}

export default AllFriends
