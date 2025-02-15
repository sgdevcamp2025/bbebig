import { AxiosError, AxiosResponse, InternalAxiosRequestConfig } from 'axios'

import { COOKIE_KEYS } from '@/constants/keys'
import cookie from '@/utils/cookie'

export function requestInterceptor(config: InternalAxiosRequestConfig) {
  const cookieValue = cookie.getCookie(COOKIE_KEYS.ACCESS_TOKEN)

  if (cookieValue) {
    config.headers.Authorization = `Bearer ${cookieValue}`
  } else {
    const localStorageValue = localStorage.getItem(COOKIE_KEYS.ACCESS_TOKEN)
    if (localStorageValue) {
      config.headers.Authorization = `Bearer ${localStorageValue}`
    }
  }

  return config
}

export function responseInterceptor(response: AxiosResponse) {
  return response
}

export function errorInterceptor(error: AxiosError) {
  return Promise.reject(error)
}
