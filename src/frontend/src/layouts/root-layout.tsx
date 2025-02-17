import * as Sentry from '@sentry/react'
import { QueryClientProvider } from '@tanstack/react-query'
import { Toaster } from 'react-hot-toast'
import { Outlet } from 'react-router'

import { SENTRY_DSN } from '@/constants/env'
import queryClient from '@/libs/query-client'

Sentry.init({
  dsn: SENTRY_DSN,
  integrations: [Sentry.browserTracingIntegration(), Sentry.replayIntegration()],
  tracesSampleRate: 1.0,
  tracePropagationTargets: ['localhost'],
  replaysSessionSampleRate: 0.1,
  replaysOnErrorSampleRate: 1.0
})

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
