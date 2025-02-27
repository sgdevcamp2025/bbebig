import { useMutation, useQueryClient } from '@tanstack/react-query'

import { ZInviteUserRequestSchema } from '@/apis/schema/types/service'
import serviceService from '@/apis/service/service'

export function useInviteServerWithUserNickname() {
  const queryClient = useQueryClient()
  const { mutate: inviteServerWithUserNickname } = useMutation({
    mutationFn: (data: ZInviteUserRequestSchema) =>
      serviceService.inviteServerWithUserNickname(data),
    onSuccess: (_, request) => {
      queryClient.invalidateQueries({ queryKey: ['serverMemebersData', request.serverId] })
    }
  })

  return {
    inviteServerWithUserNickname
  }
}
