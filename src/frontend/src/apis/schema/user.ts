import { z } from 'zod'

import { commonResponseSchema } from './common'

export const inviteUserRequestSchema = z.object({
  serverId: z.string(),
  inviteUserName: z.string().min(1, { message: '초대할 유저 이름은 최소 1글자 이상이어야 합니다.' })
})

export const inviteUserResponseSchema = commonResponseSchema.extend({
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
