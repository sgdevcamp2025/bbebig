import { toSocketIo } from '@mswjs/socket.io-binding'
import { WebSocketLink, ws } from 'msw'

import { SIGNALING_NODE_SERVER_URL } from '@/constants/env'
import { log } from '@/utils/log'

const signalingServer: WebSocketLink = ws.link(
  `${SIGNALING_NODE_SERVER_URL}/socket.io/?EIO=4&transport=websocket`
)

export const signalingHandlers = [
  signalingServer.addEventListener('connection', (connection) => {
    log('✅ WebSocket connection initiated')

    const io = toSocketIo(connection)
    const { client } = io
    const mockSocketId = 'mock-' + Math.random().toString(36).substring(2, 9)
    let roomName: string
    let userId: string

    // 클라이언트 메시지를 서버로 전달
    connection.client.addEventListener('message', (event) => {
      // 연결 시도만 하고 실제 전송은 하지 않음
      log('Message from client:', event.data)
    })

    // 클라이언트 이벤트 처리
    client.on('join_room', (_, data: unknown) => {
      try {
        const { roomName: room, userId: user } = data as { roomName: string; userId: string }
        roomName = room
        userId = user
        log(`✅ User ${userId} joined room ${roomName}`)
        client.emit('welcome', mockSocketId, userId)
      } catch (e) {
        log('Error in join_room:', e)
      }
    })

    // 나머지 이벤트 핸들러들...
    client.on('offer', (_, offer, remoteId) => {
      log('Received offer for:', remoteId)
      client.emit('offer', offer, mockSocketId, userId)
    })

    client.on('answer', (_, answer, remoteId) => {
      log('Received answer for:', remoteId)
      client.emit('answer', answer, mockSocketId, userId)
    })

    client.on('ice', (_, ice, remoteId) => {
      log('Received ICE candidate for:', remoteId)
      client.emit('ice', ice, mockSocketId, userId)
    })

    client.on('disconnect', () => {
      log('❌ Client disconnected:', mockSocketId)
      client.emit('user_left', mockSocketId)
    })

    // 초기 handshake 응답
    client.emit('connect')
  })
]
