import { CommonResponseType } from '@/apis/schema/types/common'

export const registerMock: CommonResponseType<{ id: string; name: string }> = {
  code: 'AUTH100',
  message: 'Register OK',
  result: {
    id: '1',
    name: 'John Doe'
  }
}

export const loginMock: CommonResponseType<{ accessToken: string }> = {
  code: 'AUTH100',
  message: 'Login OK',
  result: {
    accessToken:
      'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6MSwiaWF0IjoxNzQwMzcwODQ5LCJleHAiOjE3NDAzNzA4NTl9.7bQ_NfH38iBCJIJVzlnrH5WepVBh1vrpaC3sOgfWWRM'
  }
}
