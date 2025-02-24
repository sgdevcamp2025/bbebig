import ChatArea from '@/components/chat-area'
import { useGetHistoryChattingMessages } from '@/hooks/queries/search/useGetHistoryChattingMessages'
import { ChatUser } from '@/types/user'

interface Props {
  channelId: number
  serverId: number
  currentUser: ChatUser
  targetUsers: ChatUser[]
  channelName: string
}

function TextComponent({ channelId, serverId, currentUser, targetUsers, channelName }: Props) {
  const historyMessages = useGetHistoryChattingMessages({
    serverId: serverId,
    channelId: channelId,
    limit: 20
  })

  console.log('TextComponent - historyMessages', historyMessages)

  return (
    <div className='flex-1 flex flex-col h-screen'>
      <ChatArea
        chatKey={channelId}
        serverId={serverId}
        users={{
          currentUser: currentUser,
          targetUsers: targetUsers
        }}
        channelName={channelName}
        historyMessages={historyMessages.messages}
      />
    </div>
  )
}

export default TextComponent
