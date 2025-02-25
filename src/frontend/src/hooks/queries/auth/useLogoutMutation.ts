import { useMutation, useQueryClient } from '@tanstack/react-query'

import authService from '@/apis/service/auth'
import { UseMutationCustomOptions } from '@/types/common'

export function useLogoutMutation(config?: UseMutationCustomOptions<void>) {
  const queryClient = useQueryClient()

  return useMutation({
    ...config,
    mutationFn: authService.logout,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['statusCheck'] })
    }
  })
}
