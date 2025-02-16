import { useMutation, useQueryClient } from '@tanstack/react-query'
import { Camera } from 'lucide-react'
import { Plus } from 'lucide-react'
import { ComponentProps } from 'react'
import { useForm } from 'react-hook-form'

import { CreateServerRequestSchema } from '@/apis/schema/types/service'
import serviceService from '@/apis/service/service'
import CustomButton from '@/components/custom-button'
import CustomModal from '@/components/custom-modal'

type ServerCreateModalProps = {
  isOpen: boolean
  onClose: () => void
} & ComponentProps<typeof CustomModal>

function ServerCreateModal({ isOpen, onClose, ...args }: ServerCreateModalProps) {
  const queryClient = useQueryClient()

  const { mutate: createServer } = useMutation({
    mutationFn: serviceService.createServer,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['servers'] })
    }
  })

  const {
    register,
    handleSubmit,
    formState: { errors },
    reset
  } = useForm<CreateServerRequestSchema>({
    defaultValues: {
      serverName: '',
      serverImageUrl: null
    }
  })

  const handleClickCreateServer = (data: CreateServerRequestSchema) => {
    createServer({
      serverName: data.serverName,
      serverImageUrl: null
    })
    reset()
    onClose()
  }

  return (
    <CustomModal
      {...args}
      isOpen={isOpen}
      onClose={onClose}>
      <CustomModal.Header onClose={onClose}>
        <h1 className='text-text-normal text-[24px] leading-[30px] text-center font-bold'>
          서버 커스터마이즈하기
        </h1>
        <div className='text-center mt-2 text-gray-10 leading-[20px]'>
          새로운 서버에 이름과 아이콘을 부여해 개성을 드러내 보세요. 나중에 언제든 바꿀 수 있어요.
        </div>
      </CustomModal.Header>
      <form onSubmit={handleSubmit(handleClickCreateServer)}>
        <CustomModal.Content>
          <div className='flex flex-col items-center justify-center'>
            <div className='w-[80px] h-[80px] flex-col rounded-full relative flex items-center justify-center border-2 border-dashed border-gray-10'>
              <div className='absolute top-0 right-0 w-5 h-5 rounded-full bg-brand flex items-center justify-center'>
                <Plus className='w-3 h-3 text-white-100' />
              </div>
              <Camera className='w-6 h-6 text-gray-10' />
              <span className='text-gray-10 text-[12px] leading-[16px] font-medium'>UPLOAD</span>
            </div>
          </div>
          <label className='text-[12px] text-gray-10 mb-2 leading-[24px] font-bold'>
            서버 이름
          </label>
          <input
            {...register('serverName')}
            type='text'
            className='outline-none w-full h-[40px] bg-black-80 rounded-[3px] p-[10px] text-text-normal text-[16px] leading-[24px] font-medium'
          />
          <p className='text-red-500 text-[12px] leading-[16px]'>{errors.serverName?.message}</p>
          <div className='text-[12px] mt-2 text-gray-10 leading-[20px]'>
            서버를 만들면 Discord의{' '}
            <a
              href='#'
              className='text-text-link'>
              커뮤니티 지침
            </a>
            에 동의하게 됩니다.
          </div>
        </CustomModal.Content>
        <CustomModal.Bottom>
          <div className='flex justify-between'>
            <button
              aria-label='뒤로가기'
              type='button'
              onClick={onClose}
              className='text-white-10 text-[14px] leading-[20px]'>
              뒤로가기
            </button>
            <CustomButton className='py-[2px] px-4 w-[96px] text-[14px] h-[38px]'>
              만들기
            </CustomButton>
          </div>
        </CustomModal.Bottom>
      </form>
    </CustomModal>
  )
}

export default ServerCreateModal
