import { useMutation } from '@tanstack/react-query'

import authService from '@/apis/service/auth'
import { UseMutationCustomOptions } from '@/types/common'

export function useLogoutMutation(config?: UseMutationCustomOptions<void>) {
  return useMutation({
    ...config,
    mutationFn: authService.logout,
    onSuccess: () => {
      window.location.href = '/'
    }
  })
}
