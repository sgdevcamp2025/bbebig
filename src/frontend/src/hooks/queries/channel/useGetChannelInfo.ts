import { useSuspenseQuery } from '@tanstack/react-query'

import { CommonResponseType } from '@/apis/schema/types/common'
import { GetChannelListResponseSchema } from '@/apis/schema/types/service'
import serviceService from '@/apis/service/service'
import { UseQueryCustomOptions } from '@/types/common'

export const useGetChannelInfo = (
  channelId: number,
  config?: UseQueryCustomOptions<CommonResponseType<GetChannelListResponseSchema>>
) => {
  const { data } = useSuspenseQuery({
    ...config,
    queryKey: ['channel', channelId],
    queryFn: () => serviceService.getChannelList({ channelId })
  })

  return data.result
}
