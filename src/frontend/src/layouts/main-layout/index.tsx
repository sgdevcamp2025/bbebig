import { PlusIcon } from 'lucide-react'
import { Suspense, useEffect, useRef, useState } from 'react'
import { Outlet, useLocation, useNavigate, useParams } from 'react-router-dom'
import { useShallow } from 'zustand/shallow'

import serviceService from '@/apis/service/service'
import Avatar from '@/components/avatar'
import LoadingIcon from '@/components/loading-icon'
import LoadingModal from '@/components/loading-modal'
import ServerIcon from '@/components/server-icon'
import { statusKo } from '@/constants/status'
import { useGetServer } from '@/hooks/queries/server/useGetServer'
import useGetSelfUser from '@/hooks/queries/user/useGetSelfUser'
import useChattingStomp from '@/hooks/store/use-chatting-stomp'
import { cn } from '@/libs/cn'
import { useMediaSettingsStore } from '@/stores/use-media-setting.store'
import { useSignalingStomp } from '@/stores/use-signaling-stomp-store'
import { CustomPresenceStatus } from '@/types/user'

import ProfileCard from './components/profile-card'
import ProfileStatusButton from './components/profile-status-button'
import ServerCreateModal from './components/server-create-modal'
import SettingModal, { SettingModalTabsID } from './components/setting-modal'

