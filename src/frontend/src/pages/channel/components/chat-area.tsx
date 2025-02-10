import { X } from 'lucide-react'
import { useEffect, useRef, useState } from 'react'

import Avatar from '@/components/avatar'
import { cn } from '@/libs/cn'
import { Message } from '@/types/message'
import timeHelper from '@/utils/format-time'

interface Props {
  channelId: number
  onClose?: () => void
}

function ChatArea({ channelId, onClose }: Props) {
  const messagesRef = useRef<HTMLDivElement>(null)
  const [messages, setMessages] = useState<Record<string, Message[]>>({})
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

  const isVoice = Boolean(onClose)

  return (
    <>
      <div
        className={cn('h-12 border-b border-discord-gray-800 px-4 flex items-center', {
          'justify-between': isVoice
        })}>
        <span className='flex items-center gap-1.5 text-discord-font-color-normal font-medium'>
          {isVoice ? (
            <img
              className='w-[17px] h-[17px]'
              src='/icon/channel/type-text.svg'
            />
          ) : (
            <img
              src='/icon/channel/threads.svg'
              className='w-5 h-5'
            />
          )}
          채널 {channelId}
        </span>
        {isVoice && (
          <button
            type='button'
            onClick={onClose}>
            <X className='text-white-20' />
          </button>
        )}
      </div>
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
        <div className='h-12 flex items-center bg-discord-gray-600 rounded-md'>
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
