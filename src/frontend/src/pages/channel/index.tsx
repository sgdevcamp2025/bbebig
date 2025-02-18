import { useLayoutEffect, useMemo } from 'react'
import { useNavigate, useParams } from 'react-router'

import { useGetServerInfo } from '@/hooks/queries/server/useGetServerInfo'

import TextComponent from './components/text'
import VideoComponent from './components/voice'

function ChannelPage() {
  const { serverId, channelId } = useParams()
  const navigate = useNavigate()

  if (!serverId || !channelId) {
    throw new Error('serverId or channelId is required')
  }

  const data = useGetServerInfo(serverId)

  const currentChannel = useMemo(
    () => data.channelInfoList.find((channel) => channel.channelId === Number(channelId)),
    [data.channelInfoList, channelId]
  )

  useLayoutEffect(() => {
    if (!currentChannel) {
      navigate(`/channels/${serverId}/${data.channelInfoList[0].channelId}`)
    }
  }, [currentChannel, data.channelInfoList, navigate, serverId])

  if (!currentChannel) {
    return null
  }

  if (currentChannel.channelType === 'CHAT') {
    return <TextComponent channelId={Number(channelId)} />
  }

  if (currentChannel.channelType === 'VOICE') {
    return (
      <VideoComponent
        channelId={Number(channelId)}
        serverName={data.serverName}
        channelName={currentChannel.channelName}
      />
    )
  }
}

export default ChannelPage
