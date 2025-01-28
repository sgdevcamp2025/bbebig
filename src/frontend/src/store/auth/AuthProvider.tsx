import { useState } from 'react'

import { AuthContext } from './AuthContext'

export const AuthProvider = ({ children }: { children: React.ReactNode }) => {
  const [accessToken, setAccessToken] = useState<string | null>(null)

  const isLogin = accessToken !== null

  const setUserLogin = (accessToken: string) => {
    setAccessToken(accessToken)
  }

  const setUserLogout = () => {
    setAccessToken(null)
  }

  return (
    <AuthContext.Provider value={{ isLogin, setUserLogin, setUserLogout }}>
      {children}
    </AuthContext.Provider>
  )
}
