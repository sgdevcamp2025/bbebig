import { create } from 'zustand'
import { persist } from 'zustand/middleware'

interface ServerState {
  selectedServerId: number | null
  setSelectedServer: (serverId: number) => void
}

export const useServerStore = create<ServerState>()(
  persist(
    (set) => ({
      selectedServerId: null,
      setSelectedServer: (serverId) => set({ selectedServerId: serverId })
    }),
    {
      name: 'server-storage'
    }
  )
)
