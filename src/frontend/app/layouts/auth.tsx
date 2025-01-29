import { Outlet } from 'react-router-dom'

import { discordLogo } from '@/libs/discord-assets-path'
function AuthLayout() {
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
