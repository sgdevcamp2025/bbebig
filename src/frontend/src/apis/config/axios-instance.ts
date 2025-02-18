import axios from 'axios'

import { SERVER_URL } from '@/constants/env'

import { checkAndSetToken, handleApiError, handleTokenError } from './interceptor'

const axiosInstance = axios.create({
  headers: {
    'Content-Type': 'application/json'
  },
  baseURL: SERVER_URL,
  withCredentials: true
})

axiosInstance.interceptors.request.use(checkAndSetToken, handleApiError)

axiosInstance.interceptors.response.use((response) => response, handleTokenError)

axiosInstance.interceptors.response.use((response) => response, handleApiError)

export default axiosInstance
