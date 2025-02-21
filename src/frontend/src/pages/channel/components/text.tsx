import { IMessage } from '@stomp/stompjs'
import { useEffect, useState } from 'react'

import ChatArea from '@/components/chat-area'
import useChattingStomp from '@/hooks/store/use-chatting-stomp'
import { ChatUser } from '@/types/user'

interface Props {
  channelId: number
  serverId: number
  currentUser: ChatUser
  targetUser: ChatUser
  channelName: string
}

function TextComponent({ channelId, currentUser, targetUser, channelName }: Props) {
  const { connect, subscribe } = useChattingStomp()
  const [, setMessages] = useState<IMessage[]>([])

  useEffect(() => {
    connect()

    subscribe(`/topic/channel/${channelId}`, (message) => {
      console.log('[📩] 채널 메시지 수신:', message)
      setMessages((prev) => [...prev, message])
    })
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [channelId])
  return (
    <div className='flex-1 flex flex-col h-screen'>
      <ChatArea
        chatKey={channelId}
        users={{
          currentUser: currentUser,
          targetUser: targetUser
        }}
        channelName={channelName}
      />
    </div>
  )
}

export default TextComponent
