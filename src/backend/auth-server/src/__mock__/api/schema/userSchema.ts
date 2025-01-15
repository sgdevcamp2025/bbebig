import { z } from 'zod';
import { commonHeaderSchema } from '../../../schema/commonSchema';

// 공통 헤더 스키마
const header = commonHeaderSchema;

// 공통 응답 스키마 정의
const successResponse = z.object({
  status: z.number(),
  message: z.string(),
  success: z.boolean(),
});

const errorResponse = z.object({
  status: z.number(),
  message: z.string(),
  success: z.boolean(),
  details: z.optional(z.any()), // 추가 디버깅 정보
});

// 사용자 요청 스키마
const userSchema = z.object({
  email: z.string().email(),
  password: z.string().min(6, 'Password must be at least 6 characters'),
});

// 로그인 스키마
const loginSchema = {
  header,
  body: userSchema,
  tags: ['user'],
  description: '유저 로그인',
  response: {
    200: z.object({
      id: z.string(),
      email: z.string(),
    }),
    400: errorResponse, // 잘못된 요청
    404: errorResponse, // 사용자 없음
  },
};

// 회원가입 스키마
const signupSchema = {
  header,
  body: userSchema,
  tags: ['user'],
  description: '유저 회원가입',
  response: {
    201: successResponse.extend({
      status: z.literal(201),
    }),
    400: errorResponse, // 이미 존재하는 사용자
  },
};

// 로그아웃 스키마
const logoutSchema = {
  header,
  tags: ['user'],
  description: '유저 로그아웃',
  response: {
    205: successResponse.extend({
      status: z.literal(205),
    }),
    400: errorResponse, // 잘못된 요청
    401: errorResponse, // 인증 실패
  },
};

export { loginSchema, signupSchema, logoutSchema };
