import { useEffect } from 'react'
import { Outlet, useParams } from 'react-router-dom'

import { useGetServerInfo } from '@/hooks/queries/server/useGetServerInfo'
import useChattingStomp from '@/hooks/store/use-chatting-stomp'
import useStatusBarStore from '@/stores/use-status-bar-store'

import { ServerSideBar } from './components/server-side-bar'
import { StatusSideBar } from './components/status-side-bar'

function ServerLayout() {
  const { serverId, channelId } = useParams<{ serverId: string; channelId: string }>()
  const { isStatusBarOpen } = useStatusBarStore()
  const { publishToChannelEnter, publishToChannelLeave } = useChattingStomp()

  if (!serverId || !channelId) {
    throw new Error('serverId or channelId is required')
  }

  const serverData = useGetServerInfo(serverId)

  const { channelInfoList } = serverData

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
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [serverId, channelId])

  return (
    <div className='flex h-screen w-full'>
      <ServerSideBar serverId={serverId} />

      <main className='flex-1 bg-discord-gray-600 overflow-hidden'>
        <Outlet />
      </main>

      {isStatusBarOpen && <StatusSideBar serverId={serverId} />}
    </div>
  )
}

export default ServerLayout
