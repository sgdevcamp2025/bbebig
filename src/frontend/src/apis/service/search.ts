import axiosInstance from '../config/axios-instance'
import { CommonResponseType } from '../schema/types/common'
import {
  GetAllServersUnreadCountRequestSchema,
  GetAllServerUnreadCountResponseSchema,
  GetHistoryChattingMessageResponseSchema,
  GetHistoryChattingMessagesRequestSchema,
  GetSingleServerUnreadCountRequestSchema,
  GetSingleServerUnreadCountResponseSchema,
  SearchChattingChannelRequestSchema,
  SearchChattingChannelResponseSchema
} from '../schema/types/search'

const SEARCH_SERVER_PATH = `/search-server/server`
const HISTORY_MEMBER_PATH = `/search-server/member`

const searchService = () => {
  // 서버 채팅 채널 검색
  const searchChattingChannel = async (data: SearchChattingChannelRequestSchema) => {
    const response = await axiosInstance.post<
      CommonResponseType<SearchChattingChannelResponseSchema>
    >(`${SEARCH_SERVER_PATH}/${data.serverId}`, data)
    return response.data
  }

  // 서버 채팅 채널 과거 메시지 조회
  const getHistoryChattingMessages = async (data: GetHistoryChattingMessagesRequestSchema) => {
    const response = await axiosInstance.get<
      CommonResponseType<GetHistoryChattingMessageResponseSchema>
    >(`${SEARCH_SERVER_PATH}/${data.serverId}/channel/${data.channelId}/messages`, {
      params: {
        messageId: data.messageId,
        limit: data.limit
      }
    })
    return response.data
  }

  // 멤버별 서버 안 읽은 메시지 수 조회
  const getSingleServerUnreadCount = async (data: GetSingleServerUnreadCountRequestSchema) => {
    const response = await axiosInstance.get<
      CommonResponseType<GetSingleServerUnreadCountResponseSchema>
    >(`${HISTORY_MEMBER_PATH}/${data.memberId}/unread/server/${data.serverId}`)
    return response.data
  }

  // 멤버별 모든 서버 안 읽은 메시지 수 조회
  const getAllServersUnreadCount = async (data: GetAllServersUnreadCountRequestSchema) => {
    const response = await axiosInstance.get<
      CommonResponseType<GetAllServerUnreadCountResponseSchema>
    >(`${HISTORY_MEMBER_PATH}/${data.memberId}/unread/server/all`)
    return response.data
  }

  return {
    searchChattingChannel,
    getHistoryChattingMessages,
    getSingleServerUnreadCount,
    getAllServersUnreadCount
  }
}

export default searchService()
