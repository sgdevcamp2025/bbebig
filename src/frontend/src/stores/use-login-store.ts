import { create } from 'zustand'

import authService from '@/apis/service/auth'
import { COOKIE_KEYS } from '@/constants/keys'
import cookie from '@/utils/cookie'
interface LoginStore {
  isLogin: boolean
  initialLoginState: () => Promise<void>
  login: () => void
  logout: () => void
}

const useLoginStore = create<LoginStore>()((set) => ({
  isLogin: false,
  initialLoginState: async () => {
    const statusCheck = await authService.statusCheck()
    set({ isLogin: statusCheck.result.status })
  },
  login: () => set({ isLogin: true }),
  logout: () => {
    cookie.deleteCookie(COOKIE_KEYS.ACCESS_TOKEN)
    set({ isLogin: false })
  }
}))

export default useLoginStore
