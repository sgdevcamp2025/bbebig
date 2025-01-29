import { Outlet } from 'react-router-dom'

function LandingLayout() {
  return (
    <div className='h-full min-h-screen max-w-full bg-blue-10'>
      <title>Discord Clone</title>
      <Outlet />
    </div>
  )
}

export default LandingLayout
