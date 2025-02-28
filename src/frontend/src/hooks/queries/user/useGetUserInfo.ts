import { useSuspenseQuery } from '@tanstack/react-query'

import { CommonResponseType } from '@/apis/schema/types/common'
import { GetUserResponseSchema } from '@/apis/schema/types/user'
import userService from '@/apis/service/user'
import { UseQueryCustomOptions } from '@/types/common'

export const useGetUserInfo = (
  memberId: number,
  config?: UseQueryCustomOptions<CommonResponseType<GetUserResponseSchema>>
) => {
  const { data } = useSuspenseQuery({
    ...config,
    queryKey: ['user', memberId],
    queryFn: () => userService.getUser({ memberId })
  })

  return data.result
}
