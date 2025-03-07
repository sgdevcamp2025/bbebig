import { Client, IMessage } from '@stomp/stompjs'
import { useParams } from 'react-router-dom'

import { CHAT_SERVER_URL } from '@/constants/env'
import { COOKIE_KEYS } from '@/constants/keys'
import { useHandleServerEvent } from '@/hooks/use-handler-server-event'
import { ChannelVisitEventRequest, ChattingMessageEvent } from '@/types/chat-stomp-event'
import cookie from '@/utils/cookie'
import { errorLog, log } from '@/utils/log'
import { handlePersonalNotificationEvent } from '@/utils/personal-notification-event-handler'

import useGetSelfUser from './queries/user/useGetSelfUser'

const clientInstance = new Client({
  brokerURL: CHAT_SERVER_URL,
  reconnectDelay: 5000,
  heartbeatIncoming: 4000,
  heartbeatOutgoing: 4000
})

export const useChattingStomp = () => {
  const selfUser = useGetSelfUser()
  const memberId = selfUser.id.toString()
  const serverId = useParams().serverId
  const handleServerEvent = useHandleServerEvent(serverId ?? '-1')
  const checkConnection = () => {
    return clientInstance.connected && clientInstance.webSocket?.readyState === WebSocket.OPEN
  }

  // ✅ SUBSCRIBE
  // 연결
  const connect = (): Promise<void> => {
    if (checkConnection()) {
      log('[✅] 이미 채팅 서버에 연결되어 있음')
      return Promise.resolve()
    }

    log('[🔗] 채팅 서버 연결 시도... ', memberId)

    return new Promise<void>((resolve, reject) => {
      const token = cookie.getCookie(COOKIE_KEYS.ACCESS_TOKEN)

      clientInstance.connectHeaders = {
        AcceptVersion: '1.3,1.2,1.1,1.0',
        Authorization: `Bearer ${token}`,
        MemberId: memberId,
        Platform: 'WEB'
      }

      clientInstance.onConnect = () => {
        log('[✅] 채팅 서버 연결 성공')
        resolve()
      }

      clientInstance.onStompError = (frame) => {
        errorLog('[❌] STOMP 에러:', frame.headers['message'])
        reject(new Error('STOMP 연결 오류'))
      }

      clientInstance.onWebSocketError = (event) => {
        errorLog('[❌] WebSocket 에러:', event)
        reject(new Error('WebSocket 연결 오류'))
      }

      clientInstance.onDisconnect = () => {
        log('[❌] 채팅 서버 연결 종료')
        reject(new Error('STOMP 연결 종료'))
      }

      clientInstance.activate()
    })
  }

  // 서버 구독
  const subscribeToServer = (serverId: number, callback?: (message: unknown) => void) => {
    if (!checkConnection() || !clientInstance) {
      log('[❌] STOMP 연결되지 않아서 구독 불가:', serverId)
      return
    }

    const destination = `/topic/server/${serverId}`
    log(`[✅] 서버 ${serverId} 구독 시작`)

    clientInstance.subscribe(
      destination,
      (message: IMessage) => {
        try {
          const messageBody = JSON.parse(message.body)
          log(`[📩] (${serverId})번 서버 이벤트 수신 :`, messageBody)
          handleServerEvent(messageBody)
          callback?.(messageBody)
        } catch (error) {
          errorLog('[❌] 서버 이벤트 처리 중 오류 발생:', error)
        }
      },
      { id: `chat-${memberId}`, MemberId: memberId }
    )
  }

  // 채널 타이핑 구독
  const subscribeToChannelTyping = (channelId: string, callback: (message: unknown) => void) => {
    if (checkConnection() && clientInstance) {
      const destination = `/topic/channel/${channelId}`
      log(`[✅] 채널 ${channelId} 구독 시작`)

      clientInstance.subscribe(
        destination,
        (message: IMessage) => {
          log(`[📩] 채널 ${channelId} 메시지 수신:`, message.body)
          callback(JSON.parse(message.body))
        },
        { id: `chat-${channelId}` }
      )
    }
  }

  // 개인 알림 구독
  const subscribeToPersonal = async (callback?: (message: unknown) => void) => {
    if (!checkConnection()) {
      log('[❌] 채팅 서버에 연결되지 않음.')
      return
    }

    const destination = `/queue/notification/${memberId}`
    log(`[✅] 👤 개인 알림 ${memberId} 구독 시작`)

    clientInstance.subscribe(
      destination,
      (message: IMessage) => {
        log('======== 개인 알림 구독 시작')
        try {
          const messageBody = JSON.parse(message.body)
          log(`[📩] 개인 알림 ${memberId} 메시지 수신:`, message.body)
          handlePersonalNotificationEvent(messageBody)
          callback?.(messageBody)
        } catch (error) {
          errorLog('[❌] 개인 알림 이벤트 처리 중 오류 발생:', error)
        }
      },
      { id: `chat-${memberId}`, MemberId: memberId }
    )
  }

  // 연결 종료
  const disconnect = () => {
    if (clientInstance) {
      clientInstance.deactivate()
      log('[❌] 채팅 서버 연결 종료')
    }
  }

  // 구독 해제
  const unsubscribe = (destination: string) => {
    if (checkConnection()) {
      log(`[❌] 구독 해제 요청: ${destination}`)
      clientInstance.unsubscribe(destination)
    }
  }

  // ✅ PUBLISH

  // 서버 채널 채팅 전송
  const publishToServerChatting = async (body: ChattingMessageEvent) => {
    if (!checkConnection()) {
      log('[❌] 채팅 서버에 연결되지 않음.')
      await connect()
      return
    }

    const destination = `/pub/channel/message`
    log(`[📤] 서버 ${body.serverId}의 ${body.channelId} 채널로 메시지 발행:`)

    const messageBody =
      JSON.stringify({
        chatType: 'CHANNEL',
        messageType: 'TEXT',
        type: 'MESSAGE_CREATE',
        serverId: body.serverId,
        channelId: body.channelId,
        sendMemberId: Number(memberId),
        content: body.content
      }) + '\0'

    clientInstance.publish({
      destination,
      body: messageBody,
      headers: {
        MemberId: memberId,
        'content-type': 'application/json'
      }
    })
  }

  // 채널 방문 이벤트
  const publishToChannelEnter = async (body: ChannelVisitEventRequest) => {
    if (!checkConnection()) {
      log('[❌] 채팅 서버에 연결되지 않음.')
      await connect()
      return
    }

    const destination = `/pub/channel/enter`
    log(`[📤] 채널 ${body.channelId} 방문 이벤트 발행:`)

    clientInstance.publish({
      destination,
      body: JSON.stringify({
        ...body,
        memberId: Number(memberId),
        type: 'CHANNEL_ENTER',
        eventTime: new Date().toISOString()
      }),
      headers: {
        MemberId: memberId,
        'content-type': 'application/json'
      }
    })
  }

  // 채널 퇴장 이벤트
  const publishToChannelLeave = (body: ChannelVisitEventRequest) => {
    if (!checkConnection()) {
      log('[❌] 채팅 서버에 연결되지 않음.')
      connect()
      return
    }

    const destination = `/pub/channel/leave`
    log(`[📤] 채널 ${body.channelId} 퇴장 이벤트 발행:`)

    clientInstance.publish({
      destination,
      body: JSON.stringify({
        ...body,
        memberId: Number(memberId),
        type: 'CHANNEL_LEAVE',
        lastReadMessageId: body.lastReadMessageId,
        lastReadSequence: body.lastReadSequence,
        eventTime: new Date().toISOString()
      }),
      headers: {
        MemberId: memberId,
        'content-type': 'application/json'
      }
    })
  }

  return {
    connect,
    subscribeToServer,
    subscribeToChannelTyping,
    subscribeToPersonal,
    disconnect,
    unsubscribe,
    publishToServerChatting,
    publishToChannelLeave,
    publishToChannelEnter,
    checkConnection
  }
}
