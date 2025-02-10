import { create } from 'zustand'
import cookie from '@/utils/cookie'
import { COOKIE_KEYS } from '@/constants/keys'

interface LoginStore {
  isLogin: boolean
  login: () => void
  logout: () => void
}

const getInitialLoginState = () => {
  if (typeof window === 'undefined') return false
  return !!cookie.getCookie(COOKIE_KEYS.ACCESS_TOKEN)
}

const useLoginStore = create<LoginStore>()((set) => ({
  isLogin: getInitialLoginState(),
  login: () => set({ isLogin: true }),
  logout: () => {
    cookie.deleteCookie(COOKIE_KEYS.ACCESS_TOKEN)
    set({ isLogin: false })
  }
}))

export default useLoginStore
