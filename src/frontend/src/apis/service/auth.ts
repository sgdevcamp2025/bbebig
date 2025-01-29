import axiosInstance from '../config/axios-instance'
import { LoginResponseSchema, LoginSchema, RegisterSchema } from '../schema/types/auth'

const AUTH_BASE_PATH = '/auth-server'

const authService = () => {
  const login = async (data: LoginSchema) => {
    try {
      const res = await axiosInstance.post<LoginResponseSchema>(`${AUTH_BASE_PATH}/login`, data)
    } catch (error) {
      console.error(error)
      throw error
    }
  }

  const register = async (data: RegisterSchema) => {
    try {
      const response = await axiosInstance.post(`${AUTH_BASE_PATH}/register`, data)
      return response.data
    } catch (error) {
      console.error(error)
      throw error
    }
  }

  return {
    login,
    register
  }
}

export default authService()
