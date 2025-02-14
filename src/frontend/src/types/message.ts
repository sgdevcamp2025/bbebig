type Message = {
  id: string
  memberId: string
  type: MessageType
  contents: { text: string }
  createdAt: Date
  updatedAt: Date
}

type MessageType = 'CHANNEL' | 'DIRECT'

export { type Message, type MessageType }
