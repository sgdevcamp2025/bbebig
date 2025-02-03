import Avatar from '@/components/avatar'
import { Message } from '@/types/message'
import timeHelper from '@/utils/format-time'
import { useEffect, useRef, useState } from 'react'
import { useParams } from 'react-router'

function ChannelPage() {
  const { channelId } = useParams()
  const messagesRef = useRef<HTMLDivElement>(null)
  const inputRef = useRef<HTMLInputElement>(null)
  const [messages, setMessages] = useState<{ [channelId: string]: Message[] }>({})

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

  const handleKeyDown = (e: React.KeyboardEvent<HTMLInputElement>) => {
    if (e.key === 'Enter' && !e.nativeEvent.isComposing) {
      e.preventDefault()
      sendMessage()
    }
  }

  useEffect(() => {
    if (messagesRef.current) {
      const messageContainer = messagesRef.current.parentElement
      if (messageContainer) {
        messageContainer.scrollTop = messageContainer.scrollHeight
      }
    }
  }, [messages])

  return (
    <div className='flex-1 flex flex-col h-screen'>
      <div className='h-12  border-b border-discord-gray-800 px-4 flex items-center'>
        <span className='flex items-center gap-1.5 text-discord-font-color-normal font-medium'>
          <img
            width={17}
            height={17}
            src='/image/channel/type-text.svg'
          />
          채널 {channelId}
        </span>
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
            className='ml-3'
            width={25}
            height={25}
            src='/image/chat/plus.svg'
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
                src={`/image/chat/${icon}.svg`}
              />
            ))}
          </div>

          <button
            className='flex ml-5 px-3'
            type='button'
            onClick={sendMessage}>
            <img src='/image/chat/send.svg' />
          </button>
        </div>
      </div>
    </div>
  )
}

export default ChannelPage
