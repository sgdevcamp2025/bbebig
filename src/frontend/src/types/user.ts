interface User {
  id: string
  name: string
  email: string
  avatarUrl: string
  bannerUrl: string
  customPresenceStatus: CustomPresenceStatus
  introduction: string
  introductionEmoji: string
}

type CustomPresenceStatus = 'ONLINE' | 'OFFLINE' | 'NOT_DISTURB' | 'INVISIBLE'

export { type CustomPresenceStatus, type User }
