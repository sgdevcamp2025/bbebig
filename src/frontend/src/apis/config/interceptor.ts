import * as Sentry from '@sentry/react'
import type { AxiosError, InternalAxiosRequestConfig } from 'axios'
import toast from 'react-hot-toast'

import { COOKIE_KEYS } from '@/constants/keys'
import cookie from '@/utils/cookie'

import { CommonResponseType } from '../schema/types/common'

export const checkAndSetToken = (
  config: InternalAxiosRequestConfig & { useAuth?: boolean }
): InternalAxiosRequestConfig => {
  if (config.useAuth === false) return config

  const cookieValue = cookie.getCookie(COOKIE_KEYS.ACCESS_TOKEN)

  if (!cookieValue) {
    window.location.href = '/login'
  }

  config.headers.Authorization = `Bearer ${cookieValue}`

  return config
}

export async function handleApiError(error: AxiosError) {
  const errorData = error.response?.data as CommonResponseType<{ message: string }>
  if (errorData && errorData.message) {
    toast.error(errorData.message)
  }
  Sentry.captureException(error)
  return Promise.reject(error)
}

export async function handleTokenError(error: AxiosError) {
  if (error.response?.status === 401) {
    cookie.deleteCookie(COOKIE_KEYS.ACCESS_TOKEN)
    window.location.href = '/login'
  }
  return Promise.reject(error)
}
