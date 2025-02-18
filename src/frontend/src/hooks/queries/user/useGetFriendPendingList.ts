import { useSuspenseQuery } from '@tanstack/react-query'

import { CommonResponseType } from '@/apis/schema/types/common'
import { GetFriendPendingListResponseSchema } from '@/apis/schema/types/user'
import userService from '@/apis/service/user'
import { UseQueryCustomOptions } from '@/types/common'

export const useGetFriendPendingList = (
  config?: UseQueryCustomOptions<CommonResponseType<GetFriendPendingListResponseSchema>>
) => {
  const { data } = useSuspenseQuery({
    ...config,
    queryKey: ['friend', 'pending'],
    queryFn: userService.getFriendPendingList
  })

  return data.result
}
