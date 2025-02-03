import { z } from 'zod'

import { commonResponseSchema } from './common'

const loginRequestSchema = z.object({
  email: z.string().email(),
  password: z
    .string()
    .min(8, { message: '8자 이상 입력해주세요.' })
    .max(20, { message: '20자 이하로 입력해주세요.' })
})

const loginResponseSchema = commonResponseSchema.extend({
  result: z.object({
    accessToken: z.string()
  })
})

const registerRequestSchema = z.object({
  email: z.string().email('이메일 형식이 올바르지 않습니다.'),
  name: z.string().min(2, '이름은 2자 이상이어야 합니다.').max(20, '이름은 20자 이하여야 합니다.'),
  nickname: z
    .string()
    .min(2, '닉네임은 2자 이상이어야 합니다.')
    .max(20, '닉네임은 20자 이하여야 합니다.'),
  password: z
    .string()
    .min(8, { message: '8자 이상 입력해주세요.' })
    .max(20, { message: '20자 이하로 입력해주세요.' }),
  birthDate: z.string().date()
})

const registerResponseSchema = commonResponseSchema

export { loginRequestSchema, loginResponseSchema, registerRequestSchema, registerResponseSchema }
