import { useMutation, useQueryClient } from '@tanstack/react-query'

import { SelectUserByNicknameRequestSchema } from '@/apis/schema/types/user'
import userService from '@/apis/service/user'

export function useCreateFriendRequestWithName() {
  const queryClient = useQueryClient()
  const { mutate: createFriendRequestWithName } = useMutation({
    mutationFn: (data: SelectUserByNicknameRequestSchema) =>
      userService.createFriendWithNickname(data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['friend', 'pending'] })
    }
  })

  return {
    createFriendRequestWithName
  }
}
