interface User {
  id: number
  name: string
  email: string
  avatarUrl: string
  bannerUrl: string
  customPresenceStatus: CustomPresenceStatus
  introduce: {
    text: string
    emoji: string
  }
}

type CustomPresenceStatus = 'ONLINE' | 'OFFLINE' | 'NOT_DISTURB' | 'INVISIBLE'

export { type CustomPresenceStatus, type User }
