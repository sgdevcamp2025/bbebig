import { setupWorker } from 'msw/browser'

import { authHandler } from './handlers/auth.handler'

export const worker = setupWorker(...authHandler)
