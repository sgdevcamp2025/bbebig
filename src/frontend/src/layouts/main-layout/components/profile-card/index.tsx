import { useNavigate } from 'react-router'

import authService from '@/apis/service/auth'
import Avatar from '@/components/avatar'
import LoadingIcon from '@/components/loading-icon'
import StatusIcon from '@/components/status-icon'
import { statusKo } from '@/constants/status'
import { CustomPresenceStatus } from '@/types/user'

import MenuItem from './menu-item'
interface Props {
  name: string
  email: string
  status: CustomPresenceStatus
  avatarUrl: string
  backgroundUrl: string | null
  onEditProfile: () => void
}

export function Content({ name, email, status, avatarUrl, backgroundUrl, onEditProfile }: Props) {
  const navigate = useNavigate()

  const handleClickEditProfile = () => {
    onEditProfile()
  }

  const handleClickStatus = () => {
    console.log('status')
  }

  const handleClickLogout = () => {
    authService.logout()
    navigate('/', { replace: true })
  }

  const menuItems = [
    {
      onClick: handleClickEditProfile,
      icon: (
        <img
          src='/icon/menu/edit-profile.svg'
          alt='프로필 편집'
        />
      ),
      text: '프로필 편집'
    },
    {
      onClick: handleClickStatus,
      icon: (
        <StatusIcon
          status={status}
          size='sm'
        />
      ),
      text: statusKo[status],
      hasChevron: true
    },
    {
      onClick: handleClickLogout,
      icon: (
        <img
          src='/icon/menu/minus-user.svg'
          alt='로그아웃'
        />
      ),
      text: '로그아웃',
      hasChevron: true
    }
  ]

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
            <p className='text-gray-50 text-[14px]'>{email.split('@')[0]}</p>
          </div>
          <div className='p-2'>
            <ul className='flex flex-col gap-2'>
              {menuItems.map((item, index) => (
                <MenuItem
                  key={index}
                  onClick={item.onClick}
                  icon={item.icon}
                  text={item.text}
                  hasChevron={item.hasChevron}
                />
              ))}
            </ul>
          </div>
        </div>
      </div>
    </div>
  )
}

const profileCard = {
  name: '서정우',
  email: 'test@test.com',
  status: 'ONLINE',
  avatarUrl: '/image/common/default-avatar.png',
  backgroundUrl:
    'https://cdn.discordapp.com/banners/419411480677580820/a_b6a40ef738c6f8221793be39094cce00.png'
} as const

interface ProfileCardProps {
  onEditProfile: () => void
}

function ProfileCard({ onEditProfile }: ProfileCardProps) {
  const loading = false

  return (
    <div className='p-4'>
      {loading ? (
        <div className='flex justify-center items-center h-full'>
          <LoadingIcon />
        </div>
      ) : (
        <Content
          {...profileCard}
          onEditProfile={onEditProfile}
        />
      )}
    </div>
  )
}

export default ProfileCard
