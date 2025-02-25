import { useMutation, useQueryClient } from '@tanstack/react-query'

import authService from '@/apis/service/auth'
import { UseMutationCustomOptions } from '@/types/common'

export function useLoginMutation(config?: UseMutationCustomOptions<void>) {
  const queryClient = useQueryClient()
  return useMutation({
    ...config,
    mutationFn: authService.login,
    onSuccess: () => {
      queryClient.cancelQueries({ queryKey: ['statusCheck'] })
      queryClient.setQueryData(['statusCheck'], {
        result: {
          status: true
        }
      })
    }
  })
}
