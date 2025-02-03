import { z } from 'zod';

const commonHeaderSchema = z.object({
  authorization: z.string().regex(/^Bearer\s[\w-]+\.[\w-]+\.[\w-]+$/, {
    message: 'Invalid Authorization format. Must be "Bearer <JWT>"',
  }),
});

const commonResponseSchema = z.object({
  code: z.string(),
  message: z.string(),
  result: z.optional(z.any()),
});

export { commonHeaderSchema, commonResponseSchema };
