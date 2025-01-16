import { z } from 'zod';
import { commonResponseSchema } from './commonSchema';

const signInSchema = {
  tags: ['auth'],
  description: '로그인 합니다.',
  body: z.object({
    email: z.string().email(),
    password: z.string().min(8),
  }),
  response: {
    200: z.object({
      isSuccess: z.boolean(),
      code: z.number(),
      message: z.string(),
      details: z.object({
        email: z.string(),
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
    email: z.string().email(),
    password: z.string().min(8),
    name: z.string().min(2),
    nickname: z.string().min(2),
    birthdate: z.string(),
  }),
  response: {
    201: commonResponseSchema,
    400: commonResponseSchema,
  },
};

const accessTokenSchema = {
  tags: ['auth'],
  description: '엑세스 토큰을 발급 받습니다.',
  body: z.object({
    id: z.string(),
    email: z.string(),
  }),
  response: {
    201: z.object({
      id: z.string(),
      email: z.string(),
      accessToken: z.string(),
    }),
    400: commonResponseSchema,
  },
};

const refreshTokenSchema = {
  tags: ['auth'],
  description: '리프레시 토큰을 발급 받습니다.',
  headers: z.object({
    refreshToken: z.string(),
  }),
  response: {
    201: z.object({
      accessToken: z.string(),
    }),
    400: commonResponseSchema,
  },
};

const checkTokenSchema = {
  tags: ['auth'],
  description: '토큰을 검증 받습니다.',
  response: {
    200: z.object({
      isSuccess: z.boolean(),
    }),
    401: z.object({
      isSuccess: z.boolean(),
    }),
  },
};

export { accessTokenSchema, refreshTokenSchema, checkTokenSchema, registerSchema, signInSchema };
