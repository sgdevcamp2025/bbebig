import { useMutation } from '@tanstack/react-query'
import { useQueryClient } from '@tanstack/react-query'

import { CommonResponseType } from '@/apis/schema/types/common'
import { GetUserSelfResponseSchema } from '@/apis/schema/types/user'
import { uploadFile } from '@/apis/service/common'
export function useUpdateAvatarImage() {
  const queryClient = useQueryClient()
  const { mutate: updateAvatarImage } = useMutation({
    mutationFn: uploadFile,
    onSuccess: (url) => {
      queryClient.cancelQueries({ queryKey: ['user', 'self'] })
      queryClient.setQueryData(
        ['user', 'self'],
        (old: CommonResponseType<GetUserSelfResponseSchema>) => {
          return {
            ...old,
            result: {
              ...old.result,
              avatarUrl: url || null
            }
          }
        }
      )
      queryClient.invalidateQueries({ queryKey: ['serverMemebersData'] })
    }
  })

  return { updateAvatarImage }
}
