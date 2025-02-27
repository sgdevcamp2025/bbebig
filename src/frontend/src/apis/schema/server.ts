import { z } from 'zod'

import { commonResponseSchema } from './common'

const createChannelRequestSchema = z.object({
  serverId: z.string(),
  categoryId: z.number(),
  channelType: z.string(),
  channelName: z.string().min(1, { message: '채널 이름은 최소 1글자 이상이어야 합니다.' }),
  privateStatus: z.boolean(),
  memberIds: z.array(z.number().nullable())
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

const inviteUserRequestSchema = z.object({
  serverId: z.string(),
  inviteUserName: z.string().min(1, { message: '초대할 유저 이름은 최소 1글자 이상이어야 합니다.' })
})

const inviteUserResponseSchema = commonResponseSchema.extend({
  result: z.object({
    id: z.number(),
    name: z.string(),
    nickName: z.string(),
    email: z.string(),
    avatarUrl: z.string().nullable(),
    bannerUrl: z.string().nullable(),
    introduce: z.string().nullable(),
    customPresenceStatus: z.string().nullable(),
    lastAccessAt: z.string().nullable()
  })
})

export {
  createCategoryRequestSchema,
  createCategoryResponseSchema,
  createChannelRequestSchema,
  createChannelResponseSchema,
  inviteUserRequestSchema,
  inviteUserResponseSchema
}
