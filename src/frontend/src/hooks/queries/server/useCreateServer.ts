import { useMutation } from '@tanstack/react-query'
import { useQueryClient } from '@tanstack/react-query'

import serviceService from '@/apis/service/service'

const useCreateServer = () => {
  const queryClient = useQueryClient()
  const { mutate: createServer } = useMutation({
    mutationFn: serviceService.createServer,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['server'] })
    }
  })

  return createServer
}

export default useCreateServer
