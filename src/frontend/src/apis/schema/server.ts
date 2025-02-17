import { z } from 'zod'

import { commonResponseSchema } from './common'

const createChannelRequestSchema = z.object({
  serverId: z.string(),
  categoryId: z.string().nullable(),
  channelType: z.string(),
  channelName: z.string().min(1, { message: '채널 이름은 최소 1글자 이상이어야 합니다.' }),
  privateStatus: z.boolean(),
  memberIds: z.array(z.string())
})

const createChannelResponseSchema = commonResponseSchema.extend({
  result: z.object({
    channelId: z.string()
  })
})

const createCategoryRequestSchema = z.object({
  serverId: z.string(),
  categoryName: z.string().min(1, { message: '카테고리 이름은 최소 1글자 이상이어야 합니다.' })
})

const createCategoryResponseSchema = commonResponseSchema.extend({
  result: z.object({
    categoryId: z.string()
  })
})

export {
  createCategoryRequestSchema,
  createCategoryResponseSchema,
  createChannelRequestSchema,
  createChannelResponseSchema
}
