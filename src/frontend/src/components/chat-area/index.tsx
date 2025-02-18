import { ChangeEvent, useEffect, useRef, useState } from 'react'

import Avatar from '@/components/avatar'
import CommonHeader from '@/components/common-header'
import { useGetChannelInfo } from '@/hooks/queries/channel/useGetChannelInfo'
import useGetSelfUser from '@/hooks/queries/user/useGetSelfUser'
import { cn } from '@/libs/cn'
import DmAreaHeader from '@/pages/dm/components/dm-area-header'
import useStatusBarStore from '@/stores/use-status-bar-store'
import { Friend } from '@/types/friend'
import { Message } from '@/types/message'
import timeHelper from '@/utils/format-time'

interface ChatAreaProps {
  friend?: Friend
  channelId?: number
  isVoice?: boolean
  onClose?: () => void
}

function ChatArea({ friend, channelId, isVoice, onClose }: ChatAreaProps) {
  const containerRef = useRef<HTMLDivElement>(null)
  const messagesRef = useRef<HTMLDivElement>(null)
  const inputRef = useRef<HTMLInputElement>(null)
  const [messages, setMessages] = useState<Record<string, Message[]>>({})
  const [searchValue, setSearchValue] = useState('')
  const { isStatusBarOpen, toggleStatusBar } = useStatusBarStore()
  const mySelfData = useGetSelfUser()
  const channelInfo = useGetChannelInfo(channelId ?? 0)

  const chatKey = friend?.friendId ?? channelId

  const sendMessage = () => {
    if (!inputRef.current || !chatKey) return
    const text = inputRef.current.value.trim()
    if (!text) return

    const newMessage: Message = {
      id: crypto.randomUUID(),
      memberId: mySelfData.id.toString(),
      type: 'CHANNEL',
      contents: { text: text },
      createdAt: new Date(),
      updatedAt: new Date()
    }

    setMessages((prev) => ({
      ...prev,
      [chatKey]: [...(prev[chatKey] || []), newMessage]
    }))

    inputRef.current.value = ''
  }

  useEffect(() => {
    if (messagesRef.current) {
      const messageContainer = messagesRef.current.parentElement
      if (messageContainer) {
        messageContainer.scrollTop = messageContainer.scrollHeight
      }
    }
  }, [messages])

  const handleKeyDown = (e: React.KeyboardEvent<HTMLInputElement>) => {
    if (e.key === 'Enter' && !e.nativeEvent.isComposing) {
      e.preventDefault()
      sendMessage()
    }
  }

  const handleSearch = (e: ChangeEvent<HTMLInputElement>) => {
    setSearchValue(e.target.value)
  }

  const handleClear = () => {
    setSearchValue('')
  }

  return (
    <div className='flex flex-col h-full relative'>
      <div className='absolute inset-0 flex flex-col'>
        {channelId && (
          <CommonHeader
            type={isVoice ? 'VOICE' : 'DEFAULT'}
            isStatusBarOpen={isStatusBarOpen}
            onToggleStatusBar={toggleStatusBar}
            onClose={onClose}
            searchProps={{
              value: searchValue,
              onChange: handleSearch,
              handleClear: handleClear,
              placeholder: '채팅 검색하기'
            }}>
            <img
              className={cn(isVoice ? 'w-5 h-5' : 'w-[17px] h-[17px]')}
              src={isVoice ? '/icon/channel/threads.svg' : '/icon/channel/type-chat.svg'}
              alt={isVoice ? '음성 채널' : '텍스트 채널'}
            />
            <span className='text-discord-font-color-normal font-medium'>
              {channelInfo?.channelName}
            </span>
          </CommonHeader>
        )}

        {friend && <DmAreaHeader friend={friend} />}

        {/* 채팅 영역 */}
        <div
          ref={containerRef}
          className='flex-1 overflow-y-auto p-4 flex flex-col justify-end'>
          {chatKey &&
            messages[chatKey]?.map((msg) => {
              const { isToday, ampm, hour12, minutes, year, month, day } = timeHelper(
                msg.createdAt.toISOString()
              )

              const isMyMessage = msg.memberId.toString() === mySelfData.id.toString()

              return (
                <div
                  key={msg.id}
                  className='flex items-start gap-3 mb-4'>
                  <Avatar
                    size='sm'
                    avatarUrl={
                      isMyMessage
                        ? mySelfData.avatarUrl
                        : (friend?.friendAvatarUrl ?? '/image/common/default-avatar.png')
                    }
                    statusColor='black'
                    status={isMyMessage ? mySelfData.customPresenceStatus : 'ONLINE'}
                    name={isMyMessage ? mySelfData.name : (friend?.friendName ?? '닉네임')}
                  />

                  <div className='flex-1'>
                    <div className='text-sm font-bold text-discord-font-color-normal'>
                      {isMyMessage ? mySelfData.name : (friend?.friendName ?? '닉네임')}
                      <span className='ml-2 text-xs text-gray-400'>
                        {isToday
                          ? `오늘 ${ampm} ${hour12}:${minutes}`
                          : `${year}. ${month}. ${day}. ${ampm} ${hour12}:${minutes}`}
                      </span>
                    </div>
                    <div className='text-sm text-discord-font-color-normal'>
                      {msg.contents.text}
                    </div>
                  </div>
                </div>
              )
            })}
          <div ref={messagesRef} />
        </div>

        {/* 입력창 */}
        <div className='sticky bottom-0 px-4 pb-4'>
          <div className='h-12 flex items-center bg-discord-gray-500 rounded-md'>
            <img
              className='ml-3 w-[25px] h-[25px]'
              src='/icon/chat/plus.svg'
              alt='+'
            />

            <input
              type='text'
              ref={inputRef}
              placeholder='메시지 보내기'
              className='w-full bg-transparent text-white px-3 py-2 outline-none focus-none'
              onKeyDown={handleKeyDown}
            />

            <div className='flex gap-3 items-center'>
              {['gift', 'gif', 'sticker', 'emoji'].map((icon, index) => (
                <img
                  key={index}
                  alt={`${icon}-icon`}
                  src={`/icon/chat/${icon}.svg`}
                />
              ))}
            </div>

            <button
              className='flex ml-5 px-3'
              type='button'
              onClick={sendMessage}>
              <img src='/icon/chat/send.svg' />
            </button>
          </div>
        </div>
      </div>
    </div>
  )
}

export default ChatArea
