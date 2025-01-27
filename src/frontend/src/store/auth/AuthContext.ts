import { createContext, useContext } from 'react'

interface AuthContextType {
  isLogin: boolean
  setUserLogin: (accessToken: string) => void
  setUserLogout: () => void
}

export const AuthContext = createContext<AuthContextType | null>(null)

export const useAuthStore = () => {
  const context = useContext(AuthContext)

  if (!context) {
    throw new Error('AuthContext내에 존재하지 않습니다.')
  }

  return context
}
