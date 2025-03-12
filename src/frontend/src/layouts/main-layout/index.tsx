import { Suspense, useEffect, useRef, useState } from 'react'
import { Outlet, useNavigate, useParams } from 'react-router-dom'
import { useShallow } from 'zustand/shallow'

import serviceService from '@/apis/service/service'
import Avatar from '@/components/avatar'
import LoadingIcon from '@/components/loading-icon'
import { statusKo } from '@/constants/status'
import { useGetAllServerUnreadCount } from '@/hooks/queries/search/useGetAllServerUnreadCount'
import { useGetServer } from '@/hooks/queries/server/useGetServer'
import useGetSelfUser from '@/hooks/queries/user/useGetSelfUser'
import { useChattingStomp } from '@/hooks/use-chatting-stomp'
import { useMediaSettingsStore } from '@/stores/use-media-setting.store'
import { useServerUnreadStore } from '@/stores/use-server-unread-store'
import { useSignalingStomp } from '@/stores/use-signaling-stomp-store'

import ProfileCard from './components/profile-card'
import ProfileStatusButton from './components/profile-status-button'
import ServerCreateModal from './components/server-create-modal'
import { ServerList } from './components/server-list'
import { ServerListSkeleton } from './components/server-list/server-list-skeleton'
import SettingModal, { SettingModalTabsID } from './components/setting-modal'
const Inner = () => {
  const {
    connect: connectChatting,
    disconnect: disconnectChatting,
    subscribeToPersonal,
    subscribeToServer,
    unsubscribe,
    checkConnection
  } = useChattingStomp()
  const { connect: connectSignaling, disconnect: disconnectSignaling } = useSignalingStomp()
  const { serverId } = useParams<{ serverId: string }>()
  const previousServerId = useRef<number | null>(null)

  const navigate = useNavigate()

  /* Socket 연결 관련 함수들은 매번 새로 생성될 필요가 없고,
   * 의존성 배열에 추가하면 불필요한 재연결이 발생할 수 있으므로
   * exhaustive-deps 규칙을 비활성화합니다.
   */
  useEffect(() => {
    checkConnection()
    connectChatting()
    connectSignaling()
    subscribeToPersonal()

    return () => {
      disconnectChatting()
      disconnectSignaling()
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [])

  /* 서버 연결 관련 함수들은 매번 새로 생성될 필요가 없고,
   * 의존성 배열에 추가하면 불필요한 재연결이 발생할 수 있으므로
   * exhaustive-deps 규칙을 비활성화합니다.
   */
  useEffect(() => {
    const subscribeToServerIfConnected = async () => {
      if (!serverId) return

      await connectChatting()

      if (checkConnection()) {
        if (previousServerId.current && previousServerId.current !== Number(serverId)) {
          unsubscribe(`/topic/server/${previousServerId.current}`)
        }

        if (previousServerId.current !== Number(serverId)) {
          subscribeToServer(Number(serverId))
          previousServerId.current = Number(serverId)
        }
      }
    }

    subscribeToServerIfConnected()

    return () => {
      if (previousServerId.current !== Number(serverId)) {
        unsubscribe(`/topic/server/${previousServerId.current}`)
      }
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [checkConnection])

  const myChannelList = useGetServer()
  const selfUser = useGetSelfUser()

  const { setInitialUnreadCounts, resetUnreadCountForServer } = useServerUnreadStore()

  const allUnreadMessageCounts = useGetAllServerUnreadCount({
    memberId: selfUser.id
  })

  useEffect(() => {
    if (allUnreadMessageCounts && allUnreadMessageCounts.serverInfos) {
      allUnreadMessageCounts.serverInfos.forEach((server) => {
        const formattedData = server.channels.reduce(
          (acc, channel) => {
            acc[channel.channelId] = channel.unreadCount
            return acc
          },
          {} as Record<number, number>
        )
        setInitialUnreadCounts(server.serverId, formattedData)
      })
    }
  }, [allUnreadMessageCounts, setInitialUnreadCounts])

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
      subscribeToServer(serverId)
    }

    resetUnreadCountForServer(serverId)
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

  const handleToggleProfileCard = () => {
    setIsProfileCardOpen((prev) => !prev)
  }

  const handleClickServerCreateModalClose = () => {
    setIsServerCreateModalOpen(false)
  }

  return (
    <>
      <div className='flex'>
        <nav className='bg-black-80 h-full min-h-screen w-[72px] pt-[12px]'>
          <Suspense fallback={<ServerListSkeleton />}>
            <ServerList
              handleClickServer={handleClickServer}
              handleClickServerCreate={handleClickServerCreate}
              myChannelList={myChannelList}
            />
          </Suspense>
        </nav>
        <div className='flex-1 bg-gray-20 relative'>
          {isProfileCardOpen && (
            <div className='absolute left-0 bottom-0'>
              <div
                className='fixed z-[2000] left-0 bottom-0 w-full h-full'
                onClick={handleToggleProfileCard}
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
              onClick={handleToggleProfileCard}
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
                      {statusKo[selfUser.customPresenceStatus]} 표시
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
  return <Inner />
}
