import { z } from 'zod';

const commonHeaderSchema = z.object({
  Authorization: z.string(),
});

const commonResponseSchema = z.object({
  status: z.number(),
  message: z.string(),
  isSuccess: z.boolean(),
  details: z.optional(z.any()), // 추가 디버깅 정보
});

export { commonHeaderSchema, commonResponseSchema };
