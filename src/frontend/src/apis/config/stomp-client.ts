import { Client } from '@stomp/stompjs'

import { SOCKET_URL } from '@/constants/env'
import { COOKIE_KEYS } from '@/constants/keys'
import cookie from '@/utils/cookie'

export const createStompClient = () => {
  return new Client({
    brokerURL: SOCKET_URL,
    connectHeaders: {
      Authorization: `Bearer ${cookie.getCookie(COOKIE_KEYS.ACCESS_TOKEN)}`
    },
    reconnectDelay: 5000,
    heartbeatIncoming: 4000,
    heartbeatOutgoing: 4000,
    debug: (str) => console.log('[STOMP Debug]', str)
  })
}
