import { ServerChannelList } from '@/types/channel'
import { Outlet, useNavigate, useParams } from 'react-router'
import ServerSidebar from './components/server-side-bar'
import useStatusBarStore from '@/stores/use-status-bar-store'
import StatusSideBar from './components/status-side-bar'
import { User } from '@/types/user'

const myChannelList: ServerChannelList = {
  1: [
    {
      id: 1,
      name: '채팅 채널',
      position: 0,
      channels: [
        { id: 1, name: '일반', type: 'VOICE', position: 0, privateStatus: false },
        { id: 2, name: '공지사항', type: 'TEXT', position: 1, privateStatus: false }
      ]
    }
  ],
  2: [
    {
      id: 2,
      name: '개발 채널',
      position: 0,
      channels: [
        { id: 3, name: '프론트엔드', type: 'TEXT', position: 0, privateStatus: false },
        { id: 4, name: '백엔드', type: 'TEXT', position: 1, privateStatus: false }
      ]
    }
  ]
}

const userList: User[] = [
  {
    id: '1',
    name: '김예지',
    avatarUrl: '/image/common/default-avatar.png',
    bannerUrl: '/image/common/default-avatar.png',
    customPresenceStatus: 'ONLINE',
    introduction: '안뇽',
    introductionEmoji: '👋',
    email: 'yeji@gmail.com'
  },
  {
    id: '2',
    name: '이지형',
    avatarUrl: '/image/common/default-avatar.png',
    bannerUrl: '/image/common/default-avatar.png',
    customPresenceStatus: 'OFFLINE',
    introduction: '하이루',
    introductionEmoji: '👋',
    email: 'jihyung@gmail.com'
  },
  {
    id: '2',
    name: '이소은',
    avatarUrl: '/image/common/default-avatar.png',
    bannerUrl: '/image/common/default-avatar.png',
    customPresenceStatus: 'NOT_DISTURB',
    introduction: '뇽안',
    introductionEmoji: '👋',
    email: 'soeun@gmail.com'
  }
]

function ServerLayout() {
  const { serverId, channelId } = useParams<{ serverId: string; channelId: string }>()
  const { isStatusBarOpen } = useStatusBarStore()
  const navigate = useNavigate()
  const categories =
    serverId && myChannelList[Number(serverId)] ? myChannelList[Number(serverId)] : []
  const handleChannelSelect = (selectedChannelId: number) => {
    navigate(`/channels/${serverId}/${selectedChannelId}`)
  }

  return (
    <div className='flex'>
      <ServerSidebar
        serverName={`서버 ${serverId}`}
        categories={categories}
        onChannelSelect={handleChannelSelect}
        selectedChannelId={channelId}
      />

      <Outlet />

      {isStatusBarOpen && <StatusSideBar users={userList} />}
    </div>
  )
}

export default ServerLayout
