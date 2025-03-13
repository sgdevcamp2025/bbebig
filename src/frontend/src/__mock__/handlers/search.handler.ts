import { http, HttpResponse } from 'msw'

import {
  GetAllServerUnreadCountResponseSchema,
  GetHistoryChattingMessageResponseSchema,
  GetSingleServerUnreadCountResponseSchema
} from '@/apis/schema/types/search'
import { SERVER_URL } from '@/constants/env'

import {
  mockMessages,
  mockMessagesByChannel,
  mockUnreadChannels,
  mockUnreadMessages
} from '../data/search.handler'
const copyMockMessages = [...mockMessages]
const copyUnreadChannels = structuredClone(mockUnreadChannels)
const copyUnreadMessages = structuredClone(mockUnreadMessages)
const copyMessagesByChannel = structuredClone(mockMessagesByChannel)

const SEARCH_SERVER_PATH = `/search-server/server`
const SEARCH_HISTORY_PATH = `/search-server/history`

export const searchHandler = [
  http.post(`${SERVER_URL}${SEARCH_SERVER_PATH}/:serverId`, ({ params }) => {
    const { serverId } = params as { serverId: string }
    return HttpResponse.json({
      code: 'MESSAGE_FOUND',
      message: 'Message found',
      result: {
        serverId: Number(serverId),
        totalCount: copyMockMessages.length,
        messages: copyMockMessages
      } as GetHistoryChattingMessageResponseSchema
    })
  }),
  http.get(`${SERVER_URL}${SEARCH_HISTORY_PATH}/unread/server/:serverId`, ({ params }) => {
    const { serverId } = params as { serverId: string }
    return HttpResponse.json({
      code: 'UNREAD_MESSAGE_FOUND',
      message: 'Unread message found',
      result: {
        serverId: Number(serverId),
        channels: copyUnreadChannels,
        totalUnread: copyUnreadChannels.reduce((acc, curr) => acc + curr.unreadCount, 0)
      } as GetSingleServerUnreadCountResponseSchema
    })
  }),
  http.get(`${SERVER_URL}${SEARCH_HISTORY_PATH}/unread/server/all`, () => {
    return HttpResponse.json({
      code: 'UNREAD_MESSAGE_FOUND',
      message: 'Unread message found',
      result: {
        memberId: 1,
        totalServerCount: copyUnreadMessages.length,
        serverInfos: copyUnreadMessages
      } as GetAllServerUnreadCountResponseSchema
    })
  }),
  http.get(
    `${SERVER_URL}${SEARCH_HISTORY_PATH}/server/:serverId/channel/:channelId/messages`,
    ({ params }) => {
      const { serverId, channelId } = params as { serverId: string; channelId: string }
      return HttpResponse.json({
        code: 'UNREAD_MESSAGE_FOUND',
        message: 'Unread message found',
        result: {
          ...copyMessagesByChannel,
          serverId: Number(serverId),
          channelId: Number(channelId)
        }
      })
    }
  )
]
