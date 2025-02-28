import { useSuspenseQuery } from '@tanstack/react-query'

import { CommonResponseType } from '@/apis/schema/types/common'
import {
  GetHistoryChattingMessageResponseSchema,
  GetHistoryChattingMessagesRequestSchema
} from '@/apis/schema/types/search'
import searchService from '@/apis/service/search'
import { UseQueryCustomOptions } from '@/types/common'

export const useGetHistoryChattingMessages = (
  params: GetHistoryChattingMessagesRequestSchema,
  config?: UseQueryCustomOptions<CommonResponseType<GetHistoryChattingMessageResponseSchema>>
) => {
  const { data } = useSuspenseQuery({
    ...config,
    queryKey: ['historyChattingMessages', params.channelId],
    queryFn: () => searchService.getHistoryChattingMessages(params)
  })

  return data.result
}
