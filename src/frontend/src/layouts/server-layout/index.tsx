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

  const currentChannelUsers = serverMemebersData.result.serverMemberInfoList.map((member) => ({
    memberId: member.memberId,
    name: member.nickname,
    avatarUrl: member.avatarUrl
  }))

  const handleChannelSelect = (selectedChannelId: number) => {
    navigate(`/channels/${serverId}/${selectedChannelId}`)
  }

  const { serverName, categoryInfoList, channelInfoList } = serverData.result

  const categories = categoryInfoList.map((category) => ({
    ...category,
    channelInfoList: channelInfoList.filter((channel) => channel.categoryId === category.categoryId)
  }))

  return (
    <div className='flex h-screen w-full'>
      <ServerSidebar
        serverName={serverName}
        categories={categories}
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
