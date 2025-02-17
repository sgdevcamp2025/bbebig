import axiosInstance from '../config/axios-instance'
import { CommonResponseType } from '../schema/types/common'
import type {
  CreateCategoryRequestSchema,
  CreateCategoryResponseSchema,
  CreateChannelRequestSchema,
  CreateChannelResponseSchema,
  CreateServerRequestSchema,
  CreateServerResponseSchema,
  DeleteCategoryRequestSchema,
  DeleteCategoryResponseSchema,
  DeleteChannelRequestSchema,
  DeleteChannelResponseSchema,
  DeleteServerRequestSchema,
  DeleteServerResponseSchema,
  GetCategoriesRequestSchema,
  GetCategoriesResponseSchema,
  GetChannelLastVisitInfoWithMemberIdRequestSchema,
  GetChannelLastVisitInfoWithMemberIdResponseSchema,
  GetChannelListRequestSchema,
  GetChannelListResponseSchema,
  GetServerListRequestSchema,
  GetServerListResponseSchema,
  GetServerMemebersRequestSchema,
  GetServerMemebersResponseSchema,
  GetServersResponseSchema,
  ParticipateServerRequestSchema,
  ParticipateServerResponseSchema,
  UpdateCategoryRequestSchema,
  UpdateCategoryResponseSchema,
  UpdateChannelRequestSchema,
  UpdateChannelResponseSchema,
  UpdateServerRequestSchema,
  UpdateServerResponseSchema,
  WithdrawServerRequestSchema,
  WithdrawServerResponseSchema
} from '../schema/types/service'

const SERVER_PATH = `/service-server/servers`
const CHANNEL_PATH = `/service-server/channels`
const CATEGORY_PATH = `/service-server/categories`

const serviceService = () => {
  // Server API
  const deleteServer = async (data: DeleteServerRequestSchema) => {
    const response = await axiosInstance.delete<CommonResponseType<DeleteServerResponseSchema>>(
      `${SERVER_PATH}/${data.serverId}`
    )
    return response.data
  }

  const withDrawServer = async (data: WithdrawServerRequestSchema) => {
    const response = await axiosInstance.delete<CommonResponseType<WithdrawServerResponseSchema>>(
      `${SERVER_PATH}/${data.serverId}`
    )
    return response.data
  }

  const getServersList = async (data: GetServerListRequestSchema) => {
    const response = await axiosInstance.get<CommonResponseType<GetServerListResponseSchema>>(
      `${SERVER_PATH}/${data.serverId}`
    )
    return response.data
  }

  const getServers = async () => {
    const response = await axiosInstance.get<CommonResponseType<GetServersResponseSchema>>(
      `${SERVER_PATH}`
    )
    return response.data
  }

  const getServerMemebers = async (data: GetServerMemebersRequestSchema) => {
    const response = await axiosInstance.get<CommonResponseType<GetServerMemebersResponseSchema>>(
      `${SERVER_PATH}/${data.serverId}/members`
    )
    return response.data
  }

  const createServer = async (data: CreateServerRequestSchema) => {
    const response = await axiosInstance.post<CommonResponseType<CreateServerResponseSchema>>(
      `${SERVER_PATH}`,
      data
    )
    return response.data
  }

  const participateServer = async (data: ParticipateServerRequestSchema) => {
    const response = await axiosInstance.post<CommonResponseType<ParticipateServerResponseSchema>>(
      `${SERVER_PATH}/${data.serverId}/participate`,
      data
    )
    return response.data
  }

  const updateServer = async (data: UpdateServerRequestSchema) => {
    const response = await axiosInstance.put<CommonResponseType<UpdateServerResponseSchema>>(
      `${SERVER_PATH}/${data.serverId}`,
      data
    )
    return response.data
  }

  // Channel API
  const deleteChannel = async (data: DeleteChannelRequestSchema) => {
    const response = await axiosInstance.delete<CommonResponseType<DeleteChannelResponseSchema>>(
      `${CHANNEL_PATH}/${data.channelId}`
    )
    return response.data
  }

  const getChannelList = async (data: GetChannelListRequestSchema) => {
    const response = await axiosInstance.get<CommonResponseType<GetChannelListResponseSchema>>(
      `${CHANNEL_PATH}/${data.channelId}`
    )
    return response.data
  }

  const getChannelLastVisitInfoWithMemberId = async (
    data: GetChannelLastVisitInfoWithMemberIdRequestSchema
  ) => {
    const response = await axiosInstance.get<
      CommonResponseType<GetChannelLastVisitInfoWithMemberIdResponseSchema>
    >(`${CHANNEL_PATH}/${data.channelId}/lastInfo/member/${data.memberId}`)
    return response.data
  }

  const createChannel = async (data: CreateChannelRequestSchema) => {
    const response = await axiosInstance.post<CommonResponseType<CreateChannelResponseSchema>>(
      `${CHANNEL_PATH}`,
      data
    )
    return response.data
  }

  const updateChannel = async (data: UpdateChannelRequestSchema) => {
    const response = await axiosInstance.put<CommonResponseType<UpdateChannelResponseSchema>>(
      `${CHANNEL_PATH}/${data.channelId}`,
      data
    )
    return response.data
  }

  // Category API
  const deleteCategory = async (data: DeleteCategoryRequestSchema) => {
    const response = await axiosInstance.delete<CommonResponseType<DeleteCategoryResponseSchema>>(
      `${CATEGORY_PATH}/${data.categoryId}`
    )
    return response.data
  }

  const getCategories = async (data: GetCategoriesRequestSchema) => {
    const response = await axiosInstance.get<CommonResponseType<GetCategoriesResponseSchema>>(
      `${CATEGORY_PATH}/${data.categoryId}`
    )
    return response.data
  }

  const createCategory = async (data: CreateCategoryRequestSchema) => {
    const response = await axiosInstance.post<CommonResponseType<CreateCategoryResponseSchema>>(
      `${CATEGORY_PATH}`,
      data
    )
    return response.data
  }

  const updateCategory = async (data: UpdateCategoryRequestSchema) => {
    const response = await axiosInstance.put<CommonResponseType<UpdateCategoryResponseSchema>>(
      `${CATEGORY_PATH}/${data.categoryId}`,
      data
    )
    return response.data
  }

  return {
    getServers,
    getServersList,
    deleteServer,
    withDrawServer,
    createServer,
    participateServer,
    updateServer,
    deleteChannel,
    getChannelList,
    getServerMemebers,
    getChannelLastVisitInfoWithMemberId,
    createChannel,
    updateChannel,
    deleteCategory,
    getCategories,
    createCategory,
    updateCategory
  }
}

export default serviceService()
