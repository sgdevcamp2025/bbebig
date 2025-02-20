import CustomButton from '@/components/custom-button'
import useGetSelfUser from '@/hooks/queries/user/useGetSelfUser'

import { Content as ProfileCard } from '../profile-card'

export function MyProfile() {
  const myInfo = useGetSelfUser()
  return (
    <section className='pt-[60px] px-[40px] pb-20 flex flex-col gap-4'>
      <h2 className='text-white-100 text-[24px] leading-[30px] font-bold'>프로필</h2>
      <div className='grid grid-cols-2 gap-[35px]'>
        <div className='flex flex-col gap-6'>
          <div className='flex flex-col gap-2'>
            <h3 className='text-gray-10 text-[12px] leading-[15px] font-bold'>별명</h3>
            <input
              type='text'
              maxLength={32}
              className='w-full h-[40px] bg-black-90 rounded-[4px] px-2 outline-none text-text-normal text-[14px] leading-[18px]'
              defaultValue={myInfo.nickname}
              placeholder={myInfo.nickname}
            />
          </div>
          <div className='border-b border-gray-80' />
          <div className='flex flex-col gap-2'>
            <h3 className='text-gray-10 text-[12px] leading-[15px] font-bold'>대명사</h3>
            <input
              type='text'
              maxLength={32}
              className='w-full h-[40px] bg-black-90 rounded-[4px] px-2 outline-none text-text-normal text-[14px] leading-[18px]'
              defaultValue={myInfo.nickname}
              placeholder={'대명사를 추가해보세요'}
            />
          </div>
          <div className='border-b border-gray-80' />
          <div className='flex flex-col gap-2'>
            <h3 className='text-gray-10 text-[12px] leading-[15px] font-bold'>아바타</h3>
            <div className='flex gap-2'>
              <CustomButton
                type='button'
                className='w-fit py-[2px] px-4 h-8'
                onClick={() => {
                  console.log('아바타 변경하기')
                }}>
                아바타 변경하기
              </CustomButton>
              <CustomButton
                type='button'
                className='w-fit py-[2px] px-4 h-8 bg-transparent hover:underline'
                onClick={() => {
                  console.log('아바타 제거')
                }}>
                아바타 제거
              </CustomButton>
            </div>
          </div>
          <div className='border-b border-gray-80' />
          <div className='flex flex-col gap-2'>
            <h3 className='text-gray-10 text-[12px] leading-[15px] font-bold'>아바타 장식</h3>
            <div className='flex gap-2'>
              <CustomButton
                type='button'
                className='w-fit py-[2px] px-4 h-8'
                onClick={() => {
                  console.log('아바타 장식 변경하기')
                }}>
                장식 변경하기
              </CustomButton>
              <CustomButton
                type='button'
                className='w-fit py-[2px] px-4 h-8 bg-transparent hover:underline'
                onClick={() => {
                  console.log('아바타 장식 제거하기')
                }}>
                장식 제거하기
              </CustomButton>
            </div>
          </div>
          <div className='border-b border-gray-80' />
          <div className='flex flex-col gap-2'>
            <h3 className='text-gray-10 text-[12px] leading-[15px] font-bold'>프로필 효과</h3>
            <div className='flex gap-2'>
              <CustomButton
                type='button'
                className='w-fit py-[2px] px-4 h-8'
                onClick={() => {
                  console.log('프로필 효과 변경하기')
                }}>
                효과 변경
              </CustomButton>
              <CustomButton
                type='button'
                className='w-fit py-[2px] px-4 h-8 bg-transparent hover:underline'
                onClick={() => {
                  console.log('프로필 효과 제거하기')
                }}>
                효과 제거
              </CustomButton>
            </div>
          </div>
        </div>
        <div className='flex flex-col gap-2'>
          <ProfileCard
            isPreview={true}
            name={myInfo.name}
            email={myInfo.email}
            customPresenceStatus={myInfo.customPresenceStatus}
            avatarUrl={myInfo.avatarUrl}
            bannerUrl={myInfo.bannerUrl}
            onEditProfile={() => {
              console.log('프로필 수정하기')
            }}
          />
        </div>
      </div>
    </section>
  )
}
