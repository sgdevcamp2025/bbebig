import { Navigate, Outlet } from 'react-router-dom'

import { PRIVATE_ROUTE_ENABLE } from '@/constants/env'
import { useAuthStore } from '@/store/auth/AuthContext'

function PrivateLayout() {
  const { isLogin } = useAuthStore()

  if (PRIVATE_ROUTE_ENABLE && !isLogin) {
    return (
      <Navigate
        to='/login'
        replace
      />
    )
  }

  return <Outlet />
}

export default PrivateLayout
