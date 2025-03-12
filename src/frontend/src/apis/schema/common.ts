import { z } from 'zod'

const commonResponseSchema = z.object({
  code: z.string(),
  message: z.string(),
  result: z.optional(z.any())
})

export { commonResponseSchema }
