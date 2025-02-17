import * as Sentry from '@sentry/react'
import { AxiosRequestConfig } from 'axios'

import { COOKIE_KEYS } from '@/constants/keys'
import cookie from '@/utils/cookie'

import axiosInstance from '../config/axios-instance'
import { LoginResponseSchema, LoginSchema, RegisterSchema } from '../schema/types/auth'

const BASE_PATH = `/auth-server/auth`

export interface CustomAxiosRequestConfig extends AxiosRequestConfig {
  useAuth?: boolean
}

const authService = () => {
  const login = async (data: LoginSchema) => {
    try {
      const res = await axiosInstance.post<LoginResponseSchema>(`${BASE_PATH}/login`, data, {
        useAuth: false
      } as CustomAxiosRequestConfig)
      const accessToken = res.data.result.accessToken
      cookie.setCookie(COOKIE_KEYS.ACCESS_TOKEN, accessToken)
    } catch (error) {
      Sentry.captureException(error)
      throw error
    }
  }

  const register = async (data: RegisterSchema) => {
    try {
      const response = await axiosInstance.post(`${BASE_PATH}/register`, data, {
        useAuth: false
      } as CustomAxiosRequestConfig)
      return response.data
    } catch (error) {
      Sentry.captureException(error)
      throw error
    }
  }

  const logout = async () => {
    try {
      await axiosInstance.post(`${BASE_PATH}/logout`)
      cookie.deleteCookie(COOKIE_KEYS.ACCESS_TOKEN)
    } catch (error) {
      Sentry.captureException(error)
      throw error
    }
  }

  const refreshToken = async () => {
    try {
      const response = await axiosInstance.post(`${BASE_PATH}/refresh`)
      return response.data
    } catch (error) {
      Sentry.captureException(error)
      throw error
    }
  }
  return {
    login,
    register,
    logout,
    refreshToken
  }
}

export default authService()
