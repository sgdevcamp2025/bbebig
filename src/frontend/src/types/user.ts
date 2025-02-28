interface User {
  id: number
  name: string
  nickname: string
  email: string
  birthdate: string
  avatarUrl: string | null
  bannerUrl: string | null
  introduce: string | null
  lastAccessAt: string
  customPresenceStatus: CustomPresenceStatus
}

export interface ChatUser {
  memberId: number
  nickName: string
  avatarUrl: string | null
  bannerUrl: string | null
  globalStatus: CustomPresenceStatus
}

type CustomPresenceStatus = 'ONLINE' | 'AWAY' | 'DND' | 'INVISIBLE' | 'OFFLINE'

export { type CustomPresenceStatus, type User }
