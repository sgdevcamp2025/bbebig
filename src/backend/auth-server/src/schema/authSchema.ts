import { z } from 'zod';
import { commonResponseSchema } from './commonSchema';

const signInSchema = {
  tags: ['auth'],
  description: '로그인 합니다.',
  body: z.object({
    email: z.string().email(),
    password: z.string(),
  }),
  response: {
    200: z.object({
      code: z.string().default('AUTH100'),
      message: z.string().default('Login Ok!'),
      result: z.object({
        accessToken: z.string(),
      }),
    }),
    400: z.object({
      code: z.enum(['AUTH001', 'AUTH003', 'AUTH009', 'AUTH012', 'AUTH013']),
      message: z.enum([
        'Bad Request',
        'Password Not Match',
        'Not Found',
        'Server Error',
        'Too Many Requests',
      ]),
    }),
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
    birthdate: z
      .string()
      .regex(/^\d{4}-\d{2}-\d{2}$/, '생년월일은 YYYY-MM-DD 형식이어야 합니다.')
      .refine((date) => new Date(date) <= new Date(), {
        message: '생년월일은 현재 날짜 이전이어야 합니다.',
      })
      .refine((date) => new Date(date).getFullYear() >= 1900, {
        message: '생년월일은 1900년 이후여야 합니다.',
      }),
  }),
  response: {
    201: z.object({
      code: z.string().default('AUTH104'),
      message: z.string().default('register success!'),
    }),
    400: z.object({
      code: z.enum(['AUTH002', 'AUTH008', 'AUTH010', 'AUTH012']),
      message: z.enum([
        'Duplicate Email',
        'Already Sign Up',
        'Precondition Failed',
        'Server Error',
      ]),
    }),
  },
};

const refreshTokenSchema = {
  tags: ['auth'],
  description: '리프레시 토큰을 발급 받습니다.',
  security: [{ bearerAuth: [] }],
  response: {
    201: z.object({
      code: z.string().default('AUTH102'),
      message: z.string().default('refresh success'),
      result: z.object({
        accessToken: z.string(),
      }),
    }),
    400: commonResponseSchema,
  },
};

const logoutSchema = {
  tags: ['auth'],
  description: '로그아웃 합니다.',
  security: [{ bearerAuth: [] }],
  response: {
    205: z.object({
      code: z.string().default('AUTH101'),
      message: z.string().default('Logout success!'),
    }),
    400: z.object({
      code: z.enum(['AUTH001', 'AUTH004', 'AUTH012', 'AUTH014']),
      message: z.enum([
        'Bad Request',
        'Unauthorized',
        'Server Error',
        'Authorization header is required',
      ]),
    }),
  },
};

const verifyTokenSchema = {
  tags: ['auth'],
  description: '토큰을 검증 받습니다.',
  security: [{ bearerAuth: [] }],
  response: {
    200: z.object({
      code: z.string().default('AUTH105'),
      message: z.string().default('token verify success!'),
    }),
    401: z.object({
      code: z.enum(['AUTH004', 'AUTH011', 'AUTH014']),
      message: z.enum(['Unauthorized', 'Verify Token Failed', 'Authorization header is required']),
    }),
  },
};

const verifyEmailSchema = {
  tags: ['auth'],
  description: '이메일 검증 합니다.',
  security: [{ bearerAuth: [] }],
  body: z.object({
    email: z.string().email(),
  }),
  response: {
    200: z.object({
      code: z.enum(['AUTH106']),
      message: z.enum(['email verify success!']),
    }),
    400: z.object({
      code: z.enum(['AUTH001', 'AUTH002', 'AUTH012', 'AUTH014']),
      message: z.enum([
        'Bad Request',
        'Duplicate Email',
        'Server Error',
        'Authorization header is required',
      ]),
    }),
  },
};

const tokenDecodeSchema = {
  tags: ['auth'],
  description: '토큰을 복호화 합니다.',
  security: [{ bearerAuth: [] }],
  response: {
    200: z.object({
      code: z.string().default('AUTH107'),
      message: z.string().default('token decode success!'),
      result: z.object({
        memberId: z.number(),
        valid: z.boolean(),
      }),
    }),
    400: z.object({
      code: z.enum(['AUTH001', 'AUTH004', 'AUTH012', 'AUTH014']),
      message: z.enum([
        'Bad Request',
        'Unauthorized',
        'Server Error',
        'Authorization header is required',
      ]),
      result: z.object({
        memberId: z.number().default(-1),
        valid: z.boolean().default(false),
      }),
    }),
  },
};

const healthCheckSchema = {
  tags: ['auth'],
  description: '서버 상태를 확인 합니다.',
  response: {
    200: z.object({
      code: z.string().default('AUTH108'),
      message: z.string().default('health check success!'),
    }),
  },
};

export {
  logoutSchema,
  refreshTokenSchema,
  verifyTokenSchema,
  registerSchema,
  signInSchema,
  verifyEmailSchema,
  tokenDecodeSchema,
  healthCheckSchema,
};
