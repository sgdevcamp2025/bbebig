import type { AxiosError, AxiosResponse, InternalAxiosRequestConfig } from 'axios'

import { COOKIE_KEYS } from '@/constants/keys'
import cookie from '@/utils/cookie'

export const requestInterceptor = (
  config: InternalAxiosRequestConfig & { useAuth?: boolean }
): InternalAxiosRequestConfig => {
  if (config.useAuth === false) return config

  const cookieValue = cookie.getCookie(COOKIE_KEYS.ACCESS_TOKEN)

  if (cookieValue) {
    config.headers.Authorization = `Bearer ${cookieValue}`
  }

  return config
}

export function responseInterceptor(response: AxiosResponse) {
  return response
}

export function errorInterceptor(error: AxiosError) {
  return Promise.reject(error)
}
