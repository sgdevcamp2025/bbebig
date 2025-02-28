import { zodResolver } from '@hookform/resolvers/zod'
import { useState } from 'react'
import { useForm } from 'react-hook-form'
import { useParams } from 'react-router-dom'

import { createChannelRequestSchema } from '@/apis/schema/service'
import { ZCreateChannelRequestSchema } from '@/apis/schema/types/service'
import CustomButton from '@/components/custom-button'
import CustomModal from '@/components/custom-modal'
import CustomRadio, { RadioItem } from '@/components/custom-radio'
import { useCreateChannel } from '@/hooks/queries/server/useCreateChannel'
import { cn } from '@/libs/cn'

interface InnerProps {
  selectCategoryId: number
  serverId: number
  categoryInfo?: {
    id: string
    name: string
  }
  isOpen: boolean
  onClose: () => void
}

const CHANNEL_TYPE_ITEMS = [
  {
    id: '1',
    label: '텍스트',
    description: '메시지, 이미지, GIF, 이모지, 의견, 농담을 전송하세요',
    value: 'CHAT',
    icon: (
      <img
        src='/icon/channel/type-chat.svg'
        alt='텍스트'
        width={20}
        height={20}
      />
    )
  },
  {
    id: '2',
    label: '음성',
    description: '음성, 영상, 화면 공유로 함꼐 어울리세요',
    value: 'VOICE',
    icon: (
      <img
        src='/icon/channel/type-voice.svg'
        alt='음성'
        width={20}
        height={20}
      />
    )
  }
]

export function Inner({ serverId, isOpen, onClose, categoryInfo, selectCategoryId }: InnerProps) {
  const [selectedChannelType, setSelectedChannelType] = useState<RadioItem>(CHANNEL_TYPE_ITEMS[0])

  const {
    register,
    handleSubmit,
    reset,
    formState: { errors }
  } = useForm<ZCreateChannelRequestSchema>({
    defaultValues: {
      serverId: String(serverId),
      categoryId: -1,
      channelType: '',
      channelName: '',
      privateStatus: false,
      memberIds: []
    },
    resolver: zodResolver(createChannelRequestSchema)
  })

  const createChannel = useCreateChannel()

  const handleCreateChannel = (data: ZCreateChannelRequestSchema) => {
    createChannel({
      channelName: data.channelName,
      channelType: selectedChannelType.value,
      privateStatus: false,
      memberIds: [null],
      serverId: String(serverId),
      categoryId: selectCategoryId
    })

    if (selectCategoryId) {
      selectCategoryId = -1
    }

    setSelectedChannelType(CHANNEL_TYPE_ITEMS[0])
    onClose()
    reset()
  }

  return (
    <CustomModal
      isOpen={isOpen}
      onClose={onClose}>
      <CustomModal.Header onClose={onClose}>
        <h1 className='text-text-normal text-[20px] leading-[24px] font-bold'>채널 만들기</h1>
        {categoryInfo?.name && (
          <div className='text-gray-10 text-[12px] leading-[16px]'>
            :{categoryInfo.name}에 속해 있음
          </div>
        )}
      </CustomModal.Header>
      <form onSubmit={handleSubmit(handleCreateChannel)}>
        <CustomModal.Content>
          <CustomRadio
            label='채널 유형'
            items={CHANNEL_TYPE_ITEMS}
            selectedItem={selectedChannelType}
            onChange={setSelectedChannelType}
          />
          <div
            className='flex flex-col gap-2 mt-6'
            onSubmit={handleSubmit(handleCreateChannel)}>
            <label className='text-white-100 text-[12px] leading-[16px] font-bold'>채널 이름</label>
            <div className='relative'>
              <div className='absolute left-[16px] top-1/2 -translate-x-1/2 -translate-y-1/2 w-5 h-5 flex items-center justify-center'>
                <img
                  src='/icon/channel/type-chat.svg'
                  alt='텍스트'
                  width={14}
                  height={14}
                />
              </div>
              <input
                {...register('channelName')}
                type='text'
                placeholder='새로운 채널'
                className={cn(
                  'w-full outline-none h-[40px] bg-black-80 rounded-[3px] p-[10px] pl-[28px] text-text-normal text-[16px] leading-[24px]'
                )}
              />
              <p className='text-red-500 text-[12px] leading-[16px]'>
                {errors.channelName?.message}
              </p>
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
              aria-label='채널 만들기'
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

function ChannelCreateModal({ isOpen, onClose, categoryInfo, selectCategoryId }: Props) {
  const { serverId } = useParams<{ serverId: string }>()

  if (!serverId) {
    throw new Error('serverId is required')
  }

  return (
    <Inner
      selectCategoryId={selectCategoryId}
      isOpen={isOpen}
      onClose={onClose}
      categoryInfo={categoryInfo}
      serverId={Number(serverId)}
    />
  )
}

export default ChannelCreateModal
