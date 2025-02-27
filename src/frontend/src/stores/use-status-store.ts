import { create } from 'zustand'

import { ChatUser, CustomPresenceStatus } from '@/types/user'

interface StatusStore {
  channelMemberList: ChatUser[]
  updateMemberStatus: (memberId: number, status: CustomPresenceStatus) => void
  setChannelMembers: (members: ChatUser[]) => void
}

export const useStatusStore = create<StatusStore>((set) => ({
  channelMemberList: [],
  updateMemberStatus: (memberId: number, status: CustomPresenceStatus) =>
    set((state: StatusStore) => ({
      channelMemberList: state.channelMemberList.map((user) =>
        user.memberId === memberId ? { ...user, globalStatus: status } : user
      )
    })),

  setChannelMembers: (members) => set({ channelMemberList: members })
}))
