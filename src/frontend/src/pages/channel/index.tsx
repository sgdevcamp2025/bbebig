import { useLayoutEffect, useMemo } from 'react'
import { useNavigate, useParams } from 'react-router-dom'

import { useGetServerInfo } from '@/hooks/queries/server/useGetServerInfo'
import { useGetServerMember } from '@/hooks/queries/server/useGetServerMember'
import useGetSelfUser from '@/hooks/queries/user/useGetSelfUser'

import TextComponent from './components/text'
import VideoComponent from './components/voice'

function ChannelPage() {
  const { serverId, channelId } = useParams()
  if (!serverId || !channelId) {
    throw new Error('serverId or channelId is required')
  }
  const navigate = useNavigate()
  const selfUser = useGetSelfUser()
  const serverInfo = useGetServerInfo(serverId)
  const serverMemberList = useGetServerMember(serverId)

  const currentUser = {
    memberId: selfUser.id,
    nickName: selfUser.name,
    avatarUrl: selfUser.avatarUrl,
    bannerUrl: selfUser.bannerUrl,
    globalStatus: selfUser.customPresenceStatus
  }

  const targetUser = serverMemberList.serverMemberInfoList.find(
    (user) => user.memberId !== selfUser.id
  ) ?? {
    memberId: 0,
    nickName: '',
    avatarUrl: '',
    bannerUrl: '',
    globalStatus: 'OFFLINE'
  }

  const currentChannel = useMemo(
    () => serverInfo.channelInfoList.find((channel) => channel.channelId === Number(channelId)),
    [serverInfo.channelInfoList, channelId]
  )

  useLayoutEffect(() => {
    if (!currentChannel) {
      navigate(`/channels/${serverId}/${serverInfo.channelInfoList[0].channelId}`)
    }
  }, [currentChannel, serverInfo.channelInfoList, navigate, serverId])

  if (!currentChannel) {
    return null
  }

  if (currentChannel.channelType === 'CHAT') {
    return (
      <TextComponent
        channelId={Number(channelId)}
        channelName={currentChannel.channelName}
        serverId={Number(serverId)}
        currentUser={currentUser}
        targetUser={targetUser}
      />
    )
  }

  if (currentChannel.channelType === 'VOICE') {
    return (
      <VideoComponent
        channelId={Number(channelId)}
        serverName={serverInfo.serverName}
        channelName={currentChannel.channelName}
        users={[currentUser, targetUser].filter(Boolean)}
      />
    )
  }
}

export default ChannelPage
