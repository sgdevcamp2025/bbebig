// 🛜 SUBSCRIBE
// 서버 채널 생성/수정/삭제 이벤트
export interface ServerChannelEvent {
  serverId: number
  type: 'SEVER_CHANNEL'
  categoryId: number
  channeId: number
  channelType: 'CHAT' | 'VOICE'
  order: number | null
  status: 'CREATE' | 'UPDATE' | 'DELETE'
}

// 서버 카테고리 생성/수정/삭제 이벤트
export interface ServerCategoryEvent {
  serverId: number
  type: 'SEVER_CATEGORY'
  categoryId: number
  categoryName: string | null
  order: number | null
  status: 'CREATE' | 'UPDATE' | 'DELETE'
}

// 서버 수정/삭제 이벤트
export interface ServerActionEvent {
  serverId: number
  type: 'SEVER_ACTION'
  serverName: string | null
  profileImageUrl: string | null
  status: 'UPDATE' | 'DELETE'
}

// 서버 멤버 참여/탈퇴/수정 이벤트
export interface ServerMemberActionEvent {
  serverId: number
  type: 'SERVER_MEMBER_ACTION'
  memberId: number
  nickName: string | null
  avatarUrl: string | null
  bannerUrl: string | null
  status: 'JOIN' | 'LEAVE' | 'UPDATE'
}

// 서버 멤버 온/오프라인 상태 업데이트 이벤트
export interface ServerMemberPresenceEvent {
  serverId: number
  type: 'SERVER_MEMBER_PRESENCE'
  memberId?: number
  actualStatus: 'ONLINE' | 'OFFLINE'
  globalStatus: 'ONLINE' | 'OFFLINE' | 'AWAY' | 'BUSY' | 'DND'
}

// 서버 채팅 메시지 이벤트
export interface ChatMessageRequest {
  chatType: 'CHANNEL' | 'DM'
  messageType: 'TEXT'
  type: 'MESSAGE_CREATE' | 'MESSAGE_UPDATE' | 'MESSAGE_DELETE'
  serverId: number
  channelId: number
  sendMemberId?: number
  content: string
  createdAt?: Date
  updatedAt?: Date
}

// 채널 방문/떠남 이벤트
export interface ChannelVisitEventRequest {
  memberId?: number
  type: 'ENTER' | 'LEAVE'
  channelType: 'CHANNEL' | 'DM'
  serverId: number
  channelId: number
  lastReadMessageId?: string
  eventTime?: string
}
