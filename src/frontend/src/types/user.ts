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

type CustomPresenceStatus = 'ONLINE' | 'AWAY' | 'DND' | 'INVISIBLE'

export { type CustomPresenceStatus, type User }
