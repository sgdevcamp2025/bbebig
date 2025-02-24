import { z } from 'zod';

const commonResponseSchema = z.object({
  code: z.string(),
  message: z.string(),
  result: z.optional(z.any()),
});

const commonResponseSchemaOmitResult = commonResponseSchema.omit({ result: true });

export { commonResponseSchema, commonResponseSchemaOmitResult };
