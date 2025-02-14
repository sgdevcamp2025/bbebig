import axios from 'axios'

import SERVER_PORT from '@/constants/base-port'
import { SERVER_URL } from '@/constants/env'

import { axiosInterceptorHelper } from './axios-helper'

const getAxiosInstance = (port: (typeof SERVER_PORT)[keyof typeof SERVER_PORT]) => {
  const axiosInstance = axios.create({
    headers: {
      'Content-Type': 'application/json'
    },
    baseURL: `${SERVER_URL}:${port}`,
    withCredentials: true
  })

  axiosInstance.interceptors.request.use(axiosInterceptorHelper)

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
