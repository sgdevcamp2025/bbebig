import { z } from 'zod';

const accessTokenSchema = {
  tags: ['auth'],
  description: '엑세스 토큰을 발급 받습니다.',
  body: z.object({
    id: z.string(),
    email: z.string(),
  }),
  response: {
    201: z.object({
      accessToken: z.string(),
      refreshToken: z.string(),
    }),
  },
};

const refreshTokenSchema = {
  tags: ['auth'],
  description: '리프레시 토큰을 발급 받습니다.',
  body: z.object({
    refreshToken: z.string(),
  }),
  response: {
    201: z.object({
      accessToken: z.string(),
      refreshToken: z.string(),
    }),
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

export { accessTokenSchema, refreshTokenSchema, checkTokenSchema };
