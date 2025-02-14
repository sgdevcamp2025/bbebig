import SERVER_PORT from '@/constants/base-port'

import getAxiosInstance from '../config/axios-instance'
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
  GetChannelIdListInServerRequestSchema,
  GetChannelIdListInServerResponseSchema,
  GetChannelLastVisitInfoWithMemberIdRequestSchema,
  GetChannelLastVisitInfoWithMemberIdResponseSchema,
  GetChannelListRequestSchema,
  GetChannelListResponseSchema,
  GetChannelUserListRequestSchema,
  GetChannelUserListResponseSchema,
  GetMemberIdListInServerRequestSchema,
  GetMemberIdListInServerResponseSchema,
  GetMemberInfoLastVisitChannelRequestSchema,
  GetMemberInfoLastVisitChannelResponseSchema,
  GetServerIdListWithMemberIdRequestSchema,
  GetServerIdListWithMemberIdResponseSchema,
  GetServerListRequestSchema,
  GetServerListResponseSchema,
  GetServersResponseSchema,
  HealthCheckResponseSchema,
  LookUpErrorResponseSchema,
  LookUpRequestSchema,
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

const SERVER_PATH = `/servers`
const UTIL_PATH = `/utils`
const CHANNEL_PATH = `/channels`
const CATEGORY_PATH = `/categories`

const axiosInstance = getAxiosInstance(SERVER_PORT.SERVICE)

