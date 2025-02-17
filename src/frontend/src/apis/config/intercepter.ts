import * as Sentry from '@sentry/react'
import type { AxiosError, InternalAxiosRequestConfig } from 'axios'
import toast from 'react-hot-toast'

import authService from '@/apis/service/auth'
import { ERROR_CODE, ERROR_MESSAGE } from '@/constants/error'
import { COOKIE_KEYS } from '@/constants/keys'
import cookie from '@/utils/cookie'

import axiosInstance from './axios-instance'
export const checkAndSetToken = (
  config: InternalAxiosRequestConfig & { useAuth?: boolean }
): InternalAxiosRequestConfig => {
  if (config.useAuth === false) return config

  const cookieValue = cookie.getCookie(COOKIE_KEYS.ACCESS_TOKEN)

  if (!cookieValue) {
    window.location.href = '/login'
    throw new Error('토큰이 유효하지 않습니다.')
  }
  config.headers.Authorization = `Bearer ${cookieValue}`

  return config
}

export async function handleApiError(error: AxiosError) {
  Sentry.captureException(error)
  const originalRequest = error.config

  if (!error.response || !originalRequest) {
    throw new Error(ERROR_MESSAGE.API_ERROR)
  }

  const { status } = error.response

  if (status !== ERROR_CODE.TOKEN_EXPIRED) {
    toast.error(ERROR_MESSAGE.API_ERROR)
  }

  if (status === ERROR_CODE.TOKEN_EXPIRED) {
    const newAccessToken = await authService.refreshToken()
    originalRequest.headers.Authorization = `Bearer ${newAccessToken}`
    return axiosInstance(originalRequest)
  }

  if (status === ERROR_CODE.TOKEN_INVALID) {
    cookie.deleteCookie(COOKIE_KEYS.ACCESS_TOKEN)
    window.location.href = '/login'
  }

  return Promise.reject(error)
}

export async function handleTokenError(error: AxiosError) {
  if (error.response?.status === 401) {
    const response = await authService.refreshToken()
    console.log(response)
  }
  return Promise.reject(error)
}
