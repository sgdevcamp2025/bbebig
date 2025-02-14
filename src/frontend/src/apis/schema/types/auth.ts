import { z } from 'zod'

import {
  loginRequestSchema,
  loginResponseSchema,
  registerRequestSchema,
  registerResponseSchema
} from '../auth'

export type LoginSchema = z.infer<typeof loginRequestSchema>
export type LoginResponseSchema = z.infer<typeof loginResponseSchema>
export type RegisterSchema = z.infer<typeof registerRequestSchema>
export type RegisterResponseSchema = z.infer<typeof registerResponseSchema>
