import * as Sentry from '@sentry/react'
import type { AxiosError, InternalAxiosRequestConfig } from 'axios'

import { COOKIE_KEYS } from '@/constants/keys'
import cookie from '@/utils/cookie'

export const checkAndSetToken = (
  config: InternalAxiosRequestConfig & { useAuth?: boolean }
): InternalAxiosRequestConfig => {
  if (config.useAuth === false) return config

  const cookieValue = cookie.getCookie(COOKIE_KEYS.ACCESS_TOKEN)

  if (!cookieValue) {
    throw new Error('토큰이 유효하지 않습니다.')
  }

  config.headers.Authorization = `Bearer ${cookieValue}`

  return config
}

export async function handleApiError(error: AxiosError) {
  Sentry.captureException(error)
  return Promise.reject(error)
}

export async function handleTokenError(error: AxiosError) {
  return Promise.reject(error)
}
