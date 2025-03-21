import { http, HttpResponse } from 'msw'

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

export const authHandler = [
  http.get(`${SERVER_URL}/auth-server/auth/status-check`, () => {
    return new HttpResponse(JSON.stringify(statusCheckMock))
  }),
  http.post(`${SERVER_URL}/auth-server/auth/register`, () => {
    return new HttpResponse(JSON.stringify(registerMock))
  }),
  http.post(`${SERVER_URL}/auth-server/auth/login`, () => {
    return new HttpResponse(JSON.stringify(loginMock))
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
