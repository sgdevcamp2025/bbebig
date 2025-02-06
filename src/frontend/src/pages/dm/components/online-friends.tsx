import UserListItem from '@/components/user-list-item'
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
  }
]

function OnlineFriends() {
  return (
    <div className='flex flex-col gap-2 p-4'>
      <div className='text-discord-font-color-muted text-xs font-semibold mb-2'>
        온라인 — {DUMMY_FRIENDS.filter((friend) => friend.status === 'ONLINE').length}
      </div>
      {DUMMY_FRIENDS.map((friend) => (
        <UserListItem
          key={friend.id}
          avatarUrl={friend.avatarUrl}
          name={friend.name}
          status={friend.status}
          description={friend.status}
          statusColor='black'
          size='sm'
          iconType='default'
        />
      ))}
    </div>
  )
}
export default OnlineFriends
