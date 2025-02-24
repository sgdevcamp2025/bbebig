import { useEffect } from 'react'
import { Outlet, useNavigate, useParams } from 'react-router-dom'

import { useGetServerInfo } from '@/hooks/queries/server/useGetServerInfo'
import { useGetServerMember } from '@/hooks/queries/server/useGetServerMember'
import useChattingStomp from '@/hooks/store/use-chatting-stomp'
import useStatusBarStore from '@/stores/use-status-bar-store'

import ServerSidebar from './components/server-side-bar'
import StatusSideBar from './components/status-side-bar'

function ServerLayout() {
  const { serverId, channelId } = useParams<{ serverId: string; channelId: string }>()
  const { isStatusBarOpen } = useStatusBarStore()
  const { publishToChannelEnter, publishToChannelLeave } = useChattingStomp()

  const navigate = useNavigate()

  if (!serverId || !channelId) {
    throw new Error('serverId or channelId is required')
  }

  const serverData = useGetServerInfo(serverId)

  const serverMemebersData = useGetServerMember(serverId)

  const currentChannelUsers = serverMemebersData.serverMemberInfoList.map((member) => ({
    memberId: member.memberId,
    nickName: member.nickName,
    avatarUrl: member.avatarUrl,
    bannerUrl: member.bannerUrl,
    globalStatus: member.globalStatus
  }))

  const handleChannelSelect = (selectedChannelId: number) => {
    navigate(`/channels/${serverId}/${selectedChannelId}`)
  }

  const { serverName, categoryInfoList, channelInfoList } = serverData

  const categories = categoryInfoList.map((category) => ({
    ...category,
    channelInfoList: channelInfoList.filter((channel) => channel.categoryId === category.categoryId)
  }))

  useEffect(() => {
    console.log(`[🚪] 채널 ${channelId} 자동 입장`)
    publishToChannelEnter({
      channelType: channelInfoList.find((channel) => channel.channelId === Number(channelId))
        ?.channelType as 'CHAT' | 'VOICE',
      serverId: Number(serverId),
      channelId: Number(channelId),
      type: 'ENTER'
    })

    return () => {
      console.log(`[🚪] 채널 ${channelId} 퇴장`)
      publishToChannelLeave({
        channelType: channelInfoList.find((channel) => channel.channelId === Number(channelId))
          ?.channelType as 'CHAT' | 'VOICE',
        serverId: Number(serverId),
        channelId: Number(channelId),
        type: 'LEAVE'
      })
    }
  }, [serverId, channelId])

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
