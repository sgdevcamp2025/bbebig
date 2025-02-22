import { ChannelMessage, SearchMessage } from '@/types/message'

// Search 서버 - Search Controller
export type SearchOption = 'CONTENT'

export interface SearchChattingChannelRequestSchema {
  serverId: number
  keyword: string
  senderId: number
  afterDate: string
  beforeDate: string
  exactDate: string
  options: SearchOption[]
  page: number
  size: number
}

export interface SearchChattingChannelResponseSchema {
  serverId: number
  totalCount: number
  messages: SearchMessage[]
}

// Search 서버 - history Controller
export interface GetHistoryChattingMessagesRequestSchema {
  serverId: number
  channelId: number
  messageId?: number
  limit?: number
}

export interface GetHistoryChattingMessageResponse {
  id: number
  serverId: number
  channelId: number
  lastMessageId: number
  totalCount: number
  messages: ChannelMessage[]
}

export interface GetSingleServerUnreadCountRequestSchema {
  serverId: number
  memberId: number
}

export interface GetSingleServerUnreadCountResponseSchema {
  serverId: number
  channels: {
    channelId: number
    unreadCount: number
  }[]
  totalUnread: number
}

export interface GetAllServersUnreadCountRequestSchema {
  memberId: number
}

export interface GetAllServerUnreadCountResponseSchema {
  memberId: number
  totalServerCount: number
  serverInfos: {
    serverId: number
    channels: {
      channelId: number
      unreadCount: number
    }[]
    totalUnread: number
  }[]
}
