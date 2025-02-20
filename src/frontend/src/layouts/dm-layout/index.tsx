import { Outlet } from 'react-router-dom'

import DmSideBar from './components/dm-side-bar'

function DMLayout() {
  return (
    <div className='flex h-screen w-full'>
      <DmSideBar />
      <main className='flex-1 bg-discord-gray-600 overflow-hidden'>
        <Outlet />
      </main>
    </div>
  )
}

export default DMLayout
