import CustomButton from '@/components/custom-button'
import CustomModal from '@/components/custom-modal'
import { cn } from '@/libs/cn'

interface Props {
  serverId: string
  isOpen: boolean
  onClose: () => void
}

function CategoryCreateModal({ isOpen, onClose, serverId }: Props) {
  const handleCreateCategory = () => {
    console.log('create category', serverId)
  }

  return (
    <CustomModal
      isOpen={isOpen}
      onClose={onClose}>
      <CustomModal.Header onClose={onClose}>
        <h1 className='text-text-normal text-[20px] leading-[24px] font-bold'>카테고리 만들기</h1>
      </CustomModal.Header>
      <CustomModal.Content>
        <div className='flex flex-col gap-2 mt-6'>
          <label className='text-white-100 text-[12px] leading-[16px] font-bold'>
            카테고리 이름
          </label>
          <div className='relative'>
            <input
              type='text'
              placeholder='새로운 카테고리'
              className={cn(
                'w-full outline-none h-[40px] bg-black-80 rounded-[3px] p-[10px] text-text-normal text-[16px] leading-[24px]'
              )}
            />
          </div>
        </div>
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
            type='button'
            onClick={handleCreateCategory}
            className='py-[2px] px-4 w-[96px] text-[14px] h-[38px]'>
            만들기
          </CustomButton>
        </div>
      </CustomModal.Bottom>
    </CustomModal>
  )
}

export default CategoryCreateModal
