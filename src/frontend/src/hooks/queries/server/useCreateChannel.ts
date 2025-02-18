import { useMutation, useQueryClient } from '@tanstack/react-query'

import { CommonResponseType } from '@/apis/schema/types/common'
import {
  CreateChannelResponseSchema,
  ZCreateChannelRequestSchema
} from '@/apis/schema/types/service'
import serviceService from '@/apis/service/service'
import { UseMutationCustomOptions } from '@/types/common'
import { ChannelType } from '@/types/server'

export const useCreateChannel = (
  config?: UseMutationCustomOptions<
    CommonResponseType<CreateChannelResponseSchema>,
    ZCreateChannelRequestSchema
  >
) => {
  const queryClient = useQueryClient()

  const { mutate: createChannel } = useMutation({
    ...config,
    mutationFn: (data: ZCreateChannelRequestSchema) => {
      return serviceService.createChannel({
        serverId: Number(data.serverId),
        categoryId: data.categoryId ? Number(data.categoryId) : null,
        channelType: data.channelType as ChannelType,
        channelName: data.channelName,
        privateStatus: data.privateStatus,
        memberIds: data.memberIds.map(Number)
      })
    },
    onSuccess: (_data, variables) => {
      queryClient.invalidateQueries({ queryKey: ['server', variables.serverId.toString()] })
    }
  })

  return createChannel
}
