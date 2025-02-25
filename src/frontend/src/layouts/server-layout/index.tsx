import { Outlet, useParams } from 'react-router-dom'

import useStatusBarStore from '@/stores/use-status-bar-store'

import { ServerSideBar } from './components/server-side-bar'
import { StatusSideBar } from './components/status-side-bar'

function ServerLayout() {
  const { serverId, channelId } = useParams<{ serverId: string; channelId: string }>()
  const { isStatusBarOpen } = useStatusBarStore()

  if (!serverId || !channelId) {
    throw new Error('serverId or channelId is required')
  }

  return (
    <div className='flex h-screen w-full'>
      <ServerSideBar serverId={serverId} />

      <main className='flex-1 bg-discord-gray-600 overflow-hidden'>
        <Outlet />
      </main>

      {isStatusBarOpen && <StatusSideBar serverId={serverId} />}
    </div>
  )
}

export default ServerLayout
