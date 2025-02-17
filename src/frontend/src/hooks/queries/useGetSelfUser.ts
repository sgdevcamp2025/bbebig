import { useSuspenseQuery } from '@tanstack/react-query'

import { CommonResponseType } from '@/apis/schema/types/common'
import { GetUserSelfResponseSchema } from '@/apis/schema/types/user'
import userService from '@/apis/service/user'
import { UseQueryCustomOptions } from '@/types/common'

function useGetSelfUser(
  config?: UseQueryCustomOptions<CommonResponseType<GetUserSelfResponseSchema>>
) {
  const { data } = useSuspenseQuery({
    ...config,
    queryKey: ['user', 'self'],
    queryFn: userService.getUserSelf
  })

  return data
}

export default useGetSelfUser
