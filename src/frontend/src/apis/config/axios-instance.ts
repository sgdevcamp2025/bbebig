import axios from 'axios'

import SERVER_PORT from '@/constants/base-port'
import { SERVER_URL } from '@/constants/env'
import { COOKIE_KEYS } from '@/constants/keys'
import cookie from '@/utils/cookie'

const getAxiosInstance = (port: (typeof SERVER_PORT)[keyof typeof SERVER_PORT]) => {
  const axiosInstance = axios.create({
    baseURL: `${SERVER_URL}:${port}`,
    withCredentials: true
  })

  axiosInstance.interceptors.request.use((config) => {
    const accessToken = cookie.getCookie(COOKIE_KEYS.ACCESS_TOKEN)

    if (accessToken) {
      config.headers.Authorization = `Bearer ${accessToken}`
    }

    return config
  })

  axiosInstance.interceptors.response.use(
    (response) => {
      return response
    },
    (error) => {
      return Promise.reject(error)
    }
  )

  return axiosInstance
}

export default getAxiosInstance
