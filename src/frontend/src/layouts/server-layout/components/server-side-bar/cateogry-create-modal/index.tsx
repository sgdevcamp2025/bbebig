import { useForm } from 'react-hook-form'
import { useParams } from 'react-router-dom'

import { ZCreateCategoryRequestSchema } from '@/apis/schema/types/service'
import CustomButton from '@/components/custom-button'
import CustomModal from '@/components/custom-modal'
import { useCreateCategory } from '@/hooks/queries/server/useCreateCategory'
import { cn } from '@/libs/cn'
interface InnerProps {
  serverId: number
  isOpen: boolean
  onClose: () => void
}

export function Inner({ isOpen, onClose, serverId }: InnerProps) {
  const {
    register,
    handleSubmit,
    reset,
    formState: { errors }
  } = useForm<ZCreateCategoryRequestSchema>({
    defaultValues: {
      categoryName: ''
    }
  })

  const createCategory = useCreateCategory()

  const handleCreateCategory = (data: ZCreateCategoryRequestSchema) => {
    createCategory({
      serverId: String(serverId),
      categoryName: data.categoryName
    })

    onClose()
    reset()
  }

  return (
    <CustomModal
      isOpen={isOpen}
      onClose={onClose}>
      <CustomModal.Header onClose={onClose}>
        <h1 className='text-text-normal text-[20px] leading-[24px] font-bold'>카테고리 만들기</h1>
      </CustomModal.Header>
      <form onSubmit={handleSubmit(handleCreateCategory)}>
        <CustomModal.Content>
          <div className='flex flex-col gap-2 mt-6'>
            <label className='text-white-100 text-[12px] leading-[16px] font-bold'>
              카테고리 이름
            </label>
            <div className='relative'>
              <input
                {...register('categoryName')}
                type='text'
                placeholder='새로운 카테고리'
                className={cn(
                  'w-full outline-none h-[40px] bg-black-80 rounded-[3px] p-[10px] text-text-normal text-[16px] leading-[24px]'
                )}
              />
            </div>
          </div>
          <p className='text-red-500 text-[12px] leading-[16px]'>{errors.categoryName?.message}</p>
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
              aria-label='카테고리 만들기'
              type='submit'
              className='py-[2px] px-4 w-[96px] text-[14px] h-[38px]'>
              만들기
            </CustomButton>
          </div>
        </CustomModal.Bottom>
      </form>
    </CustomModal>
  )
}

type Props = Omit<InnerProps, 'serverId'>

function CategoryCreateModal({ isOpen, onClose }: Props) {
  const { serverId } = useParams<{ serverId: string }>()

  if (!serverId) {
    throw new Error('serverId is required')
  }

  return (
    <Inner
      isOpen={isOpen}
      onClose={onClose}
      serverId={Number(serverId)}
    />
  )
}

export default CategoryCreateModal
