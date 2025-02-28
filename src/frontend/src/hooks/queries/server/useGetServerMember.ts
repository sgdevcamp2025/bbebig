import { useSuspenseQuery } from '@tanstack/react-query'

import { CommonResponseType } from '@/apis/schema/types/common'
import { GetServerMemebersResponseSchema } from '@/apis/schema/types/service'
import serviceService from '@/apis/service/service'
import { UseQueryCustomOptions } from '@/types/common'

export const useGetServerMember = (
  serverId: string,
  config?: UseQueryCustomOptions<CommonResponseType<GetServerMemebersResponseSchema>>
) => {
  const { data } = useSuspenseQuery({
    ...config,
    queryKey: ['serverMemebersData', serverId],
    queryFn: () => serviceService.getServerMemebers({ serverId: Number(serverId) })
  })

  return data.result
}
