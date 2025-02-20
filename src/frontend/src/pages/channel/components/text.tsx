import { IMessage } from '@stomp/stompjs'
import { useEffect, useState } from 'react'

import ChatArea from '@/components/chat-area'
import useChattingStomp from '@/hooks/store/use-chatting-stomp'

interface Props {
  channelId: number
  serverId: number
}

function TextComponent({ channelId }: Props) {
  const { connect, subscribe, sendMessage } = useChattingStomp()
  const [messages, setMessages] = useState<IMessage[]>([])
  useEffect(() => {
    connect()

    subscribe(`/topic/channel/${channelId}`, (message) => {
      console.log('[📩] 채널 메시지 수신:', message)
      setMessages((prev) => [...prev, message])
    })
  }, [channelId])
  return (
    <div className='flex-1 flex flex-col h-screen'>
      <ChatArea
        channelId={channelId}
        isVoice={false}
      />
    </div>
  )
}

export default TextComponent
