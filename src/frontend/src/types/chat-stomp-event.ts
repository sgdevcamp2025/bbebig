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

export type PersonalNotificationEvent = FriendActionEvent | FriendPresenceEvent | ServerUnreadEvent

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

// 친구 관련 이벤트 (친구 요청, 수신, 수락, 거절 등)
export interface FriendActionEvent {
  memberId: number
  type: 'FRIEND_ACTION'
  friendId: number
  friendMemberId: number
  friendNickName: string
  friendAvatarUrl: string
  friendBannerUrl: string
  status: 'RECEIVE' | 'PENDING' | 'ACCEPT' | 'REJECT' | 'CANCEL' | 'DELETE' | 'UPDATE'
}

// 친구 활성화 상태 이벤트
export interface FriendPresenceEvent {
  memberId: number
  type: 'FRIEND_PRESENCE'
  friendId: number
  friendMemberId: number
  globalStatus: CustomPresenceStatus
}

// 서버 안 읽은 메세지 이벤트
export interface ServerUnreadEvent {
  memberId: number
  type: 'SERVER_UNREAD'
  serverId: number
  channelId: number
  sequence: number
  status: 'UNREAD' | 'READ'
  unreadCount: number
}
