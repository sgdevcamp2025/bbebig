import { z } from 'zod'

import { commonResponseSchema } from '../common'

export type CommonResponseSchema = z.infer<typeof commonResponseSchema>
