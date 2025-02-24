import { create } from 'zustand'

import { ChannelMessage } from '@/types/message'

interface ChatState {
  messages: Record<number, ChannelMessage[]>
  addMessage: (channelId: number, message: ChannelMessage) => void
  setMessages: (channelId: number, messages: ChannelMessage[]) => void
}

export const useChatStore = create<ChatState>((set) => ({
  messages: {},

  addMessage: (channelId, message) =>
    set((state) => ({
      messages: {
        ...state.messages,
        [channelId]: [...(state.messages[channelId] || []), message]
      }
    })),

  setMessages: (channelId, messages) =>
    set((state) => ({
      messages: {
        ...state.messages,
        [channelId]: messages
      }
    }))
}))
