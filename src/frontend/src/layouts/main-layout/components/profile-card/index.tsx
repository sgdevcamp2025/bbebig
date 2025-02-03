import Avatar from '@/components/avatar'
import LoadingIcon from '@/components/loading-icon'
import StatusIcon from '@/components/status-icon'
import { statusKo } from '@/constants/status'
import { CustomPresenceStatus } from '@/types/user'
import { ChevronRightIcon } from 'lucide-react'

type Props = {
  name: string
  email: string
  status: CustomPresenceStatus
  avatarUrl: string
  backgroundUrl: string | null
}

export function Content({ name, email, status, avatarUrl, backgroundUrl }: Props) {
  const handleClickEditProfile = () => {
    console.log('edit profile')
  }

  const handleClickStatus = () => {
    console.log('status')
  }

  const handleClickLogout = () => {
    console.log('logout')
  }

  return (
    <div className='w-[308px] bg-black-100 rounded-[8px] border-black-90 border-[4px] overflow-hidden'>
      {backgroundUrl ? (
        <img
          src={backgroundUrl}
          className='w-full h-[105px] object-cover'
        />
      ) : (
        <div className='w-full h-[105px] bg-black-92' />
      )}
      <div className='relative p-4'>
        <div className='flex justify-center items-center absolute top-[-40px]'>
          <Avatar
            status={status}
            avatarUrl={avatarUrl}
            size='lg'
            statusColor='black'
            defaultBackgroundColor='black'
          />
        </div>
        <div className='flex flex-col pt-7'>
          <div className='flex flex-col pb-3'>
            <p className='text-white-100 text-[20px] font-bold'>{name}</p>
            <p className='text-gray-50 text-[14px]'>{email}</p>
          </div>
          <div className='flex flex-col gap-2 p-2'>
            <ul className='flex flex-col gap-2'>
              <li className='flex items-center p-2 hover:bg-gray-80 rounded-md'>
                <button
                  type='button'
                  onClick={handleClickEditProfile}>
                  <div className='flex items-center gap-1'>
                    <div className='w-4 h-4 flex items-center justify-center'>
                      <img src='/icon/menu/edit.svg' />
                    </div>
                    <p className='text-gray-50 text-[14px]'>프로필 편집</p>
                  </div>
                </button>
              </li>
              <div className='w-full h-[1px] rounded-sm bg-[#ffffff3d]' />
              <li className='flex items-center p-2 hover:bg-gray-80 rounded-md w-full mb-3'>
                <button
                  type='button'
                  onClick={handleClickStatus}
                  className='w-full'>
                  <div className='flex items-center justify-between w-full'>
                    <div className='flex items-center gap-1'>
                      <div className='w-4 h-4 flex items-center justify-center'>
                        <StatusIcon
                          status={status}
                          size='sm'
                        />
                      </div>
                      <p className='text-gray-50 text-[14px]'>{statusKo[status]}</p>
                    </div>
                    <ChevronRightIcon className='w-[14px] h-[14px] text-gray-50' />
                  </div>
                </button>
              </li>
              <li className='flex items-center p-2 hover:bg-gray-80 rounded-md w-full'>
                <button
                  type='button'
                  onClick={handleClickLogout}
                  className='w-full'>
                  <div className='flex items-center justify-between'>
                    <div className='flex gap-1'>
                      <div className='w-4 h-4 group flex items-center justify-center'>
                        <img
                          src='/icon/menu/minus-user.svg'
                          className='fill-discord-status-offline text-gray-50 group-hover:fill-white-100 transition-all duration-300'
                        />
                      </div>
                      <p className='text-gray-50 text-[14px]'>로그아웃</p>
                    </div>
                    <ChevronRightIcon className='w-[14px] h-[14px] text-gray-50' />
                  </div>
                </button>
              </li>
            </ul>
          </div>
        </div>
      </div>
    </div>
  )
}

const profileCard = {
  name: '지형',
  email: 'jihyeong@gmail.com',
  status: 'ONLINE',
  avatarUrl: '/image/common/default-avatar.png',
  backgroundUrl: '/image/common/default-background.png'
} as const

function ProfileCard() {
  const loading = false

  return (
    <div className='p-4'>
      {loading ? (
        <div className='flex justify-center items-center h-full'>
          <LoadingIcon />
        </div>
      ) : (
        <Content {...profileCard} />
      )}
    </div>
  )
}

export default ProfileCard
