import { create } from 'zustand'

interface StatusBarStore {
  isStatusBarOpen: boolean
  toggleStatusBar: () => void
}

const useStatusBarStore = create<StatusBarStore>((set) => ({
  isStatusBarOpen: false,
  toggleStatusBar: () => set((state) => ({ isStatusBarOpen: !state.isStatusBarOpen }))
}))

export default useStatusBarStore
