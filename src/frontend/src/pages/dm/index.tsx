import { useParams } from 'react-router'

import { Friend } from '@/types/friend'

import DmArea from './components/dm-area'
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

  const selectedFriend = DUMMY_FRIENDS.find((friend) => friend.id === Number(friendId))

  if (!selectedFriend) {
    return <div>친구를 찾을 수 없습니다.</div>
  }

  return (
    <div className='flex flex-col h-screen'>
      <DmHeader friend={selectedFriend} />
      <DmArea friend={selectedFriend} />
    </div>
  )
}

export default DmPage
