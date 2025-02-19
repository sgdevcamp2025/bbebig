import { useSuspenseQuery } from '@tanstack/react-query'

import { CommonResponseType } from '@/apis/schema/types/common'
import { GetFriendListResponseSchema } from '@/apis/schema/types/user'
import userService from '@/apis/service/user'
import { UseQueryCustomOptions } from '@/types/common'

function useGetFriendList(
  config?: UseQueryCustomOptions<CommonResponseType<GetFriendListResponseSchema>>
) {
  const { data } = useSuspenseQuery({
    ...config,
    queryKey: ['friend', 'list'],
    queryFn: userService.getFriendList
  })

  return data.result
}

export default useGetFriendList
