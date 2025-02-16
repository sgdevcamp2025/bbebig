import { QueryClientProvider } from '@tanstack/react-query'
import { Outlet } from 'react-router'

import queryClient from '@/libs/query-client'
import { Toaster } from 'react-hot-toast'

function RootLayout() {
  return (
    <div className='h-full min-h-screen max-w-full bg-blue-10'>
      <title>Discord Clone</title>
      <QueryClientProvider client={queryClient}>
        <Outlet />
        <Toaster position='top-right' />
      </QueryClientProvider>
    </div>
  )
}

export default RootLayout
