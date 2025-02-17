import { Outlet, useNavigate, useParams } from 'react-router'

import { useGetServerInfo } from '@/hooks/queries/server/useGetServerInfo'
import useGetServerMember from '@/hooks/queries/server/useGetServerMember'
import useStatusBarStore from '@/stores/use-status-bar-store'

import ServerSidebar from './components/server-side-bar'
import StatusSideBar from './components/status-side-bar'

function ServerLayout() {
  const { serverId, channelId } = useParams<{ serverId: string; channelId: string }>()
  const { isStatusBarOpen } = useStatusBarStore()

  const navigate = useNavigate()

  if (!serverId || !channelId) {
    throw new Error('serverId or channelId is required')
  }

  const serverData = useGetServerInfo(serverId)

  const serverMemebersData = useGetServerMember(serverId)

  const currentChannelUsers = serverMemebersData.result.serverMemberInfoList.map((member) => ({
    memberId: member.memberId,
    name: member.nickname,
    avatarUrl: member.avatarUrl
  }))

  const handleChannelSelect = (selectedChannelId: number) => {
    navigate(`/channels/${serverId}/${selectedChannelId}`)
  }

  const { serverName, categoryInfoList, channelInfoList } = serverData

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
