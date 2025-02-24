import { useEffect } from 'react'

import { worker } from '@/__mock__/worker'
import { MOCK_SERVICE_WORKER } from '@/constants/env'

function MSWProvider() {
  useEffect(() => {
    if (MOCK_SERVICE_WORKER) {
      worker.start({
        onUnhandledRequest: 'bypass'
      })
    }
  }, [])

  return null
}

export default MSWProvider
