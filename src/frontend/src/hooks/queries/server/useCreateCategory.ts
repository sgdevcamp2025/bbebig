import { useMutation, useQueryClient } from '@tanstack/react-query'

import { CommonResponseType } from '@/apis/schema/types/common'
import {
  CreateCategoryResponseSchema,
  ZCreateCategoryRequestSchema
} from '@/apis/schema/types/service'
import serviceService from '@/apis/service/service'
import { UseMutationCustomOptions } from '@/types/common'

export const useCreateCategory = (
  config?: UseMutationCustomOptions<
    CommonResponseType<CreateCategoryResponseSchema>,
    ZCreateCategoryRequestSchema
  >
) => {
  const queryClient = useQueryClient()

  const { mutate: createCategory } = useMutation({
    ...config,
    mutationFn: (data: ZCreateCategoryRequestSchema) =>
      serviceService.createCategory({
        serverId: Number(data.serverId),
        categoryName: data.categoryName
      }),
    onSuccess: (_, variables) => {
      queryClient.invalidateQueries({ queryKey: ['server', variables.serverId.toString()] })
    }
  })

  return createCategory
}
