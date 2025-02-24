import { Navigate, Outlet } from 'react-router-dom'

import useLoginStore from '@/stores/use-login-store'

function AuthLayout() {
  const isLogin = useLoginStore((state) => state.isLogin)

  if (isLogin) {
    return (
      <Navigate
        to='/channels/@me'
        replace
      />
    )
  }

  return (
    <div className='w-full h-screen flex justify-center items-center overflow-auto relative bg-auth-background bg-cover'>
      <div className='absolute left-0 right-0 top-0'>
        <img
          src='/icon/brand/icon_logo.svg'
          width={36}
          height={36}
          className='absolute top-12 left-12'
        />
      </div>
      <Outlet />
    </div>
  )
}

export default AuthLayout
