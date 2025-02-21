import { ChangeEvent, useState } from 'react'

import SearchInput from '@/components/search-input'
import UserListItem from '@/components/user-list-item'
import { statusKo } from '@/constants/status'
import useGetFriendList from '@/hooks/queries/user/useGetFriendList'
import { CustomPresenceStatus } from '@/types/user'

function OnlineFriends() {
  const { friends = [] } = useGetFriendList()
  const [searchValue, setSearchValue] = useState('')
  const filteredFriends = friends.filter((friend) =>
    friend.memberName.toLowerCase().includes(searchValue.toLowerCase())
  )
  const handleSearch = (e: ChangeEvent<HTMLInputElement>) => {
    setSearchValue(e.target.value)
  }

  const handleClear = () => {
    setSearchValue('')
  }

  return (
    <div className='flex flex-col gap-6 p-4'>
      <div>
        <SearchInput
          value={searchValue}
          onChange={handleSearch}
          handleClear={handleClear}
          placeholder='검색하기'
        />
      </div>

      <div className='flex flex-col gap-2'>
        <div className='text-discord-font-color-muted text-xs font-semibold mb-2'>
          온라인 — {filteredFriends.filter((friend) => friend.globalStatus === 'ONLINE').length}
        </div>
        {filteredFriends.map((friend) => (
          <UserListItem
            key={friend.memberId}
            id={friend.memberId}
            avatarUrl={friend.memberAvatarUrl ?? '/image/common/default-avatar.png'}
            name={friend.memberName}
            status={friend.actualStatus as CustomPresenceStatus}
            description={statusKo[friend.actualStatus as CustomPresenceStatus]}
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
export default OnlineFriends
