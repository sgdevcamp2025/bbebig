import { useMutation, useQueryClient } from '@tanstack/react-query'
import { useRef, useState } from 'react'
import { toast } from 'react-hot-toast'
import { useNavigate } from 'react-router'

import { CommonResponseType } from '@/apis/schema/types/common'
import { GetUserSelfResponseSchema } from '@/apis/schema/types/user'
import userService from '@/apis/service/user'
import Avatar from '@/components/avatar'
import StatusIcon from '@/components/status-icon'
import { statusKo } from '@/constants/status'
import useGetSelfUser from '@/hooks/queries/user/useGetSelfUser'
import useClickOutside from '@/hooks/use-click-outside'
import useLoginStore from '@/stores/use-login-store'
import { CustomPresenceStatus } from '@/types/user'

import MenuItem from './menu-item'
interface Props {
  name: string
  email: string
  customPresenceStatus: CustomPresenceStatus
  avatarUrl: string | null
  bannerUrl: string | null
  onEditProfile: () => void
}

const statusMenuItems = [
  {
    status: 'ONLINE',
    text: '온라인'
  },
  {
    status: 'AWAY',
    text: '자리 비움'
  },
  {
    status: 'DND',
    text: '방해 금지',
    description: '모든 알림을 받지 않아요'
  },
  {
    status: 'INVISIBLE',
    text: '오프라인 표시',
    description: '온라인으로 표시되지는 않지만, 비슷코드의 모든 기능을 이용할 수 있어요'
  }
] as {
  status: CustomPresenceStatus
  text: string
  description?: string
}[]

export function Content({
  name,
  email,
  customPresenceStatus,
  avatarUrl,
  bannerUrl,
  onEditProfile
}: Props) {
  const queryClient = useQueryClient()
  const [isStatusMenuOpen, setIsStatusMenuOpen] = useState(false)

  const { mutate: updateUserStatus } = useMutation({
    mutationFn: (status: CustomPresenceStatus) =>
      userService.updateUserPresenceStatus({ customPresenceStatus: status }),
    onMutate: async (newStatus) => {
      await queryClient.cancelQueries({ queryKey: ['user', 'self'] })

      const previousUser = queryClient.getQueryData(['user', 'self'])

      queryClient.setQueryData(
        ['user', 'self'],
        (prev: CommonResponseType<GetUserSelfResponseSchema>) => ({
          ...prev,
          result: {
            ...prev.result,
            customPresenceStatus: newStatus
          }
        })
      )

      return { previousUser }
    },
    onError: (_error, _newStatus, context) => {
      queryClient.setQueryData(['user', 'self'], context?.previousUser)
      toast.error('상태 변경 실패')
    },
    onSuccess: () => {
      toast.success('상태 변경 완료')
    }
  })

  const navigate = useNavigate()
  const { logout } = useLoginStore()

  const statusMenuRef = useRef<HTMLDivElement>(null)

  useClickOutside(statusMenuRef, () => {
    setIsStatusMenuOpen(false)
  })

  const handleClickEditProfile = () => {
    onEditProfile()
  }

  const handleClickStatus = () => {
    setIsStatusMenuOpen((prev) => !prev)
  }

  const handleClickLogout = () => {
    logout()
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
          status={customPresenceStatus}
          size='sm'
        />
      ),
      text: statusKo[customPresenceStatus],
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
    <div className='w-[308px] bg-black-100 rounded-[8px] border-black-90 border-[4px]'>
      {bannerUrl ? (
        <img
          src={bannerUrl}
          className='w-full h-[105px] object-cover'
        />
      ) : (
        <div className='w-full h-[105px] bg-black-92' />
      )}
      <div className='relative p-4'>
        <div className='flex justify-center items-center absolute top-[-40px]'>
          <Avatar
            name={name}
            status={customPresenceStatus}
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
          <div className='p-2 relative'>
            {isStatusMenuOpen ? (
              <div
                ref={statusMenuRef}
                className='absolute top-[1/2] translate-y-[-20%] left-[270px]'>
                <div className='p-2 rounded-sm bg-black-90'>
                  {statusMenuItems.map((item) => {
                    return (
                      <button
                        type='button'
                        key={item.status}
                        className='rounded-sm py-3 px-2 w-[300px] flex items-center gap-2 group hover:bg-brand cursor-pointer'
                        onClick={() => {
                          updateUserStatus(item.status)
                        }}>
                        <StatusIcon
                          status={item.status}
                          size='sm'
                        />
                        <p className='text-white-20 font-medium text-[14px] group-hover:text-white-100'>
                          {item.text}
                        </p>
                      </button>
                    )
                  })}
                </div>
              </div>
            ) : null}
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

interface ProfileCardProps {
  onEditProfile: () => void
}

function ProfileCard({ onEditProfile }: ProfileCardProps) {
  const mySelfData = useGetSelfUser()

  return (
    <div className='p-4'>
      <Content
        {...mySelfData}
        onEditProfile={onEditProfile}
      />
    </div>
  )
}

export default ProfileCard
