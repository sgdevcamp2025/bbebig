import { useSuspenseQuery } from '@tanstack/react-query'

import { CommonResponseType } from '@/apis/schema/types/common'
import { GetServersResponseSchema } from '@/apis/schema/types/service'
import serviceService from '@/apis/service/service'
import { UseQueryCustomOptions } from '@/types/common'

export const useGetServer = (
  config?: UseQueryCustomOptions<CommonResponseType<GetServersResponseSchema>>
) => {
  const { data } = useSuspenseQuery({
    ...config,
    queryKey: ['server'],
    queryFn: serviceService.getServers
  })

  return data.result
}
