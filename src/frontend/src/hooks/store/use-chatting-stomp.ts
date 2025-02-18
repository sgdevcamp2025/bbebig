import { Client, IMessage } from '@stomp/stompjs'
import { useEffect, useRef, useState } from 'react'

import { chattingStompInstance } from '@/apis/config/stomp-instance'
import { COOKIE_KEYS } from '@/constants/keys'
import cookie from '@/utils/cookie'

export const useChattingStomp = () => {
  const client = useRef<Client | null>(null)
  const [isConnected, setIsConnected] = useState(false)

  // ✅ SUBSCRIBE
  // 연결
  const connect = () => {
    if (client.current) {
      console.log('[⚠️] 채팅 서버 연결 상태')
      return
    }

    const currentToken = cookie.getCookie(COOKIE_KEYS.ACCESS_TOKEN)
    client.current = chattingStompInstance()
    client.current.connectHeaders = {
      Authorization: `Bearer ${currentToken}`
    }

    client.current.onConnect = () => {
      console.log('[✅] 채팅 서버 연결 성공')
      setIsConnected(true)
    }

    client.current.onDisconnect = () => {
      console.log('[❌] 채팅 서버 연결 종료')
      setIsConnected(false)
      client.current = null
    }

    client.current.activate()
  }

  // 서버 구독
  const subscribeToServer = (
    serverId: string,
    memberId: string,
    callback: (message: unknown) => void
  ) => {
    if (client.current && isConnected) {
      const destination = `/topic/server/${serverId}`
      console.log(`[✅] 서버 ${serverId} 구독 시작`)

      client.current.subscribe(
        destination,
        (message: IMessage) => {
          console.log(`[📩] 서버 ${serverId} 메시지 수신:`, message.body)
          callback(JSON.parse(message.body))
        },
        { id: `chat-${memberId}`, MemberId: memberId }
      )
    }
  }

  // 채널 타이핑 구독
  const subscribeToChannel = (
    channelId: string,
    memberId: string,
    callback: (message: unknown) => void
  ) => {
    if (client.current && isConnected) {
      const destination = `/topic/channel/${channelId}`
      console.log(`[✅] 채널 ${channelId} 구독 시작`)

      client.current.subscribe(
        destination,
        (message: IMessage) => {
          console.log(`[📩] 채널 ${channelId} 메시지 수신:`, message.body)
          callback(JSON.parse(message.body))
        },
        { id: `chat-${memberId}`, MemberId: memberId }
      )
    }
  }

  // 개인 알림 구독
  const subscribeToPersonal = (memberId: string, callback: (message: unknown) => void) => {
    if (client.current && isConnected) {
      const destination = `/queue/${memberId}`
      console.log(`[✅] 개인 알림 ${memberId} 구독 시작`)

      client.current.subscribe(
        destination,
        (message: IMessage) => {
          console.log(`[📩] 개인 알림 ${memberId} 메시지 수신:`, message.body)
          callback(JSON.parse(message.body))
        },
        { id: `chat-${memberId}`, MemberId: memberId }
      )
    }
  }
  // 연결 종료
  const disconnect = () => {
    if (client.current) {
      client.current.deactivate()
      console.log('[❌] 채팅 서버 연결 종료')
      setIsConnected(false)
      client.current = null
    }
  }

  // 구독
  const subscribe = (destination: string, callback: (message: IMessage) => void) => {
    if (client.current && isConnected) {
      client.current.subscribe(destination, (message: IMessage) => {
        console.log('[✅] 채팅 메시지 구독:', message)
        callback(JSON.parse(message.body))
      })
    }
  }

  // ✅ PUBLISH

  // 메세지 전송
  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  const send = (destination: string, body: any) => {
    if (client.current && isConnected) {
      client.current.publish({
        destination,
        body: JSON.stringify(body)
      })
      console.log('[✅] 채팅 메시지 발행:', destination, body)
    }
  }

  // 서버 채널 채팅 전송
  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  const publishToServerChatting = (serverId: string, messageBody: any) => {
    if (!isConnected || !client.current) {
      console.log('[❌] 채팅 서버에 연결되지 않음.')
      return
    }

    const destination = `/pub/channel/message`
    console.log(`[📤] 서버 ${serverId} 채널로 메시지 발행:`)

    client.current.publish({
      destination,
      body: JSON.stringify({
        ...messageBody,
        serverId,
        createdAt: new Date().toISOString(),
        updatedAt: new Date().toISOString()
      }),
      headers: {
        'content-type': 'application/json',
        MemberId: messageBody.sendMemberId.toString()
      }
    })
  }

  useEffect(() => {
    return () => {
      if (client.current) {
        client.current.deactivate()
      }
    }
  }, [])

  return {
    connect,
    subscribeToServer,
    subscribeToChannel,
    subscribeToPersonal,
    disconnect,
    subscribe,
    send,
    publishToServerChatting,
    isConnected
  }
}

export default useChattingStomp
