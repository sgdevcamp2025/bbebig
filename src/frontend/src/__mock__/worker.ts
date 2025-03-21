import { setupWorker } from 'msw/browser'

import { authHandler } from './handlers/auth.handler'
import { chattingHandlers } from './handlers/chatting.handler'
import { searchHandler } from './handlers/search.handler'
import { serviceHandler } from './handlers/service.handler'
import { signalingHandlers } from './handlers/signaling.handler'
import { userHandler } from './handlers/user.handler'

export const worker = setupWorker(
  ...authHandler,
  ...serviceHandler,
  ...userHandler,
  ...searchHandler,
  ...signalingHandlers,
  ...chattingHandlers
)
