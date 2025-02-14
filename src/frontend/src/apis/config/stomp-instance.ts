import { Client } from '@stomp/stompjs'

import { CHAT_SERVER_URL, SIGNALING_SERVER_URL } from '@/constants/env'

const STOMP_SETTING = (serverName: string) => {
  return {
    onConnect: () => {
      console.log(`[✅] ${serverName} 스톰프 연결 성공`)
    },
    onDisconnect: () => {
      console.log(`[❌] ${serverName} 스톰프 연결 실패`)
    },
    reconnectDelay: 5000,
    heartbeatIncoming: 4000,
    heartbeatOutgoing: 4000,
    debug: (str: string) => console.log(`[${serverName} STOMP Debug]`, str)
  }
}

export const chattingStompInstance = () => {
  return new Client({
    brokerURL: CHAT_SERVER_URL,
    ...STOMP_SETTING('채팅')
  })
}

export const signalingStompInstance = () => {
  return new Client({
    brokerURL: SIGNALING_SERVER_URL,
    ...STOMP_SETTING('시그널링')
  })
}
