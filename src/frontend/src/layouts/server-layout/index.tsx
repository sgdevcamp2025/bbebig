import { useEffect, useRef } from 'react'
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
  const prevChannelIdRef = useRef(channelId)

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
    if (prevChannelIdRef.current && prevChannelIdRef.current !== channelId) {
      publishToChannelLeave({
        channelType: 'CHANNEL',
        serverId: Number(serverId),
        channelId: Number(prevChannelIdRef.current),
        type: 'LEAVE'
      })

      publishToChannelEnter({
        channelType: 'CHANNEL',
        serverId: Number(serverId),
        channelId: Number(channelId),
        type: 'ENTER'
      })
    }

    prevChannelIdRef.current = channelId

    return () => {
      if (channelId && !prevChannelIdRef.current) {
        publishToChannelLeave({
          channelType: 'CHANNEL',
          serverId: Number(serverId),
          channelId: Number(channelId),
          type: 'LEAVE'
        })
      }
    }
  }, [channelId, serverId])

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
