import { z } from 'zod'

import {
  loginRequestSchema,
  loginResponseSchema,
  registerRequestSchema,
  registerResponseSchema
} from '../auth'
import { CommonResponseType } from './common'
export type LoginSchema = z.infer<typeof loginRequestSchema>
export type LoginResponseSchema = z.infer<typeof loginResponseSchema>
export type RegisterSchema = z.infer<typeof registerRequestSchema>
export type RegisterResponseSchema = z.infer<typeof registerResponseSchema>

export type StatusCheckResponseSchema = CommonResponseType<{
  status: boolean
}>
