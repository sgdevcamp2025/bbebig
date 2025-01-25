import { z } from 'zod'

const loginSchema = z.object({
  email: z.string().email(),
  password: z
    .string()
    .min(8, { message: '8자 이상 입력해주세요.' })
    .max(20, { message: '20자 이하로 입력해주세요.' })
})

const registerSchema = z.object({
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
  birthdate: z
    .string()
    .regex(/^\d{4}-\d{2}-\d{2}$/, '생년월일은 YYYY-MM-DD 형식이어야 합니다.')
    .transform((str) => new Date(str))
    .refine((date) => date <= new Date(), {
      message: '생년월일은 현재 날짜 이전이어야 합니다.'
    })
    .refine((date) => date.getFullYear() >= 1900, {
      message: '생년월일은 1900년 이후여야 합니다.'
    })
})

export { loginSchema, registerSchema }
