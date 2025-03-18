import { startTransition } from 'react'
import { createRoot } from 'react-dom/client'
import { RouterProvider } from 'react-router-dom'

import { MOCK_SERVICE_WORKER } from '@/constants/env'

import { router } from './routes'

async function enableMocking() {
  if (MOCK_SERVICE_WORKER) {
    const { worker } = await import('@/__mock__/worker')
    worker.start({
      onUnhandledRequest: 'bypass'
    })
  }
}

enableMocking().then(() => {
  startTransition(() => {
    createRoot(document.getElementById('root') as HTMLElement).render(
      <RouterProvider router={router} />
    )
  })
})
