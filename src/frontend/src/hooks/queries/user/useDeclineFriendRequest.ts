import { useMutation, useQueryClient } from '@tanstack/react-query'

import {
  DeclineFriendRequestSchema,
  GetFriendPendingListResponseSchema
} from '@/apis/schema/types/user'
import userService from '@/apis/service/user'

export function useDeclineFriendRequest() {
  const queryClient = useQueryClient()

  return useMutation({
    mutationFn: async (data: DeclineFriendRequestSchema) => {
      const response = await userService.declineFriend(data)
      return response
    },

    onMutate: async (data) => {
      await queryClient.cancelQueries({ queryKey: ['friend', 'decline'] })

      const previousPendingList = queryClient.getQueryData<{
        receivePendingFriends: GetFriendPendingListResponseSchema[]
        sendPendingFriends: GetFriendPendingListResponseSchema[]
      }>(['friend', 'decline'])

      if (previousPendingList) {
        queryClient.setQueryData(['friend', 'decline'], {
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
        queryClient.setQueryData(['friend', 'decline'], context.previousPendingList)
      }
    },

    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['friend'] })
    }
  })
}
