import axios from 'axios'

import { SERVER_URL } from '@/constants/env'

import { errorInterceptor, requestInterceptor, responseInterceptor } from './axios-helper'

const axiosInstance = axios.create({
  headers: {
    'Content-Type': 'application/json',
    Authorization: `Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6MTAsImlhdCI6MTczOTY3MTk5NCwiZXhwIjoxNzM5Njc1NTk0fQ.u1kL4vdkly3xTprljuyucavFofwI1ldUL17oScpYYHE`
  },
  baseURL: SERVER_URL,
  withCredentials: true
})

axiosInstance.interceptors.request.use(requestInterceptor)

axiosInstance.interceptors.response.use(responseInterceptor, errorInterceptor)

export default axiosInstance
