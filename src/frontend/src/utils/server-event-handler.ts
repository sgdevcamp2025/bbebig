import { useChatStore } from '@/stores/use-chat-store'
import { useStatusStore } from '@/stores/use-status-store'
import { ServerSubscribeResponse } from '@/types/chat-stomp-event'
import { CustomPresenceStatus } from '@/types/user'

export const handleServerEvent = (message: ServerSubscribeResponse) => {
  const { addMessage } = useChatStore.getState()

  switch (message.type) {
    case 'SERVER_CHANNEL':
      break
    case 'SERVER_CATEGORY':
      break
      break
    case 'SERVER_ACTION':
      break
    case 'SERVER_MEMBER_ACTION':
      break
      break
    case 'SERVER_MEMBER_PRESENCE':
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
      addMessage(message.channelId, {
        ...message,
        sendMemberId: message.sendMemberId
      })
      break
    default:
      break
  }
}
