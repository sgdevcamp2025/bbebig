import { ChatUser } from '@/types/user'

interface DmAreaHeaderProps {
  member: ChatUser
}

function DmAreaHeader({ member }: DmAreaHeaderProps) {
  return (
    <div className='flex flex-col gap-4 justify-center p-8 pb-6'>
      <div
        className='relative group'
        role='button'
        tabIndex={0}
        aria-label={`${member.nickName}의 프로필 사진`}>
        <img
          src={member.avatarUrl ?? '/image/common/default-avatar.png'}
          alt={`${member.nickName}의 프로필`}
          className='w-20 h-20 rounded-full'
        />
      </div>
      <div className='flex flex-col gap-1'>
        <h2 className='text-2xl font-bold text-discord-font-color-normal'>{member.nickName}</h2>
        <p className='text-discord-gray-200 text-sm'>
          <span className='text-discord-gray-100 font-medium'>{member.nickName}</span>
          님과 나눈 다이렉트 메시지의 첫 부분이에요
        </p>
      </div>
    </div>
  )
}

export default DmAreaHeader
