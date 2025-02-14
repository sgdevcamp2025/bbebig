import { ChangeEvent, useEffect, useRef, useState } from 'react'

import Avatar from '@/components/avatar'
import CommonHeader from '@/components/common-header'
import { cn } from '@/libs/cn'
import useStatusBarStore from '@/stores/use-status-bar-store'
import { Message } from '@/types/message'
import timeHelper from '@/utils/format-time'

interface Props {
  channelId: number
  isVoice: boolean
  onClose?: () => void
}

function ChatArea({ channelId, isVoice, onClose }: Props) {
  const messagesRef = useRef<HTMLDivElement>(null)
  const [messages, setMessages] = useState<Record<string, Message[]>>({})
  const [searchValue, setSearchValue] = useState('')
  const { isStatusBarOpen, toggleStatusBar } = useStatusBarStore()
  const inputRef = useRef<HTMLInputElement>(null)
  const sendMessage = () => {
    if (!inputRef.current || !channelId) return
    const text = inputRef.current.value.trim()
    if (!text) return

    const newMessage: Message = {
      id: crypto.randomUUID(),
      memberId: '1',
      type: 'CHANNEL',
      contents: { text: text },
      createdAt: new Date(),
      updatedAt: new Date()
    }

    setMessages((prev) => ({
      ...prev,
      [channelId]: [...(prev[channelId] || []), newMessage]
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
    <>
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
          src={isVoice ? '/icon/channel/threads.svg' : '/icon/channel/type-text.svg'}
          alt={isVoice ? '음성 채널' : '텍스트 채널'}
        />
        <span className='text-discord-font-color-normal font-medium'>채널 {channelId}</span>
      </CommonHeader>

      {/* 메시지 영역 */}
      <div className='flex-1 p-4 text-discord-font-color-normal space-y-4 overflow-y-auto'>
        {channelId &&
          messages[channelId]?.map((msg) => {
            const { isToday, ampm, hour12, minutes, year, month, day } = timeHelper(
              msg.createdAt.toISOString()
            )

            return (
              <div
                key={msg.id}
                className='flex items-start gap-3'>
                <Avatar
                  size='sm'
                  avatarUrl='/image/common/default-avatar.png'
                  statusColor='black'
                  status='ONLINE'
                />

                <div className='flex-1'>
                  <div className='text-sm font-bold'>
                    유저유저
                    <span className='ml-2 text-xs text-gray-400'>
                      {isToday
                        ? `오늘 ${ampm} ${hour12}:${minutes}`
                        : `${year}. ${month}. ${day}. ${ampm} ${hour12}:${minutes}`}
                    </span>
                  </div>
                  <div className='text-sm'>{msg.contents.text}</div>
                </div>
              </div>
            )
          })}
        <div ref={messagesRef} />
      </div>

      {/* 입력창 */}
      <div className='px-4 pb-4'>
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
    </>
  )
}

export default ChatArea
