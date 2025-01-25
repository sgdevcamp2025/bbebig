import { z } from 'zod'

import { loginSchema, registerSchema } from '../auth'

export type LoginSchema = z.infer<typeof loginSchema>
export type RegisterSchema = z.infer<typeof registerSchema>
