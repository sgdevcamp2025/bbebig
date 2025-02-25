import { AxiosRequestConfig } from 'axios'

import { COOKIE_KEYS } from '@/constants/keys'
import cookie from '@/utils/cookie'

import axiosInstance from '../config/axios-instance'
import {
  LoginResponseSchema,
  LoginSchema,
  RegisterSchema,
  StatusCheckResponseSchema
} from '../schema/types/auth'
const BASE_PATH = `/auth-server/auth`

export interface CustomAxiosRequestConfig extends AxiosRequestConfig {
  useAuth?: boolean
}

const authService = () => {
  const statusCheck = async () => {
    const res = await axiosInstance.get<StatusCheckResponseSchema>(`${BASE_PATH}/status-check`)
    return res.data
  }

  const login = async (data: LoginSchema) => {
    const res = await axiosInstance.post<LoginResponseSchema>(`${BASE_PATH}/login`, data, {
      useAuth: false
    } as CustomAxiosRequestConfig)
    const accessToken = res.data.result.accessToken
    cookie.setCookie(COOKIE_KEYS.ACCESS_TOKEN, accessToken)
  }

  const register = async (data: RegisterSchema) => {
    const response = await axiosInstance.post(`${BASE_PATH}/register`, data, {
      useAuth: false
    } as CustomAxiosRequestConfig)
    return response.data
  }

  const logout = async () => {
    try {
      await axiosInstance.post(`${BASE_PATH}/logout`)
    } catch (error) {
      console.error(error)
    } finally {
      cookie.deleteCookie(COOKIE_KEYS.ACCESS_TOKEN)
    }
  }

  const refreshToken = async () => {
    const response = await axiosInstance.post(`${BASE_PATH}/refresh`)
    return response.data
  }

  return {
    login,
    register,
    logout,
    refreshToken,
    statusCheck
  }
}

export default authService()
