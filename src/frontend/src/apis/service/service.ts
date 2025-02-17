import * as Sentry from '@sentry/react'

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
  GetMemberIdListInServerRequestSchema,
  GetMemberIdListInServerResponseSchema,
  GetServerListRequestSchema,
  GetServerListResponseSchema,
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
    try {
      const response = await axiosInstance.delete<CommonResponseType<DeleteServerResponseSchema>>(
        `${SERVER_PATH}/${data.serverId}`
      )
      return response.data
    } catch (error) {
      Sentry.captureException(error)
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
      Sentry.captureException(error)
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
      Sentry.captureException(error)
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
      Sentry.captureException(error)
      throw error
    }
  }

  const getServerMemebers = async (data: GetMemberIdListInServerRequestSchema) => {
    try {
      const response = await axiosInstance.get<
        CommonResponseType<GetMemberIdListInServerResponseSchema>
      >(`${SERVER_PATH}/${data.serverId}/members`)
      return response.data
    } catch (error) {
      Sentry.captureException(error)
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
      Sentry.captureException(error)
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
      Sentry.captureException(error)
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
      Sentry.captureException(error)
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
      Sentry.captureException(error)
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
      Sentry.captureException(error)
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
      Sentry.captureException(error)
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
      Sentry.captureException(error)
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
      Sentry.captureException(error)
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
      Sentry.captureException(error)
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
      Sentry.captureException(error)
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
      Sentry.captureException(error)
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
      Sentry.captureException(error)
      throw error
    }
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
