import { Outlet, useNavigate, useParams } from 'react-router'

import useStatusBarStore from '@/stores/use-status-bar-store'
import { ServerChannelList } from '@/types/channel'
import { User } from '@/types/user'

import ServerSidebar from './components/server-side-bar'
import StatusSideBar from './components/status-side-bar'

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

const channelUsers: Record<number, User[]> = {
  1: [
    {
      id: '1',
      name: '김예지',
      avatarUrl: '/image/common/default-avatar.png',
      bannerUrl: '/image/common/default-background.png',
      customPresenceStatus: 'ONLINE',
      introduction: '안뇽',
      introductionEmoji: '👋',
      email: 'yeji@gmail.com'
    },
    {
      id: '2',
      name: '이지형',
      avatarUrl: '/image/common/default-avatar.png',
      bannerUrl: '/image/common/default-background.png',
      customPresenceStatus: 'OFFLINE',
      introduction: '하이루',
      introductionEmoji: '👋',
      email: 'jihyung@gmail.com'
    }
  ],
  2: [
    {
      id: '3',
      name: '이소은',
      avatarUrl: '/image/common/default-avatar.png',
      bannerUrl: '/image/common/default-background.png',
      customPresenceStatus: 'NOT_DISTURB',
      introduction: '뇽안',
      introductionEmoji: '👋',
      email: 'soeun@gmail.com'
    }
  ],
  3: [
    {
      id: '4',
      name: '비비빅',
      avatarUrl: '/image/common/default-avatar.png',
      bannerUrl: '/image/common/default-background.png',
      customPresenceStatus: 'ONLINE',
      introduction: '안녕하세요',
      introductionEmoji: '👋',
      email: 'bbebig@gmail.com'
    }
  ]
}

function ServerLayout() {
  const { serverId, channelId } = useParams<{ serverId: string; channelId: string }>()
  const { isStatusBarOpen } = useStatusBarStore()
  const currentChannelUsers = channelId ? channelUsers[Number(channelId)] || [] : []
  const navigate = useNavigate()
  const categories =
    serverId && myChannelList[Number(serverId)] ? myChannelList[Number(serverId)] : []
  const handleChannelSelect = (selectedChannelId: number) => {
    navigate(`/channels/${serverId}/${selectedChannelId}`)
  }

  return (
    <div className='flex h-screen w-full'>
      <ServerSidebar
        serverName={`서버 ${serverId}`}
        categories={categories}
        onChannelSelect={handleChannelSelect}
        selectedChannelId={channelId}
      />

      <main className='flex-1 bg-discord-gray-600 overflow-hidden'>
        <Outlet />
      </main>

      {isStatusBarOpen && <StatusSideBar users={currentChannelUsers} />}
    </div>
  )
}

export default ServerLayout
