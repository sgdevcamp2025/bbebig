import { Client } from '@stomp/stompjs'

import { CHAT_SERVER_URL, SIGNALING_SERVER_URL } from '@/constants/env'

export const chattingStompInstance = () => {
  return new Client({
    brokerURL: CHAT_SERVER_URL,
    reconnectDelay: 5000,
    heartbeatIncoming: 4000,
    heartbeatOutgoing: 4000
  })
}

export const signalingStompInstance = () => {
  return new Client({
    brokerURL: SIGNALING_SERVER_URL,
    reconnectDelay: 5000,
    heartbeatIncoming: 4000,
    heartbeatOutgoing: 4000
  })
}
