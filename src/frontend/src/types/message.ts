interface Message {
  id: string
  memberId: string
  type: MessageType
  contents: { text: string }
  createdAt: Date
  updatedAt: Date
}

interface ChannelMessage {
  id: number
  serverId: number
  channelId: number
  sequence: number
  sendMemberId: number
  content: string
  createdAt: string
  updatedAt: string
  messageType: 'TEXT' | 'IMAGE' | 'VIDEO' | 'FILE' | 'SYSTEM'
  isDeleted: boolean
  deleted: boolean
}

interface SearchMessage {
  id: number
  serverId: number
  sequence: number
  channelId: number
  sendMemberId: number
  content: string
  createdAt: string
}

type MessageType = 'CHANNEL' | 'DM'

export { type ChannelMessage, type Message, type MessageType, type SearchMessage }
