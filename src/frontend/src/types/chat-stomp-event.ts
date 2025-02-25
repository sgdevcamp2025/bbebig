import { ChannelType } from './server'
import { CustomPresenceStatus } from './user'

export type ServerSubscribeResponse =
  | ServerChannelEvent
  | ServerCategoryEvent
  | ServerActionEvent
  | ServerMemberActionEvent
  | ServerMemberPresenceEvent
  | ChattingMessageEvent

export interface BaseServerEvent {
  serverId: number
  type: string
}

// 🛜 SUBSCRIBE
// 서버 채널 생성/수정/삭제 이벤트
export interface ServerChannelEvent extends BaseServerEvent {
  type: 'SERVER_CHANNEL'
  categoryId: number
  channeId: number
  channelType: ChannelType
  order: number | null
  status: 'CREATE' | 'UPDATE' | 'DELETE'
}
// 서버 카테고리 생성/수정/삭제 이벤트
export interface ServerCategoryEvent extends BaseServerEvent {
  serverId: number
  type: 'SERVER_CATEGORY'
  categoryId: number
  categoryName: string | null
  order: number | null
  status: 'CREATE' | 'UPDATE' | 'DELETE'
}

// 서버 수정/삭제 이벤트
export interface ServerActionEvent extends BaseServerEvent {
  type: 'SERVER_ACTION'
  serverName?: string | null
  profileImageUrl?: string | null
  status: 'UPDATE' | 'DELETE'
}

// 서버 멤버 참여/탈퇴/수정 이벤트
export interface ServerMemberActionEvent extends BaseServerEvent {
  type: 'SERVER_MEMBER_ACTION'
  memberId: number
  nickName?: string | null
  avatarUrl?: string | null
  bannerUrl?: string | null
  status: 'JOIN' | 'LEAVE' | 'UPDATE'
}

// 서버 멤버 온/오프라인 상태 업데이트 이벤트
export interface ServerMemberPresenceEvent extends BaseServerEvent {
  type: 'SERVER_MEMBER_PRESENCE'
  memberId: number
  serverId: number
  actualStatus: CustomPresenceStatus
  globalStatus: CustomPresenceStatus
}

// 서버 채팅 메시지 이벤트 (STOMP에서 받는 MESSAGE)
export interface ChattingMessageEvent extends BaseServerEvent {
  chatType: 'CHANNEL' | 'DM'
  messageType: 'TEXT'
  type: 'MESSAGE_CREATE'
  channelId: number
  sequence?: number
  sendMemberId?: number
  content: string
  createdAt?: string
  updatedAt?: string
}

// 채널 방문/떠남 이벤트
export interface ChannelVisitEventRequest extends BaseServerEvent {
  type: 'ENTER' | 'LEAVE'
  memberId?: number
  channelType: ChannelType
  channelId: number
  lastReadMessageId?: string
  eventTime?: string
}
