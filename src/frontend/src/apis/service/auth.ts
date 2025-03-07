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

interface CustomAxiosRequestConfig extends AxiosRequestConfig {
  useAuth?: boolean
}

const authService = () => {
  const statusCheck = async () => {
    const res = await axiosInstance.get<StatusCheckResponseSchema>(`${BASE_PATH}/status-check`, {
      headers: {
        Authorization: `Bearer ${cookie.getCookie(COOKIE_KEYS.ACCESS_TOKEN)}`,
        'refresh-token': `${cookie.getCookie(COOKIE_KEYS.REFRESH_TOKEN)}`
      },
      useAuth: false
    } as CustomAxiosRequestConfig)

    if (!res.data.result.status) {
      cookie.deleteCookie(COOKIE_KEYS.ACCESS_TOKEN)
      cookie.deleteCookie(COOKIE_KEYS.REFRESH_TOKEN)
    }

    return res.data
  }

  const login = async (data: LoginSchema) => {
    const res = await axiosInstance.post<LoginResponseSchema>(`${BASE_PATH}/login`, data, {
      useAuth: false
    } as CustomAxiosRequestConfig)
    const accessToken = res.data.result.accessToken
    const refreshToken = res.data.result.refreshToken
    cookie.setCookie(COOKIE_KEYS.ACCESS_TOKEN, accessToken)
    cookie.setCookie(COOKIE_KEYS.REFRESH_TOKEN, refreshToken)
  }

  const register = async (data: RegisterSchema) => {
    const response = await axiosInstance.post(`${BASE_PATH}/register`, data, {
      useAuth: false
    } as CustomAxiosRequestConfig)
    return response.data
  }

  const logout = async () => {
    try {
      await axiosInstance.post(`${BASE_PATH}/logout`, {}, {
        headers: {
          Authorization: `Bearer ${cookie.getCookie(COOKIE_KEYS.ACCESS_TOKEN)}`,
          'refresh-token': `${cookie.getCookie(COOKIE_KEYS.REFRESH_TOKEN)}`
        },
        useAuth: false
      } as CustomAxiosRequestConfig)
    } catch (error) {
      console.error(error)
    } finally {
      cookie.deleteCookie(COOKIE_KEYS.ACCESS_TOKEN)
      cookie.deleteCookie(COOKIE_KEYS.REFRESH_TOKEN)
    }
  }

  const refreshToken = async () => {
    const response = await axiosInstance.post(`${BASE_PATH}/refresh`, {}, {
      headers: {
        'refresh-token': `${cookie.getCookie(COOKIE_KEYS.REFRESH_TOKEN)}`
      },
      useAuth: false
    } as CustomAxiosRequestConfig)
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
