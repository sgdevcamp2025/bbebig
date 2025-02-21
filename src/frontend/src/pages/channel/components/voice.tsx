import { useEffect, useState } from 'react'

import AvatarCard from '@/components/avatar-card'
import ChatArea from '@/components/chat-area'
import CustomButton from '@/components/custom-button'
import useGetSelfUser from '@/hooks/queries/user/useGetSelfUser'
import { useSignalingWithSFU } from '@/hooks/store/use-signaling-with-sfu'
import useUserStatus from '@/hooks/store/use-user-status'
import { cn } from '@/libs/cn'
import { ChatUser } from '@/types/user'

interface Props {
  channelId: number
  channelName: string
  serverName: string
  currentUser: ChatUser
  targetUser: ChatUser
}

function VideoComponent({ channelId, serverName, channelName, currentUser, targetUser }: Props) {
  const [sideBar, setSideBar] = useState(true)
  const selfUser = useGetSelfUser()
  const { getCurrentChannelInfo } = useUserStatus()

  const { joinChannel, leaveChannel, users } = useSignalingWithSFU(
    selfUser?.id.toString(),
    channelId.toString(),
    channelName,
    serverName
  )

  const isInVoiceChannel = getCurrentChannelInfo()?.channelId === channelId
  const isEmptyText = false

  const handleJoinVoiceChannel = () => {
    joinChannel()
  }

  const handleLeaveVoiceChannel = () => {
    leaveChannel()
  }

  useEffect(function cleanup() {
    return leaveChannel()
  }, [])

  return (
    <div className='flex flex-1 h-screen'>
      <div
        className={cn('flex w-full group bg-black', {
          'rounded-r-lg mr-2': sideBar
        })}>
        <div className='w-full h-full flex flex-col'>
          <section
            className={cn(
              'opacity-0 h-12 px-4 w-full flex items-center justify-between transition-all duration-300 group-hover:opacity-100 group-hover:translate-y-0 group-hover:blur-none'
            )}>
            <div className='flex items-center gap-2'>
              <img
                src={`/icon/channel/type-voice.svg`}
                className='w-6 h-6'
              />
              <span className='text-white-10 text-md font-bold'>{channelName}</span>
            </div>
            <button
              type='button'
              onClick={() => setSideBar(true)}>
              <img
                src='/icon/channel/threads.svg'
                className='w-6 h-6'
                alt='스레드'
              />
            </button>
          </section>
          <div />
          <section className='flex-1 flex flex-col items-center justify-center gap-2'>
            <ul className='flex gap-2 flex-wrap mb-8 w-full justify-center'>
              {[currentUser, ...targetUser].map((user) => (
                <li key={user.memberId}>
                  <AvatarCard
                    name={user.nickName}
                    avatarUrl={user.avatarUrl ?? '/image/common/default-avatar.png'}
                    backgroundUrl={user.bannerUrl ?? '/image/common/default-background.png'}
                    size={users.length > 3 ? 'sm' : isInVoiceChannel ? 'md' : 'sm'}
                    micStatus={true}
                    headphoneStatus={true}
                  />
                </li>
              ))}
            </ul>
            {!isInVoiceChannel && (
              <>
                <h4 className='text-gray-10 text-3xl font-bold'>{channelName}</h4>
                <span className='text-gray-90 text-sm font-semibold'>
                  {isEmptyText
                    ? '현재 채널에 아무도 없어요'
                    : `${users.map((user) => user.nickName).join(', ')} 님이 현재 음성 채널에 있어요.`}
                </span>
                <CustomButton
                  className='w-fit px-4 py-2 mt-5 mb-10'
                  variant='secondary'
                  onClick={handleJoinVoiceChannel}>
                  음성 채널 참가하기
                </CustomButton>
              </>
            )}
            {isInVoiceChannel && (
              <div
                className={cn(
                  'absolute bottom-4 opacity-0 flex items-center justify-center gap-4 transition-all duration-300 group-hover:opacity-100 group-hover:translate-y-0 group-hover:blur-none'
                )}>
                <button
                  type='button'
                  onClick={() => {
                    console.log(users, 'users')
                  }}
                  className='w-14 h-14 rounded-full bg-[#282d31] flex items-center justify-center'>
                  <img
                    alt='음성 채널 소리 끄기'
                    src='/icon/channel/microphone-muted.svg'
                    className='w-6 h-6'
                  />
                </button>
                <button
                  type='button'
                  onClick={handleLeaveVoiceChannel}
                  className='w-14 h-14 rounded-full bg-red-100 flex items-center justify-center'>
                  <img
                    alt='음성 채널 나가기'
                    src='/icon/channel/voice.svg'
                    className='w-6 h-6'
                  />
                </button>
              </div>
            )}
          </section>
        </div>
      </div>
      {sideBar && (
        <div className='flex flex-col min-w-[480px] h-screen bg-brand-10 rounded-l-lg'>
          <ChatArea
            isVoice={true}
            chatKey={channelId}
            users={{
              currentUser: currentUser,
              targetUser: targetUser
            }}
            channelName={channelName}
            onClose={() => setSideBar(false)}
          />
        </div>
      )}
    </div>
  )
}

export default VideoComponent