const Inner = () => {
  const {
    connect: connectChatting,
    disconnect: disconnectChatting,
    subscribeToServer,
    unsubscribe,
    checkConnection
  } = useChattingStomp()
  const { connect: connectSignaling, disconnect: disconnectSignaling } = useSignalingStomp()
  const { serverId } = useParams<{ serverId: string }>()
  const previousServerId = useRef<number | null>(null)

  const location = useLocation()
  const navigate = useNavigate()

  useEffect(function init() {
    const initChatting = async () => {
      if (!checkConnection()) {
        await connectChatting()
      }
    }

    connectSignaling()
    initChatting()

    return function cleanup() {
      disconnectChatting()
      disconnectSignaling()
    }
  }, [])

  useEffect(() => {
    const subscribeToServerIfConnected = async () => {
      if (!serverId) return

      await connectChatting()

      if (checkConnection()) {
        console.log(`[📡] 서버 ${serverId} 자동 구독`)
        subscribeToServer(Number(serverId), (message) => {
          console.log(`[📩] 서버 이벤트 수신 (${serverId}):`, message)
        })

        previousServerId.current = Number(serverId)
      }
    }

    subscribeToServerIfConnected()

    return function cleanup() {
      if (previousServerId.current) {
        unsubscribe(`/topic/server/${previousServerId.current}`)
      }
    }
  }, [serverId, checkConnection])

  const myChannelList = useGetServer()
  const selfUser = useGetSelfUser()

  const { muted, toggleAudioInputMute, toggleAudioOutputMute } = useMediaSettingsStore(
    useShallow((state) => ({
      muted: state.muted,
      toggleAudioInputMute: state.toggleAudioInputMute,
      toggleAudioOutputMute: state.toggleAudioOutputMute
    }))
  )

  const [isProfileCardOpen, setIsProfileCardOpen] = useState(false)
  const [isServerCreateModalOpen, setIsServerCreateModalOpen] = useState(false)

  const handleClickServer = async (serverId: number) => {
    const {
      result: { channelInfoList }
    } = await serviceService.getServersList({ serverId: serverId.toString() })
    const firstChannelId = channelInfoList[0].channelId
    navigate(`/channels/${serverId}/${firstChannelId}`)

    if (checkConnection()) {
      console.log(`[📡] 서버 클릭 - 서버 ${serverId} 이벤트 구독 요청`)
      subscribeToServer(serverId, (message) => {
        console.log(`[📩] 서버 클릭 - 서버 이벤트 수신 (${serverId}):`, message)
      })
    }
  }

  const handleClickMyServer = () => {
    navigate('/channels/@me')
  }

  const [settingModalState, setSettingModalState] = useState({
    itemId: SettingModalTabsID.myAccount,
    isOpen: false
  })

  const handleClickSetting = () => {
    setSettingModalState({
      itemId: SettingModalTabsID.myAccount,
      isOpen: true
    })
  }

  const handleClickServerCreate = () => {
    setIsServerCreateModalOpen(true)
  }

  const handleClickEditProfileSettingModal = () => {
    setSettingModalState({
      itemId: SettingModalTabsID.myProfile,
      isOpen: true
    })
  }

  const handleClickSettingModalClose = () => {
    setSettingModalState({
      itemId: SettingModalTabsID.myAccount,
      isOpen: false
    })
  }

  const handleClickProfile = () => {
    setIsProfileCardOpen((prev) => !prev)
  }

  const handleClickServerCreateModalClose = () => {
    setIsServerCreateModalOpen(false)
  }

  const pathname =
    location.pathname.split('/')[1] === 'channels' ? location.pathname.split('/')[2] : null

  return (
    <>
      <div className='flex'>
        <nav className='bg-black-80 h-full min-h-screen w-[72px] pt-[12px]'>
          <ul className='w-[72px] flex flex-col gap-2'>
            <li>
              <button
                type='button'
                onClick={handleClickMyServer}
                className='flex items-center justify-center relative w-full'>
                <div
                  className={cn(
                    'absolute top-1/2 translate-y-[-50%] left-[-4px] w-2 rounded-r-[4px] overflow-hidden transition-all duration-300 bg-white',
                    pathname !== '@me' ? 'h-0' : 'h-10'
                  )}
                />
                <div
                  className={cn(
                    'w-[48px] h-[48px] flex items-center justify-center rounded-[48px] bg-brand-10 overflow-hidden transition-all duration-300 hover:rounded-[14px]',
                    pathname === '@me' ? 'rounded-[14px] bg-brand text-text-normal' : ''
                  )}>
                  <svg
                    aria-hidden='true'
                    role='img'
                    width='30'
                    height='30'
                    viewBox='0 0 24 24'>
                    <path
                      fill='#ffffff'
                      d='M19.73 4.87a18.2 18.2 0 0 0-4.6-1.44c-.21.4-.4.8-.58 1.21-1.69-.25-3.4-.25-5.1 0-.18-.41-.37-.82-.59-1.2-1.6.27-3.14.75-4.6 1.43A19.04 19.04 0 0 0 .96 17.7a18.43 18.43 0 0 0 5.63 2.87c.46-.62.86-1.28 1.2-1.98-.65-.25-1.29-.55-1.9-.92.17-.12.32-.24.47-.37 3.58 1.7 7.7 1.7 11.28 0l.46.37c-.6.36-1.25.67-1.9.92.35.7.75 1.35 1.2 1.98 2.03-.63 3.94-1.6 5.64-2.87.47-4.87-.78-9.09-3.3-12.83ZM8.3 15.12c-1.1 0-2-1.02-2-2.27 0-1.24.88-2.26 2-2.26s2.02 1.02 2 2.26c0 1.25-.89 2.27-2 2.27Zm7.4 0c-1.1 0-2-1.02-2-2.27 0-1.24.88-2.26 2-2.26s2.02 1.02 2 2.26c0 1.25-.88 2.27-2 2.27Z'
                    />
                  </svg>
                </div>
              </button>
            </li>
            <div className='w-full flex justify-center'>
              <div className='h-[2px] w-8 rounded-[1px] bg-gray-80' />
            </div>
            {myChannelList.servers.map((server) => (
              <li key={server.serverId}>
                <ServerIcon
                  imageUrl={server.serverImageUrl}
                  label={server.serverName}
                  isActive={pathname === server.serverId.toString()}
                  hasAlarm={false}
                  onClick={() => {
                    handleClickServer(server.serverId)
                  }}
                />
              </li>
            ))}
            <li>
              <button
                aria-label='서버 생성'
                type='button'
                onClick={handleClickServerCreate}
                className='flex items-center justify-center w-full'>
                <div
                  className={cn(
                    'w-[48px] h-[48px] flex items-center justify-center rounded-[48px] bg-brand-10 overflow-hidden transition-all duration-300 hover:rounded-[14px] hover:bg-brand'
                  )}>
                  <PlusIcon className='w-5 h-5 text-white' />
                </div>
              </button>
            </li>
          </ul>
        </nav>
        <div className='flex-1 bg-gray-20 relative'>
          {isProfileCardOpen && (
            <div className='absolute left-0 bottom-0'>
              <div
                className='fixed z-[2000] left-0 bottom-0 w-full h-full'
                onClick={handleClickProfile}
              />
              <div className='fixed z-[2001] left-[33px] bottom-[46px]'>
                <Suspense
                  fallback={
                    <div className='top-[-70px] left-[122px] absolute flex justify-center items-center h-fit bg-black-90 p-2 rounded-[8px] border-black-90 border-[4px]'>
                      <LoadingIcon />
                    </div>
                  }>
                  <ProfileCard onEditProfile={handleClickEditProfileSettingModal} />
                </Suspense>
              </div>
            </div>
          )}
          <div className='h-[52px] w-60 absolute left-0 px-1 pb-[1px] bottom-0 bg-black-92 flex items-center justify-between gap-2'>
            <button
              aria-label='프로필 버튼'
              type='button'
              onClick={handleClickProfile}
              className='flex gap-2 flex-1 hover:bg-gray-80 rounded-md p-1 group'>
              <Avatar
                name={selfUser.name}
                avatarUrl={selfUser.avatarUrl}
                size='sm'
                status={selfUser.customPresenceStatus}
                defaultBackgroundColor='bg-gray-60'
                statusColor={'black'}
              />
              <div className='flex flex-col'>
                <span className='text-text-normal text-left text-sm font-medium text-white leading-[18px]'>
                  {selfUser.name}
                </span>
                <div className='h-[13px] overflow-hidden'>
                  <div className='flex flex-col h-[13px] leading-[13px] group-hover:translate-y-[-100%] transition-all duration-300'>
                    <span className='text-[13px] text-left text-gray-10'>
                      {statusKo[selfUser.customPresenceStatus as CustomPresenceStatus]} 표시
                    </span>
                    <span className='text-[13px] text-left text-gray-10'>
                      {selfUser.email.split('@')[0]}
                    </span>
                  </div>
                </div>
              </div>
            </button>

            <div className='flex items-center justify-center'>
              <ProfileStatusButton
                aria-label='마이크 버튼'
                onClick={toggleAudioInputMute}
                icon={'microphone'}
                isMuted={muted.audioInput}
              />
              <ProfileStatusButton
                aria-label='소리 버튼'
                onClick={toggleAudioOutputMute}
                icon={'sound'}
                isMuted={muted.audioOutput}
              />
              <ProfileStatusButton
                aria-label='설정 버튼'
                onClick={handleClickSetting}
                icon={'settings'}
                isMuted={false}
              />
            </div>
          </div>
          <Outlet />
        </div>
      </div>
      <SettingModal
        itemId={settingModalState.itemId}
        isOpen={settingModalState.isOpen}
        onClose={handleClickSettingModalClose}
      />
      <ServerCreateModal
        isOpen={isServerCreateModalOpen}
        onClose={handleClickServerCreateModalClose}
      />
    </>
  )
}

export default function MainRootLayout() {
  return (
    <Suspense fallback={<LoadingModal isOpen={true} />}>
      <Inner />
    </Suspense>
  )
}
