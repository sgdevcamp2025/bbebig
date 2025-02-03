import { Navigate, Outlet } from 'react-router'

import { discordLogo } from '@/constants/discord-assets-path'
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
          src={discordLogo}
          alt='Discord Logo'
          className='w-[124px] absolute top-12 left-12'
        />
      </div>
      <Outlet />
    </div>
  )
}

export default AuthLayout
