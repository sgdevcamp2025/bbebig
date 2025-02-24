import { http, HttpResponse } from 'msw'

import { SERVER_URL } from '@/constants/env'

import { loginMock, registerMock } from '../data/auth.mock'

export const authHandler = [
  http.post(`${SERVER_URL}/auth-server/auth/register`, () => {
    return new HttpResponse(JSON.stringify(registerMock), {
      headers: {
        'Set-Cookie': 'refreshToken=1234567890; HttpOnly; Secure; SameSite=Strict'
      }
    })
  }),
  http.post(`${SERVER_URL}/auth-server/auth/login`, () => {
    return new HttpResponse(JSON.stringify(loginMock), {
      headers: {
        'Set-Cookie': 'refreshToken=1234567890; HttpOnly; Secure; SameSite=Strict'
      }
    })
  })
]
