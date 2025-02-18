import { useState } from 'react'

import Avatar from '@/components/avatar'
import CustomButton from '@/components/custom-button'
import useGetSelfUser from '@/hooks/queries/user/useGetSelfUser'
import { cn } from '@/libs/cn'

interface Props {
  onProfileEditClick: () => void
}

function MyAccount({ onProfileEditClick }: Props) {
  const [isEmailShown, setIsEmailShown] = useState(false)
  const myInfo = useGetSelfUser()
  return (
    <section className=' pt-[60px] px-[40px] pb-20 flex flex-col gap-4'>
      <h2 className='text-white-100 text-[24px] leading-[30px] font-bold'>내 계정</h2>
      <div className='relative w-[660px] rounded-lg bg-black-80 min-h-[400px]'>
        <div className='relative w-full h-[100px]'>
          {myInfo?.bannerUrl ? (
            <img
              src={myInfo?.bannerUrl}
              className='w-full h-[100px] object-cover'
            />
          ) : (
            <div className='w-full h-[100px] bg-[#22C55E] rounded-t-[4px]' />
          )}
        </div>
        <div className='p-4 pb-0 pl-[120px]'>
          <div className='absolute top-[82px] left-[22px]'>
            <Avatar
              name={myInfo?.name}
              status={myInfo?.customPresenceStatus}
              avatarUrl={myInfo?.avatarUrl}
              size='md'
              statusColor='#1E1F22'
              defaultBackgroundColor='#1E1F22'
            />
          </div>
          <div className='flex items-center justify-between h-[76px]'>
            <p className='text-white-100 text-[20px] font-bold'>{myInfo?.name}</p>
            <CustomButton
              type='button'
              className='w-fit py-[2px] px-4 h-8'
              onClick={onProfileEditClick}>
              사용자 프로필 편집
            </CustomButton>
          </div>
        </div>
        <div className={cn('flex flex-col gap-2 m-4 mt-2 bg-gray-20 p-4 rounded-md')}>
          <ul className='flex flex-col gap-6'>
            <li className='flex items-center justify-between'>
              <div className='flex flex-col gap-1'>
                <h2 className='text-gray-10 leading-[15px] text-[12px] font-bold'>별명</h2>
                <span className='text-white-10 leading-[20px] text-[16px]'>{myInfo?.nickname}</span>
              </div>
              <CustomButton
                type='button'
                className='w-fit px-4 py-[2px] my-1 h-8 bg-[#4E5058] duration-200 hover:bg-[#5E6068]'
                onClick={onProfileEditClick}>
                수정
              </CustomButton>
            </li>
            <li className='flex items-center justify-between'>
              <div className='flex flex-col gap-1'>
                <h2 className='text-gray-10 leading-[15px] text-[12px] font-bold'>사용자명</h2>
                <span className='text-white-10 leading-[20px] text-[16px]'>{myInfo?.name}</span>
              </div>
              <CustomButton
                type='button'
                className='w-fit px-4 py-[2px] my-1 h-8 bg-[#4E5058] duration-200 hover:bg-[#5E6068]'
                onClick={onProfileEditClick}>
                수정
              </CustomButton>
            </li>
            <li className='flex items-center justify-between'>
              <div className='flex flex-col gap-1'>
                <h2 className='text-gray-10 leading-[15px] text-[12px] font-bold'>이메일</h2>
                <div className='flex items-center gap-1'>
                  <span className='text-white-10 leading-[20px] text-[16px]'>
                    {isEmailShown ? myInfo?.email : '****@' + myInfo?.email.split('@')[1]}
                  </span>
                  <span
                    onClick={() => setIsEmailShown(!isEmailShown)}
                    className='cursor-pointer text-text-link text-[14px] leading-[15px]'>
                    보이기
                  </span>
                </div>
              </div>
              <CustomButton
                type='button'
                className='w-fit px-4 py-[2px] my-1 h-8 bg-[#4E5058] duration-200 hover:bg-[#5E6068]'
                onClick={onProfileEditClick}>
                수정
              </CustomButton>
            </li>
          </ul>
        </div>
      </div>
    </section>
  )
}

export default MyAccount
