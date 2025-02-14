import { Outlet, useNavigate, useParams } from 'react-router'

import { GetServerListResponseSchema } from '@/apis/schema/types/service'
import useStatusBarStore from '@/stores/use-status-bar-store'
import { ChannelUser } from '@/types/server'

import ServerSidebar from './components/server-side-bar'
import StatusSideBar from './components/status-side-bar'

const mockServerData: GetServerListResponseSchema = {
  serverId: 1,
  serverName: '서버 이름',
  ownerId: 1,
  serverImageUrl: '/image/common/default-avatar.png',
  categoryInfoList: [
    {
      categoryId: 1,
      categoryName: 'test-category',
      position: 1,
      channelInfoList: [
        {
          channelId: 1,
          categoryId: 1,
          position: 1,
          channelName: 'test-channel',
          channelType: 'TEXT',
          privateStatus: false
        }
      ]
    },
    {
      categoryId: 2,
      categoryName: 'test-category2',
      position: 2,
      channelInfoList: [
        {
          channelId: 2,
          categoryId: 2,
          position: 2,
          channelName: 'test-channel2',
          channelType: 'TEXT',
          privateStatus: false
        }
      ]
    },
    {
      categoryId: 3,
      categoryName: 'test-category3',
      position: 3,
      channelInfoList: [
        {
          channelId: 3,
          categoryId: 3,
          position: 3,
          channelName: 'test-channel3',
          channelType: 'TEXT',
          privateStatus: false
        },
        {
          channelId: 4,
          categoryId: 3,
          position: 4,
          channelName: 'test-channel4',
          channelType: 'TEXT',
          privateStatus: false
        }
      ]
    }
  ]
}

const channelUsers = [
  {
    id: '1',
    name: '김예지',
    avatarUrl: '/image/common/default-avatar.png',
    bannerUrl: '/image/common/default-background.png',
    customPresenceStatus: 'ONLINE',
    introduction: '안뇽',
    introductionEmoji: '👋',
    email: 'yeji@gmail.com',
    includeChannelId: [1, 3, 4]
  },
  {
    id: '2',
    name: '이지형',
    avatarUrl: '/image/common/default-avatar.png',
    bannerUrl: '/image/common/default-background.png',
    customPresenceStatus: 'OFFLINE',
    introduction: '하이루',
    introductionEmoji: '👋',
    email: 'jihyung@gmail.com',
    includeChannelId: [1, 2, 3, 4]
  }
] as ChannelUser[]

function ServerLayout() {
  const { serverId, channelId } = useParams<{ serverId: string; channelId: string }>()
  const { isStatusBarOpen } = useStatusBarStore()

  if (!serverId) {
    throw new Error('serverId is required')
  }

  // const { data: serverData } = useSuspenseQuery({
  //   queryKey: ['serverData', serverId],
  //   queryFn: () => serviceService.getServersList({ serverId })
  // })

  const navigate = useNavigate()

  const currentChannelUsers = channelUsers.filter((user) =>
    user.includeChannelId.includes(Number(channelId))
  )

  const handleChannelSelect = (selectedChannelId: number) => {
    navigate(`/channels/${serverId}/${selectedChannelId}`)
  }

  const { serverId: responseServerId, serverName, categoryInfoList } = mockServerData

  return (
    <div className='flex h-screen w-full'>
      <ServerSidebar
        serverId={responseServerId}
        serverName={serverName}
        categories={categoryInfoList}
        onChannelSelect={handleChannelSelect}
        selectedChannelId={channelId}
      />

      <main className='flex-1 bg-discord-gray-600 overflow-hidden'>
        <Outlet />
      </main>

      {isStatusBarOpen && <StatusSideBar channelUserList={currentChannelUsers} />}
    </div>
  )
}

export default ServerLayout
