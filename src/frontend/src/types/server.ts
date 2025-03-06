import { User } from './user'

export type ChannelType = 'CHAT' | 'VOICE'

export interface ChannelUser extends User {
  includeChannelId: number[]
}
