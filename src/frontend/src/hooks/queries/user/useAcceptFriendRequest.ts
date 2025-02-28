import { useMutation, useQueryClient } from '@tanstack/react-query'

import {
  AcceptFriendRequestSchema,
  GetFriendPendingListResponseSchema
} from '@/apis/schema/types/user'
import userService from '@/apis/service/user'

export function useAcceptFriendRequest() {
  const queryClient = useQueryClient()

  return useMutation({
    mutationFn: async (data: AcceptFriendRequestSchema) => {
      const response = await userService.acceptFriend(data)

      return response
    },

    onMutate: async (data) => {
      await queryClient.cancelQueries({ queryKey: ['friend', 'accept'] })

      const previousPendingList = queryClient.getQueryData<{
        receivePendingFriends: GetFriendPendingListResponseSchema[]
        sendPendingFriends: GetFriendPendingListResponseSchema[]
      }>(['friend', 'accept'])

      if (previousPendingList) {
        queryClient.setQueryData(['friend', 'accept'], {
          ...previousPendingList,
          receivePendingFriends: previousPendingList.receivePendingFriends.filter(
            (friend) => friend.memberId !== data.friendId
          )
        })
      }

      return { previousPendingList }
    },

    onError: (error, data, context) => {
      if (context?.previousPendingList) {
        queryClient.setQueryData(['friend', 'accept'], context.previousPendingList)
      }
    },

    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['friend'] })
    }
  })
}
