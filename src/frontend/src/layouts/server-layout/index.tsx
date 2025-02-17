import { useSuspenseQuery } from '@tanstack/react-query'
import { Outlet, useNavigate, useParams } from 'react-router'

import serviceService from '@/apis/service/service'
import useStatusBarStore from '@/stores/use-status-bar-store'

import ServerSidebar from './components/server-side-bar'
import StatusSideBar from './components/status-side-bar'

function ServerLayout() {
  const { serverId, channelId } = useParams<{ serverId: string; channelId: string }>()
  const { isStatusBarOpen } = useStatusBarStore()

  if (!serverId) {
    throw new Error('serverId is required')
  }

  const { data: serverData } = useSuspenseQuery({
    queryKey: ['serverData', serverId],
    queryFn: () => serviceService.getServersList({ serverId })
  })

  const { data: serverMemebersData } = useSuspenseQuery({
    queryKey: ['serverMemebersData', serverId],
    queryFn: () => serviceService.getServerMemebers({ serverId: Number(serverId) })
  })

  const navigate = useNavigate()

  const currentChannelUsers = serverData.result.categoryInfoList.flatMap((category) =>
    category.channelInfoList.flatMap((channel) =>
      channel.channelMemberList
        .map((memberId) =>
          serverMemebersData.result.serverMemberList.find((user) => user.memberId === memberId)
        )
        .filter((user): user is NonNullable<typeof user> => Boolean(user))
    )
  )

  const handleChannelSelect = (selectedChannelId: number) => {
    navigate(`/channels/${serverId}/${selectedChannelId}`)
  }

  const { serverName, categoryInfoList } = serverData.result

  return (
    <div className='flex h-screen w-full'>
      <ServerSidebar
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
