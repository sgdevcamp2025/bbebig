import { z } from 'zod';

const commonHeaderSchema = z.object({
  Authorization: z.string(),
});

const commonResponseSchema = z.object({
  code: z.number(),
  message: z.string(),
  isSuccess: z.boolean(),
  details: z.optional(z.any()),
});

export { commonHeaderSchema, commonResponseSchema };
