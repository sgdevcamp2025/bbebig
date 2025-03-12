import { LoginResponseSchema } from '@/apis/schema/types/auth'

export const statusCheckMock = {
  code: 'AUTH109',
  message: 'login status check success!',
  result: {
    status: true
  }
}

export const registerMock = {
  code: 'AUTH104',
  message: 'register success!'
}

export const loginMock: LoginResponseSchema = {
  code: 'AUTH100',
  message: 'Login OK',
  result: {
    accessToken:
      'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6MSwiaWF0IjoxNzQwMzcwODQ5LCJleHAiOjE3NDAzNzA4NTl9.7bQ_NfH38iBCJIJVzlnrH5WepVBh1vrpaC3sOgfWWRM',
    refreshToken:
      'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6MSwiaWF0IjoxNzQwMzcwODQ5LCJleHAiOjE3NDAzNzA4NTl9.7bQ_NfH38iBCJIJVzlnrH5WepVBh1vrpaC3sOgfWWRM'
  }
}

export const logoutMock = { code: 'AUTH101', message: 'Logout success!' }

export const refreshTokenMock = {
  code: 'AUTH102',
  message: 'refresh success',
  result: {
    accessToken:
      'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6MSwiaWF0IjoxNzQwMzcwODQ5LCJleHAiOjE3NDAzNzA4NTl9.7bQ_NfH38iBCJIJVzlnrH5WepVBh1vrpaC3sOgfWWRM',
    refreshToken:
      'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6MSwiaWF0IjoxNzQwMzcwODQ5LCJleHAiOjE3NDAzNzA4NTl9.7bQ_NfH38iBCJIJVzlnrH5WepVBh1vrpaC3sOgfWWRM'
  }
}

export const verifyTokenMock = {
  code: 'AUTH105',
  message: 'token verify success!'
}

export const healthCheckMock = {
  code: 'AUTH108',
  message: 'health check success!'
}
