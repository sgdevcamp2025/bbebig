import { z } from 'zod';
import { commonHeaderSchema, commonResponseSchema } from './commonSchema';

const headers = commonHeaderSchema;

const signInSchema = {
  tags: ['auth'],
  description: '로그인 합니다.',
  body: z.object({
    email: z.string().email(),
    password: z.string(),
  }),
  response: {
    200: z.object({
      code: z.number(),
      message: z.string(),
      result: z.object({
        accessToken: z.string(),
      }),
    }),
    400: commonResponseSchema,
  },
};

const registerSchema = {
  tags: ['auth'],
  description: '회원가입 합니다.',
  body: z.object({
    email: z.string().email('이메일 형식이 올바르지 않습니다.'),
    password: z
      .string()
      .min(8, '비밀번호는 8자 이상이어야 합니다.')
      .max(20, '비밀번호는 20자 이하여야 합니다.'),
    name: z
      .string()
      .min(2, '이름은 2자 이상이어야 합니다.')
      .max(20, '이름은 20자 이하여야 합니다.'),
    nickname: z
      .string()
      .min(2, '닉네임은 2자 이상이어야 합니다.')
      .max(20, '닉네임은 20자 이하여야 합니다.'),
    birthDate: z
      .string()
      .regex(/^\d{4}-\d{2}-\d{2}$/, '생년월일은 YYYY-MM-DD 형식이어야 합니다.')
      .transform((str) => new Date(str))
      .refine((date) => date <= new Date(), {
        message: '생년월일은 현재 날짜 이전이어야 합니다.',
      })
      .refine((date) => date.getFullYear() >= 1900, {
        message: '생년월일은 1900년 이후여야 합니다.',
      }),
  }),
  response: {
    201: commonResponseSchema,
    400: commonResponseSchema,
  },
};

const refreshTokenSchema = {
  tags: ['auth'],
  description: '리프레시 토큰을 발급 받습니다.',
  response: {
    201: z.object({
      accessToken: z.string(),
    }),
    400: commonResponseSchema,
  },
};

const logoutSchema = {
  tags: ['auth'],
  description: '로그아웃 합니다.',
  headers,
  response: {
    205: commonResponseSchema,
    400: commonResponseSchema,
  },
};

const verifyTokenSchema = {
  tags: ['auth'],
  description: '토큰을 검증 받습니다.',
  headers,
  response: {
    200: commonResponseSchema,
    401: commonResponseSchema,
  },
};

const verifyEmailSchema = {
  tags: ['auth'],
  description: '이메일 검증 합니다.',
  body: z.object({
    email: z.string().email(),
  }),
  response: {
    200: commonResponseSchema,
    400: commonResponseSchema,
  },
};

export {
  logoutSchema,
  refreshTokenSchema,
  verifyTokenSchema,
  registerSchema,
  signInSchema,
  verifyEmailSchema,
};
