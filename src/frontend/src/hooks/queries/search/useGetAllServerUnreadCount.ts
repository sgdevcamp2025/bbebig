import { useSuspenseQuery } from '@tanstack/react-query'

import { CommonResponseType } from '@/apis/schema/types/common'
import {
  GetAllServersUnreadCountRequestSchema,
  GetAllServerUnreadCountResponseSchema
} from '@/apis/schema/types/search'
import searchService from '@/apis/service/search'
import { UseQueryCustomOptions } from '@/types/common'

export const useGetAllServerUnreadCount = (
  params: GetAllServersUnreadCountRequestSchema,
  config?: UseQueryCustomOptions<CommonResponseType<GetAllServerUnreadCountResponseSchema>>
) => {
  const { data } = useSuspenseQuery({
    ...config,
    queryKey: ['unreadMessageCounts', params.memberId],
    queryFn: () => searchService.getAllServersUnreadCount()
  })

  return data.result
}
