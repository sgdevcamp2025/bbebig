import { useMutation } from '@tanstack/react-query'

import { StatusCheckResponseSchema } from '@/apis/schema/types/auth'
import authService from '@/apis/service/auth'
import { UseMutationCustomOptions } from '@/types/common'

export function useStatusCheckMutation(
  config?: UseMutationCustomOptions<StatusCheckResponseSchema>
) {
  return useMutation({
    ...config,
    mutationFn: authService.statusCheck
  })
}
