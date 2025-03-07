import { z } from 'zod'

import { loginRequestSchema, loginResponseSchema, registerRequestSchema } from '../auth'
import { CommonResponseType } from './common'
export type LoginSchema = z.infer<typeof loginRequestSchema>
export type LoginResponseSchema = z.infer<typeof loginResponseSchema>
export type RegisterSchema = z.infer<typeof registerRequestSchema>

export type StatusCheckResponseSchema = CommonResponseType<{
  status: boolean
}>
