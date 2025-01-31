import Sidebar from '@/components/side-bar'
import { Outlet } from 'react-router'

function DMLayout() {
  return (
    <div className='flex'>
      <Sidebar type='dm' />
      <Outlet />
    </div>
  )
}

export default DMLayout
