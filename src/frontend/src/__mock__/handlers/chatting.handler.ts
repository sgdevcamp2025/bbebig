import { WebSocketLink, ws } from 'msw'

import { CHAT_SERVER_URL } from '@/constants/env'
import { log } from '@/utils/log'

const chattingServer: WebSocketLink = ws.link(CHAT_SERVER_URL)

export const chattingHandlers = [
  chattingServer.addEventListener('connection', ({ client }) => {
    log('✅ WebSocket connection initiated')

    // 클라이언트 메시지를 서버로 전달
    client.addEventListener('message', (event) => {
      // 연결 시도만 하고 실제 전송은 하지 않음
      log('Message from client:', event.data)
    })
  })
]
