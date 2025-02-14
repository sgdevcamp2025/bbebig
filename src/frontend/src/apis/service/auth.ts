import { COOKIE_KEYS } from '@/constants/keys'
import cookie from '@/utils/cookie'

import SERVER_PORT from '../../constants/base-port'
import getAxiosInstance from '../config/axios-instance'
import { LoginResponseSchema, LoginSchema, RegisterSchema } from '../schema/types/auth'

const BASE_PATH = `/auth-server`

const axiosInstance = getAxiosInstance(SERVER_PORT.AUTH)

const authService = () => {
  const login = async (data: LoginSchema) => {
    try {
      const res = await axiosInstance.post<LoginResponseSchema>(`${BASE_PATH}/login`, data)
      const accessToken = res.data.result.accessToken
      cookie.setCookie(COOKIE_KEYS.ACCESS_TOKEN, accessToken)
      return true
    } catch (error) {
      console.error(error)
      throw error
    }
  }

  const register = async (data: RegisterSchema) => {
    try {
      const response = await axiosInstance.post(`${BASE_PATH}/register`, data)
      return response.data
    } catch (error) {
      console.error(error)
      throw error
    }
  }

  const logout = async () => {
    try {
      await axiosInstance.post(`${BASE_PATH}/logout`)
      cookie.deleteCookie(COOKIE_KEYS.ACCESS_TOKEN)
    } catch (error) {
      console.error(error)
      throw error
    }
  }

  return {
    login,
    register,
    logout
  }
}

export default authService()
