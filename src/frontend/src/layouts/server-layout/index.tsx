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
    if (!channelInfoList || channelInfoList.length === 0) return

    const channel = channelInfoList.find((channel) => channel.channelId === Number(channelId))

    if (!channel) return

    publishToChannelEnter({
      channelType: channel.channelType as 'CHAT' | 'VOICE',
      serverId: Number(serverId),
      channelId: Number(channelId),
      type: 'ENTER'
    })

    return () => {
      publishToChannelLeave({
        channelType: channel.channelType as 'CHAT' | 'VOICE',
        serverId: Number(serverId),
        channelId: Number(channelId),
        type: 'LEAVE'
      })
    }
  }, [channelId, channelInfoList, serverId])

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
