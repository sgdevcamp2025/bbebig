import { z } from 'zod'

import { commonResponseSchema } from '../common'

export interface CommonResponseType<T> {
  code: string
  message: string
  result: T
}

export type CommonResponseSchema = z.infer<typeof commonResponseSchema>
