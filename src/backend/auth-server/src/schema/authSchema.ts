import { z } from 'zod';
import { commonResponseSchema, commonResponseSchemaOmitResult } from './commonSchema';

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
        refreshToken: z.string(),
      }),
    }),
    400: commonResponseSchemaOmitResult,
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
      .refine(
        (val) => {
          const date = new Date(val);
          return !isNaN(date.getTime());
        },
        {
          message: '유효하지 않은 날짜 형식입니다.',
        },
      ),
  }),
  response: {
    201: z.object({
      code: z.string().default('AUTH104'),
      message: z.string().default('register success!'),
    }),
    400: commonResponseSchemaOmitResult,
  },
};

const refreshTokenSchema = {
  tags: ['auth'],
  headers: z.object({
    'refresh-token': z.string(),
  }),
  security: [{ bearerAuth: [] }],
  response: {
    201: z.object({
      code: z.string().default('AUTH102'),
      message: z.string().default('refresh success'),
      result: z.object({
        accessToken: z.string(),
        refreshToken: z.string(),
      }),
    }),
    400: commonResponseSchemaOmitResult,
  },
  description: `
  리프레시 토큰은 쿠키('refresh_token')로 자동 처리됩니다.
  Swagger UI에서 테스트하려면 브라우저 쿠키가 있어야 합니다.
  1. 먼저 로그인하여 쿠키 설정
  2. 이 엔드포인트 호출하여 새 액세스 토큰 발급
`,
};

const logoutSchema = {
  tags: ['auth'],
  description: '로그아웃 합니다.',
  headers: z.object({
    'refresh-token': z.string(),
  }),
  security: [{ bearerAuth: [] }],
  response: {
    205: z.object({
      code: z.string().default('AUTH101'),
      message: z.string().default('Logout success!'),
    }),
    400: commonResponseSchemaOmitResult,
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
    401: commonResponseSchemaOmitResult,
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
    400: commonResponseSchemaOmitResult,
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
    400: commonResponseSchemaOmitResult,
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

const loginStatusCheckSchema = {
  tags: ['auth'],
  description: '로그인 상태를 확인 합니다.',
  security: [{ bearerAuth: [] }],
  response: {
    200: z.object({
      code: z.string().default('AUTH109'),
      message: z.string().default('login status check success!'),
      result: z.object({
        status: z.boolean(),
      }),
    }),
    401: commonResponseSchema,
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
  loginStatusCheckSchema,
};
