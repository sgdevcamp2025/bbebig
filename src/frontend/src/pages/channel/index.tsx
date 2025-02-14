import { useParams } from 'react-router'

import TextComponent from './components/text'
import VideoComponent from './components/voice'

const CHANNEL_INFO = {
  type: 'VOICE',
  serverName: 'test 서버 이름',
  channelName: 'test 음성 채널'
}

function ChannelPage() {
  const data = CHANNEL_INFO
  const { channelId } = useParams()

  if ('TEXT' === data.type) {
    return <TextComponent channelId={Number(channelId)} />
  }

  if ('VOICE' === data.type) {
    return (
      <VideoComponent
        channelId={Number(channelId)}
        serverName={data.serverName}
        channelName={data.channelName}
      />
    )
  }
}

export default ChannelPage
