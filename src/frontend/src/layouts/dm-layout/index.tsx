import { Outlet } from 'react-router'
import DmSideBar from './components/dm-side-bar'

function DMLayout() {
  return (
    <div className='flex'>
      <DmSideBar />
      <Outlet />
    </div>
  )
}

export default DMLayout
