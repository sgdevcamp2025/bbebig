import { create } from 'zustand'

import { ChannelMessage } from '@/types/message'

interface ChatState {
  messages: Record<number, ChannelMessage[]>
  addMessage: (channelId: number, message: ChannelMessage) => void
  setMessages: (channelId: number, messages: ChannelMessage[]) => void
}

export const useChatStore = create<ChatState>((set) => ({
  messages: {},

  addMessage: (channelId, message) => {
    set((state) => ({
      messages: {
        ...state.messages,
        [channelId]: [...(state.messages[channelId] || []), message]
      }
    }))
  },

  setMessages: (channelId, messages) => {
    set((state) => ({
      messages: {
        ...state.messages,
        [channelId]: messages
      }
    }))
  }
}))

interface MessageIdState {
  messageIds: Record<number, number>
  addMessageId: (channelId: number, messageId: number) => void
  getLastMessageId: (channelId: number) => number
}

export const useMessageIdStore = create<MessageIdState>((set, get) => ({
  messageIds: {},

  addMessageId: (channelId, messageId) =>
    set((state) => ({
      messageIds: {
        ...state.messageIds,
        [channelId]: messageId
      }
    })),

  getLastMessageId: (channelId) => {
    return get().messageIds[channelId]
  }
}))

interface MessageSequenceState {
  messageSequences: Record<number, number>
  addMessageSequence: (channelId: number, messageSequence: number) => void
  getLastMessageSequence: (channelId: number) => number
}

export const useMessageSequenceStore = create<MessageSequenceState>((set, get) => ({
  messageSequences: {},

  addMessageSequence: (channelId, messageSequence) =>
    set((state) => ({
      messageSequences: {
        ...state.messageSequences,
        [channelId]: messageSequence
      }
    })),

  getLastMessageSequence: (channelId) => {
    return get().messageSequences[channelId]
  }
}))
