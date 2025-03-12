import { setupWorker } from 'msw/browser'

import { authHandler } from './handlers/auth.handler'
import { serviceHandler } from './handlers/service.handler'
import { userHandler } from './handlers/user.handler'

export const worker = setupWorker(...authHandler, ...serviceHandler, ...userHandler)