const serviceService = () => {
  // Server API
  const deleteServer = async (data: DeleteServerRequestSchema) => {
    try {
      const response = await axiosInstance.delete<CommonResponseType<DeleteServerResponseSchema>>(
        `${SERVER_PATH}/${data.serverId}`
      )
      return response.data
    } catch (error) {
      console.error(error)
      throw error
    }
  }

  const withDrawServer = async (data: WithdrawServerRequestSchema) => {
    try {
      const response = await axiosInstance.delete<CommonResponseType<WithdrawServerResponseSchema>>(
        `${SERVER_PATH}/${data.serverId}`
      )
      return response.data
    } catch (error) {
      console.error(error)
      throw error
    }
  }

  const getServersList = async (data: GetServerListRequestSchema) => {
    try {
      const response = await axiosInstance.get<CommonResponseType<GetServerListResponseSchema>>(
        `${SERVER_PATH}/${data.serverId}`
      )
      return response.data
    } catch (error) {
      console.error(error)
      throw error
    }
  }

  const getServers = async () => {
    try {
      const response = await axiosInstance.get<CommonResponseType<GetServersResponseSchema>>(
        `${SERVER_PATH}`
      )
      return response.data
    } catch (error) {
      console.error(error)
      throw error
    }
  }

  const getMemberIdListInServer = async (data: GetMemberIdListInServerRequestSchema) => {
    try {
      const response = await axiosInstance.get<
        CommonResponseType<GetMemberIdListInServerResponseSchema>
      >(`${SERVER_PATH}/${data.serverId}/list/members`)
      return response.data
    } catch (error) {
      console.error(error)
      throw error
    }
  }

  const getChannelIdListInServer = async (data: GetChannelIdListInServerRequestSchema) => {
    try {
      const response = await axiosInstance.get<
        CommonResponseType<GetChannelIdListInServerResponseSchema>
      >(`${SERVER_PATH}/${data.serverId}/list/channels`)
      return response.data
    } catch (error) {
      console.error(error)
      throw error
    }
  }

  const getMemberInfoLastVisitChannel = async (
    data: GetMemberInfoLastVisitChannelRequestSchema
  ) => {
    try {
      const response = await axiosInstance.get<
        CommonResponseType<GetMemberInfoLastVisitChannelResponseSchema>
      >(`${SERVER_PATH}/${data.serverId}/channels/info/member/${data.memberId}`)
      return response.data
    } catch (error) {
      console.error(error)
      throw error
    }
  }

  const getServerIdListWithMemberId = async (data: GetServerIdListWithMemberIdRequestSchema) => {
    try {
      const response = await axiosInstance.get<
        CommonResponseType<GetServerIdListWithMemberIdResponseSchema>
      >(`${SERVER_PATH}/${data.memberId}/list/servers`)
      return response.data
    } catch (error) {
      console.error(error)
      throw error
    }
  }

  const createServer = async (data: CreateServerRequestSchema) => {
    try {
      const response = await axiosInstance.post<CommonResponseType<CreateServerResponseSchema>>(
        `${SERVER_PATH}`,
        data
      )
      return response.data
    } catch (error) {
      console.error(error)
      throw error
    }
  }

  const participateServer = async (data: ParticipateServerRequestSchema) => {
    try {
      const response = await axiosInstance.post<
        CommonResponseType<ParticipateServerResponseSchema>
      >(`${SERVER_PATH}/${data.serverId}/participate`, data)
      return response.data
    } catch (error) {
      console.error(error)
      throw error
    }
  }

  const updateServer = async (data: UpdateServerRequestSchema) => {
    try {
      const response = await axiosInstance.put<CommonResponseType<UpdateServerResponseSchema>>(
        `${SERVER_PATH}/${data.serverId}`,
        data
      )
      return response.data
    } catch (error) {
      console.error(error)
      throw error
    }
  }

  // Util API
  const healthCheck = async () => {
    try {
      const response = await axiosInstance.get<CommonResponseType<HealthCheckResponseSchema>>(
        `${UTIL_PATH}/health`
      )
      return response.data
    } catch (error) {
      console.error(error)
      throw error
    }
  }

  const lookUp = async (data: LookUpRequestSchema) => {
    try {
      const response = await axiosInstance.get<LookUpErrorResponseSchema>(
        `${UTIL_PATH}/error-code`,
        {
          params: data
        }
      )
      return response.data
    } catch (error) {
      console.error(error)
      throw error
    }
  }

  // Channel API
  const deleteChannel = async (data: DeleteChannelRequestSchema) => {
    try {
      const response = await axiosInstance.delete<CommonResponseType<DeleteChannelResponseSchema>>(
        `${CHANNEL_PATH}/${data.channelId}`
      )
      return response.data
    } catch (error) {
      console.error(error)
      throw error
    }
  }

  const getChannelList = async (data: GetChannelListRequestSchema) => {
    try {
      const response = await axiosInstance.get<CommonResponseType<GetChannelListResponseSchema>>(
        `${CHANNEL_PATH}/${data.channelId}`
      )
      return response.data
    } catch (error) {
      console.error(error)
      throw error
    }
  }

  const getChannelLastVisitInfoWithMemberId = async (
    data: GetChannelLastVisitInfoWithMemberIdRequestSchema
  ) => {
    try {
      const response = await axiosInstance.get<
        CommonResponseType<GetChannelLastVisitInfoWithMemberIdResponseSchema>
      >(`${CHANNEL_PATH}/${data.channelId}/lastInfo/member/${data.memberId}`)
      return response.data
    } catch (error) {
      console.error(error)
      throw error
    }
  }

  const createChannel = async (data: CreateChannelRequestSchema) => {
    try {
      const response = await axiosInstance.post<CommonResponseType<CreateChannelResponseSchema>>(
        `${CHANNEL_PATH}`,
        data
      )
      return response.data
    } catch (error) {
      console.error(error)
      throw error
    }
  }

  const updateChannel = async (data: UpdateChannelRequestSchema) => {
    try {
      const response = await axiosInstance.put<CommonResponseType<UpdateChannelResponseSchema>>(
        `${CHANNEL_PATH}/${data.channelId}`,
        data
      )
      return response.data
    } catch (error) {
      console.error(error)
      throw error
    }
  }

  // Category API
  const deleteCategory = async (data: DeleteCategoryRequestSchema) => {
    try {
      const response = await axiosInstance.delete<CommonResponseType<DeleteCategoryResponseSchema>>(
        `${CATEGORY_PATH}/${data.categoryId}`
      )
      return response.data
    } catch (error) {
      console.error(error)
      throw error
    }
  }

  const getCategories = async (data: GetCategoriesRequestSchema) => {
    try {
      const response = await axiosInstance.get<CommonResponseType<GetCategoriesResponseSchema>>(
        `${CATEGORY_PATH}/${data.categoryId}`
      )
      return response.data
    } catch (error) {
      console.error(error)
      throw error
    }
  }

  const createCategory = async (data: CreateCategoryRequestSchema) => {
    try {
      const response = await axiosInstance.post<CommonResponseType<CreateCategoryResponseSchema>>(
        `${CATEGORY_PATH}`,
        data
      )
      return response.data
    } catch (error) {
      console.error(error)
      throw error
    }
  }

  const updateCategory = async (data: UpdateCategoryRequestSchema) => {
    try {
      const response = await axiosInstance.put<CommonResponseType<UpdateCategoryResponseSchema>>(
        `${CATEGORY_PATH}/${data.categoryId}`,
        data
      )
      return response.data
    } catch (error) {
      console.error(error)
      throw error
    }
  }

  const getChannelUserList = async (data: GetChannelUserListRequestSchema) => {
    try {
      const response = await axiosInstance.get<
        CommonResponseType<GetChannelUserListResponseSchema>
      >(`${CHANNEL_PATH}/${data.channelId}/list/users`)
      return response.data
    } catch (error) {
      console.error(error)
      throw error
    }
  }

  return {
    getServers,
    getServersList,
    deleteServer,
    withDrawServer,
    getMemberIdListInServer,
    getChannelIdListInServer,
    getMemberInfoLastVisitChannel,
    getServerIdListWithMemberId,
    createServer,
    participateServer,
    updateServer,
    deleteChannel,
    getChannelList,
    getChannelLastVisitInfoWithMemberId,
    createChannel,
    updateChannel,
    deleteCategory,
    getCategories,
    createCategory,
    updateCategory,
    healthCheck,
    lookUp,
    getChannelUserList
  }
}

export default serviceService()
