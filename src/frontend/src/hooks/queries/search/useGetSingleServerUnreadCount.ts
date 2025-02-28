import { useSuspenseQuery } from '@tanstack/react-query'

import { CommonResponseType } from '@/apis/schema/types/common'
import {
  GetSingleServerUnreadCountRequestSchema,
  GetSingleServerUnreadCountResponseSchema
} from '@/apis/schema/types/search'
import searchService from '@/apis/service/search'
import { UseQueryCustomOptions } from '@/types/common'

export const useGetSingleServerUnreadCount = (
  params: GetSingleServerUnreadCountRequestSchema,
  config?: UseQueryCustomOptions<CommonResponseType<GetSingleServerUnreadCountResponseSchema>>
) => {
  const { data } = useSuspenseQuery({
    ...config,
    queryKey: ['unreadMessageCounts', params.memberId, params.serverId],
    queryFn: () => searchService.getSingleServerUnreadCount(params)
  })

  return data.result
}
