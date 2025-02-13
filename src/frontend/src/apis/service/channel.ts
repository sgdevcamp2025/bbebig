import SERVER_PORT from '@/constants/base-port'

import getAxiosInstance from '../config/axios-instance'
import type {
  CreateChannelRequestSchema,
  CreateChannelResponseSchema,
  DeleteChannelRequestSchema,
  DeleteChannelResponseSchema,
  GetChannelLastVisitInfoWithMemberIdRequestSchema,
  GetChannelLastVisitInfoWithMemberIdResponseSchema,
  GetChannelListRequestSchema,
  GetChannelListResponseSchema,
  UpdateChannelRequestSchema,
  UpdateChannelResponseSchema
} from '../schema/types/channel'

const axiosInstance = getAxiosInstance(SERVER_PORT.SERVICE)

const BASE_PATH = `/channels`

const channelService = () => {
  const deleteChannel = async (data: DeleteChannelRequestSchema) => {
    const response = await axiosInstance.delete<DeleteChannelResponseSchema>(
      `${BASE_PATH}/${data.channelId}`
    )
    return response.data
  }
  const getChannelList = async (data: GetChannelListRequestSchema) => {
    const response = await axiosInstance.get<GetChannelListResponseSchema>(
      `${BASE_PATH}/${data.channelId}`
    )
    return response.data
  }
  const getChannelLastVisitInfoWithMemberId = async (
    data: GetChannelLastVisitInfoWithMemberIdRequestSchema
  ) => {
    const response = await axiosInstance.get<GetChannelLastVisitInfoWithMemberIdResponseSchema>(
      `${BASE_PATH}/${data.channelId}/lastInfo/member/${data.memberId}`
    )
    return response.data
  }
  const createChannel = async (data: CreateChannelRequestSchema) => {
    const response = await axiosInstance.post<CreateChannelResponseSchema>(`${BASE_PATH}`, data)
    return response.data
  }
  const updateChannel = async (data: UpdateChannelRequestSchema) => {
    const response = await axiosInstance.put<UpdateChannelResponseSchema>(
      `${BASE_PATH}/${data.channelId}`,
      data
    )
    return response.data
  }

  return {
    deleteChannel,
    getChannelList,
    getChannelLastVisitInfoWithMemberId,
    createChannel,
    updateChannel
  }
}

export default channelService()
