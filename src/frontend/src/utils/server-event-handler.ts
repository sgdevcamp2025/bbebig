import { useChatStore } from '@/hooks/store/use-chat-store'
import { useStatusStore } from '@/hooks/store/use-status-store'
import { ServerSubscribeResponse } from '@/types/chat-stomp-event'
import { CustomPresenceStatus } from '@/types/user'

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
      useStatusStore.setState((state) => ({
        channelMemberList: state.channelMemberList.map((user) =>
          user.memberId === message.memberId
            ? { ...user, globalStatus: message.globalStatus as CustomPresenceStatus }
            : user
        )
      }))
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
