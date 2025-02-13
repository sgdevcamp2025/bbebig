import SERVER_PORT from '@/constants/base-port'

import getAxiosInstance from '../config/axios-instance'
import type {
  CreateCategoryRequestSchema,
  CreateCategoryResponseSchema,
  CreateChannelRequestSchema,
  CreateChannelResponseSchema
} from '../schema/server'

const BASE_PATH = `/server-server`

const axiosInstance = getAxiosInstance(SERVER_PORT.SERVICE)

const serverService = () => {
  const createCategory = async (data: CreateCategoryRequestSchema) => {
    try {
      const response = await axiosInstance.post<CreateCategoryResponseSchema>(
        `${BASE_PATH}/categories`,
        data
      )
      return response.data
    } catch (error) {
      console.error(error)
      throw error
    }
  }

  const createChannel = async (data: CreateChannelRequestSchema) => {
    try {
      const response = await axiosInstance.post<CreateChannelResponseSchema>(
        `${BASE_PATH}/channels`,
        data
      )
      return response.data
    } catch (error) {
      console.error(error)
      throw error
    }
  }

  return {
    createCategory,
    createChannel
  }
}

export default serverService()
