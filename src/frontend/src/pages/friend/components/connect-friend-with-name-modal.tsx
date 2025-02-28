import { useForm } from 'react-hook-form'

import { SelectUserByNicknameRequestSchema } from '@/apis/schema/types/user'
import CustomButton from '@/components/custom-button'
import CustomModal from '@/components/custom-modal'
import { useCreateFriendRequestWithName } from '@/hooks/queries/user/useCreateFriendRequestWithName'
import { cn } from '@/libs/cn'

interface Props {
  isOpen: boolean
  onClose: () => void
}

export function ConnectFriendWithNameModal({ isOpen, onClose }: Props) {
  const {
    register,
    handleSubmit,
    reset,
    formState: { errors }
  } = useForm<SelectUserByNicknameRequestSchema>({
    defaultValues: {
      nickName: ''
    }
  })

  const { createFriendRequestWithName } = useCreateFriendRequestWithName()

  const handleCreateFriendRequestWithName = (data: SelectUserByNicknameRequestSchema) => {
    createFriendRequestWithName({
      nickName: data.nickName
    })

    onClose()
    reset()
  }

  return (
    <CustomModal
      isOpen={isOpen}
      onClose={onClose}>
      <CustomModal.Header onClose={onClose}>
        <h1 className='text-text-normal text-[20px] leading-[24px] font-bold'>친구 추가하기</h1>
      </CustomModal.Header>
      <form onSubmit={handleSubmit(handleCreateFriendRequestWithName)}>
        <CustomModal.Content>
          <div className='flex flex-col gap-2 mt-6'>
            <label className='text-white-100 text-[12px] leading-[16px] font-bold'>
              사용자 이름
            </label>
            <div className='relative'>
              <input
                {...register('nickName')}
                type='text'
                placeholder='친구 이름을 입력해주세요'
                className={cn(
                  'w-full outline-none h-[40px] bg-black-80 rounded-[3px] p-[10px] text-text-normal text-[16px] leading-[24px]'
                )}
              />
            </div>
          </div>
          <p className='text-red-500 text-[12px] leading-[16px]'>{errors.nickName?.message}</p>
        </CustomModal.Content>
        <CustomModal.Bottom>
          <div className='flex justify-end gap-2'>
            <button
              aria-label='취소'
              type='button'
              onClick={onClose}
              className='text-white-10 leading-[20px] py-[2px] px-4 w-[96px] text-[14px] h-[38px]'>
              취소
            </button>
            <CustomButton
              aria-label='친구 추가하기'
              type='submit'
              className='py-[2px] px-4 w-[96px] text-[14px] h-[38px]'>
              추가하기
            </CustomButton>
          </div>
        </CustomModal.Bottom>
      </form>
    </CustomModal>
  )
}
