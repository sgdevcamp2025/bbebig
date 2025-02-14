import { InternalAxiosRequestConfig } from 'axios'

import { COOKIE_KEYS } from '@/constants/keys'
import cookie from '@/utils/cookie'

export function axiosInterceptorHelper(config: InternalAxiosRequestConfig) {
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
