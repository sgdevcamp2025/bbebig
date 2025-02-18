import { ChangeEvent, useState } from 'react'
import { useParams } from 'react-router'

import ChatArea from '@/components/chat-area'
import CommonHeader from '@/components/common-header'
import { Friend } from '@/types/friend'

import DmHeader from './components/dm-header'
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

function DmPage() {
  const { friendId } = useParams()
  const [searchValue, setSearchValue] = useState('')
  const selectedFriend = DUMMY_FRIENDS.find((friend) => friend.id === Number(friendId))
  const handleSearch = (e: ChangeEvent<HTMLInputElement>) => {
    setSearchValue(e.target.value)
  }

  const handleClear = () => {
    setSearchValue('')
  }
  if (!selectedFriend) {
    return <div>친구를 찾을 수 없습니다.</div>
  }

  return (
    <div className='flex flex-col h-screen'>
      <CommonHeader
        type='DM'
        searchProps={{
          value: searchValue,
          onChange: handleSearch,
          handleClear: handleClear,
          placeholder: '채팅 검색하기'
        }}>
        <DmHeader friend={selectedFriend} />
      </CommonHeader>
      <ChatArea friend={selectedFriend} />
    </div>
  )
}

export default DmPage
