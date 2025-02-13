import SERVER_PORT from '@/constants/base-port'

import getAxiosInstance from '../config/axios-instance'
import { CommonResponseType } from '../schema/types/common'
import type {
  CreateServerRequestSchema,
  CreateServerResponseSchema,
  DeleteServerRequestSchema,
  DeleteServerResponseSchema,
  GetChannelIdListInServerRequestSchema,
  GetChannelIdListInServerResponseSchema,
  GetMemberIdListInServerRequestSchema,
  GetMemberIdListInServerResponseSchema,
  GetMemberInfoLastVisitChannelRequestSchema,
  GetMemberInfoLastVisitChannelResponseSchema,
  GetServerIdListWithMemberIdRequestSchema,
  GetServerIdListWithMemberIdResponseSchema,
  GetServerListRequestSchema,
  GetServerListResponseSchema,
  GetServersResponseSchema,
  ParticipateServerRequestSchema,
  ParticipateServerResponseSchema,
  UpdateServerRequestSchema,
  UpdateServerResponseSchema,
  WithdrawServerRequestSchema,
  WithdrawServerResponseSchema
} from '../schema/types/server'

const BASE_PATH = `/servers`

const axiosInstance = getAxiosInstance(SERVER_PORT.SERVICE)

const serverService = () => {
  const deleteServer = async (data: DeleteServerRequestSchema) => {
    try {
      const response = await axiosInstance.delete<CommonResponseType<DeleteServerResponseSchema>>(
        `${BASE_PATH}/${data.serverId}`
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
        `${BASE_PATH}/${data.serverId}`
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
        `${BASE_PATH}/${data.serverId}`
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
        `${BASE_PATH}`
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
      >(`${BASE_PATH}/${data.serverId}/list/members`)
      return response.data
    } catch (error) {
      console.error(error)
      throw error
    }
  }

  const getChannelIdListInServer = async (data: GetChannelIdListInServerRequestSchema) => {
    try {
      const response = await axiosInstance.get<GetChannelIdListInServerResponseSchema>(
        `${BASE_PATH}/${data.serverId}/list/channels`
      )
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
      const response = await axiosInstance.get<GetMemberInfoLastVisitChannelResponseSchema>(
        `${BASE_PATH}/${data.serverId}/channels/info/member/${data.memberId}`
      )
      return response.data
    } catch (error) {
      console.error(error)
      throw error
    }
  }

  const getServerIdListWithMemberId = async (data: GetServerIdListWithMemberIdRequestSchema) => {
    try {
      const response = await axiosInstance.get<GetServerIdListWithMemberIdResponseSchema>(
        `${BASE_PATH}/${data.memberId}/list/servers`
      )
      return response.data
    } catch (error) {
      console.error(error)
      throw error
    }
  }

  const createServer = async (data: CreateServerRequestSchema) => {
    try {
      const response = await axiosInstance.post<CommonResponseType<CreateServerResponseSchema>>(
        `${BASE_PATH}`,
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
      >(`${BASE_PATH}/${data.serverId}/participate`, data)
      return response.data
    } catch (error) {
      console.error(error)
      throw error
    }
  }

  const updateServer = async (data: UpdateServerRequestSchema) => {
    try {
      const response = await axiosInstance.put<CommonResponseType<UpdateServerResponseSchema>>(
        `${BASE_PATH}/${data.serverId}`,
        data
      )
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
    updateServer
  }
}

export default serverService()
