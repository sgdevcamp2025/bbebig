import { useMutation } from '@tanstack/react-query'

import { SelectUserByNicknameRequestSchema } from '@/apis/schema/types/user'
import userService from '@/apis/service/user'

export function useCreateFriendRequestWithName() {
  const { mutate: createFriendRequestWithName } = useMutation({
    mutationFn: (data: SelectUserByNicknameRequestSchema) =>
      userService.createFriendWithNickname(data)
  })

  return {
    createFriendRequestWithName
  }
}
