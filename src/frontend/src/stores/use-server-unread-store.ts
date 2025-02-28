import { create } from 'zustand'

interface ServerUnreadStore {
  unreadCounts: Record<number, Record<number, number>> // { serverId: { channelId: unreadCount } }
  setInitialUnreadCounts: (serverId: number, data: Record<number, number>) => void
  updateUnreadCount: (serverId: number, channelId: number, count: number) => void
  resetUnreadCount: (serverId: number, channelId: number) => void
  resetUnreadCountForServer: (serverId: number) => void
}

export const useServerUnreadStore = create<ServerUnreadStore>((set) => ({
  unreadCounts: {},

  // 초기 안 읽은 메세지(API)
  setInitialUnreadCounts: (serverId, data) =>
    set((state) => ({
      unreadCounts: { ...state.unreadCounts, [serverId]: data }
    })),

  // 안 읽은 메세지 업데이트 (소켓)
  updateUnreadCount: (serverId, channelId, count) =>
    set((state) => {
      const updatedServerUnread = {
        ...(state.unreadCounts[serverId] || {}),
        [channelId]: count
      }
      return {
        unreadCounts: {
          ...state.unreadCounts,
          [serverId]: updatedServerUnread
        }
      }
    }),

  // 안 읽은 메세지 초기화 (채널별)
  resetUnreadCount: (serverId, channelId) =>
    set((state) => {
      if (!state.unreadCounts[serverId]) return state
      const newCounts = { ...state.unreadCounts[serverId] }
      delete newCounts[channelId]
      return {
        unreadCounts: { ...state.unreadCounts, [serverId]: newCounts }
      }
    }),

  // 안 읽은 메세지 초기화 (서버별)
  resetUnreadCountForServer: (serverId) =>
    set((state) => {
      const newUnreadCounts = { ...state.unreadCounts }
      newUnreadCounts[serverId] = {}
      return {
        unreadCounts: newUnreadCounts
      }
    })
}))
