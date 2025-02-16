import axios from 'axios'

import { SERVER_URL } from '@/constants/env'

import { errorInterceptor, requestInterceptor, responseInterceptor } from './axios-helper'

const axiosInstance = axios.create({
  headers: {
    'Content-Type': 'application/json'
  },
  baseURL: SERVER_URL,
  withCredentials: true
})

axiosInstance.interceptors.request.use(requestInterceptor)

axiosInstance.interceptors.response.use(responseInterceptor, errorInterceptor)

export default axiosInstance
