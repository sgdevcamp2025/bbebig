import { z } from 'zod';

const commonHeaderSchema = z.object({
  authorization: z.string().regex(/^Bearer\s[\w-]+\.[\w-]+\.[\w-]+$/, {
    message: 'Invalid Authorization format. Must be "Bearer <JWT>"',
  }),
});

const commonResponseSchema = z.object({
  code: z.number(),
  message: z.string(),
  isSuccess: z.boolean(),
  details: z.optional(z.any()),
});

export { commonHeaderSchema, commonResponseSchema };
