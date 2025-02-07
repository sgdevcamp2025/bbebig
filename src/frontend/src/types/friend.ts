import { CustomPresenceStatus } from './user'

export interface Friend {
  id: number
  avatarUrl: string
  name: string
  status: CustomPresenceStatus
}
