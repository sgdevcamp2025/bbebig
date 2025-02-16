import { create } from 'zustand'

import { COOKIE_KEYS } from '@/constants/keys'
import cookie from '@/utils/cookie'

interface LoginStore {
  isLogin: boolean
  login: () => void
  logout: () => void
}

const getInitialLoginState = () => {
  if (typeof window === 'undefined') return false
  const accessToken = cookie.getCookie(COOKIE_KEYS.ACCESS_TOKEN)
  return !!accessToken
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
