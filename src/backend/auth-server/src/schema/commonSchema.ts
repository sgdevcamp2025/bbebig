import { z } from 'zod';

const commonHeaderSchema = z.object({
  Authorization: z.string(),
});

export { commonHeaderSchema };
