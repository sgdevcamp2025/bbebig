import { Outlet, useNavigate, useParams } from 'react-router'

import { GetServerListResponseSchema } from '@/apis/schema/types/service'
import useStatusBarStore from '@/stores/use-status-bar-store'

import ServerSidebar from './components/server-side-bar'
import StatusSideBar from './components/status-side-bar'

const mockServerData = {
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
          privateStatus: false,
          channelMemberList: [1, 2, 3]
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
          privateStatus: false,
          channelMemberList: [1, 3]
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
          privateStatus: false,
          channelMemberList: [3, 4]
        },
        {
          channelId: 4,
          categoryId: 3,
          position: 4,
          channelName: 'test-channel4',
          channelType: 'TEXT',
          privateStatus: false,
          channelMemberList: [4]
        }
      ]
    }
  ],
  serverMemberList: [
    {
      memberId: 1,
      nickname: '서정우',
      profileImageUrl: '/image/common/default-avatar.png',
      joinAt: '2025-02-14T17:00:00.000Z',
      customPresenceStatus: 'ONLINE'
    },
    {
      memberId: 2,
      nickname: '이지형',
      profileImageUrl: null,
      joinAt: '2025-02-14T17:00:00.000Z',
      customPresenceStatus: 'INVISIBLE'
    },
    {
      memberId: 3,
      nickname: '김예지',
      profileImageUrl: '/image/common/default-avatar.png',
      joinAt: '2025-02-14T17:00:00.000Z',
      customPresenceStatus: 'ONLINE'
    },
    {
      memberId: 4,
      nickname: '백도현',
      profileImageUrl: '/image/common/default-avatar.png',
      joinAt: '2025-02-14T17:00:00.000Z',
      customPresenceStatus: 'OFFLINE'
    },
    {
      memberId: 5,
      nickname: '이소은',
      profileImageUrl: '/image/common/default-avatar.png',
      joinAt: '2025-02-14T17:00:00.000Z',
      customPresenceStatus: 'OFFLINE'
    }
  ]
} satisfies GetServerListResponseSchema

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

  const serverData = {
    result: mockServerData satisfies GetServerListResponseSchema
  }

  const navigate = useNavigate()

  const currentChannelUsers = serverData.result.categoryInfoList.flatMap((category) =>
    category.channelInfoList.flatMap((channel) =>
      channel.channelMemberList
        .map((memberId) =>
          serverData.result.serverMemberList.find((user) => user.memberId === memberId)
        )
        .filter((user): user is NonNullable<typeof user> => Boolean(user))
    )
  )

  const handleChannelSelect = (selectedChannelId: number) => {
    navigate(`/channels/${serverId}/${selectedChannelId}`)
  }

  const { serverId: responseServerId, serverName, categoryInfoList } = serverData.result

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
