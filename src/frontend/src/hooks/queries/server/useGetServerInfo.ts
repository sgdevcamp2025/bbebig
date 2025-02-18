import { useSuspenseQuery } from '@tanstack/react-query'

import { CommonResponseType } from '@/apis/schema/types/common'
import { GetServerListResponseSchema } from '@/apis/schema/types/service'
import serviceService from '@/apis/service/service'
import { UseQueryCustomOptions } from '@/types/common'

export const useGetServerInfo = (
  serverId: string,
  config?: UseQueryCustomOptions<CommonResponseType<GetServerListResponseSchema>>
) => {
  const { data } = useSuspenseQuery({
    ...config,
    queryKey: ['server', serverId],
    queryFn: () => serviceService.getServersList({ serverId })
  })

  return data.result
}
