import { z } from 'zod';
import { commonHeaderSchema, commonResponseSchema } from './commonSchema';

const headers = commonHeaderSchema;

const signInSchema = {
  tags: ['auth'],
  description: '로그인 합니다.',
  body: z.object({
    email: z.string().email(),
    password: z.string().min(8),
  }),
  response: {
    200: z.object({
      code: z.number(),
      message: z.string(),
      result: z.object({
        email: z.string(),
        accessToken: z.string(),
        nickname: z.string(),
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
    birthDate: z
      .string()
      .regex(/^\d{4}-\d{2}-\d{2}$/)
      .transform((str) => new Date(str)),
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
