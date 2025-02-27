import { useServerUnreadStore } from '@/hooks/store/use-server-unread-store'
import { PersonalNotificationEvent } from '@/types/chat-stomp-event'

export const handlePersonalNotificationEvent = (message: PersonalNotificationEvent) => {
  switch (message.type) {
    case 'FRIEND_ACTION':
      break
    case 'FRIEND_PRESENCE':
      break
    case 'SERVER_UNREAD': {
      useServerUnreadStore
        .getState()
        .updateUnreadCount(message.serverId, message.channelId, message.unreadCount)
      break
    }
  }
}
