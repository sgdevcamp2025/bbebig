import { useChatStore } from '@/hooks/store/use-chat-store'
import { ServerSubscribeResponse } from '@/types/chat-stomp-event'

export const handleServerEvent = (message: ServerSubscribeResponse) => {
  const { addMessage } = useChatStore.getState()

  switch (message.type) {
    case 'SERVER_CHANNEL':
      console.log('SERVER_CHANNEL', message)
      break
    case 'SERVER_CATEGORY':
      console.log('SERVER_CATEGORY', message)
      break
    case 'SERVER_ACTION':
      console.log('SERVER_ACTION', message)
      break
    case 'SERVER_MEMBER_ACTION':
      console.log('SERVER_MEMBER_ACTION', message)
      break
    case 'SERVER_MEMBER_PRESENCE':
      console.log('SERVER_MEMBER_PRESENCE', message)
      break
    case 'MESSAGE_CREATE':
      if (!message.sendMemberId) return
      console.log('MESSAGE_CREATE', message)
      addMessage(message.channelId, {
        ...message,
        sendMemberId: message.sendMemberId
      })
      break
    default:
      console.log(message)
      break
  }
}
