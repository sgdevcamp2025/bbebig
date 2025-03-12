import { http, HttpResponse } from 'msw'

import { LoginSchema } from '@/apis/schema/types/auth'
import { SERVER_URL } from '@/constants/env'

import {
  healthCheckMock,
  loginMock,
  logoutMock,
  refreshTokenMock,
  registerMock,
  statusCheckMock,
  verifyTokenMock
} from '../data/auth.mock'

const mockUser = [
  {
    email: 'test@test1.com',
    password: '12341234'
  },
  {
    email: 'test@test2.com',
    password: '12341234'
  }
] as const

export const authHandler = [
  http.get(`${SERVER_URL}/auth-server/auth/status-check`, () => {
    return new HttpResponse(JSON.stringify(statusCheckMock))
  }),
  http.post(`${SERVER_URL}/auth-server/auth/register`, () => {
    return new HttpResponse(JSON.stringify(registerMock))
  }),
  http.post(`${SERVER_URL}/auth-server/auth/login`, async ({ request }) => {
    const body = (await request.json()) as LoginSchema
    const user = mockUser.find(
      (user) => user.email === body.email && user.password === body.password
    )
    if (user) {
      return new HttpResponse(JSON.stringify(loginMock))
    }
    return new HttpResponse(
      JSON.stringify({
        code: 'AUTH103',
        message: 'login failed'
      }),
      { status: 401 }
    )
  }),
  http.post(`${SERVER_URL}/auth-server/auth/logout`, () => {
    return new HttpResponse(JSON.stringify(logoutMock))
  }),
  http.post(`${SERVER_URL}/auth-server/auth/refresh`, () => {
    return new HttpResponse(JSON.stringify(refreshTokenMock))
  }),
  http.post(`${SERVER_URL}/auth-server/auth/verify-token`, () => {
    return new HttpResponse(JSON.stringify(verifyTokenMock))
  }),
  http.get(`${SERVER_URL}/auth-server/auth/health-check`, () => {
    return new HttpResponse(JSON.stringify(healthCheckMock))
  })
]
