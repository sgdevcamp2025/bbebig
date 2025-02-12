import { useEffect, useRef, useState } from 'react'

import Avatar from '@/components/avatar'
import { Friend } from '@/types/friend'
import { Message } from '@/types/message'
import { User } from '@/types/user'
import timeHelper from '@/utils/format-time'

interface DmPageProps {
  friend: Friend
}

const DUMMY_USER: User = {
  id: '1',
  avatarUrl: '/image/homepage/background-art.png',
  name: '이소은',
  email: 'soeun@gmail.com',
  bannerUrl: '/image/common/default-background.png',
  customPresenceStatus: 'ONLINE',
  introduction: '안녕하세요',
  introductionEmoji: '👋'
}

function DmArea({ friend }: DmPageProps) {
  const messagesRef = useRef<HTMLDivElement>(null)
  const [messages, setMessages] = useState<Record<string, Message[]>>({})
  const inputRef = useRef<HTMLInputElement>(null)

  const sendMessage = () => {
    if (!inputRef.current || !friend.id) return
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
      [friend.id]: [...(prev[friend.id] || []), newMessage]
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

  return (
    <div className='flex flex-col h-full relative'>
      <div className='absolute inset-0 flex flex-col'>
        <div className='flex-1 overflow-y-auto'>
          <div className='flex flex-col justify-end min-h-full p-4'>
            {friend.id &&
              messages[friend.id]?.map((msg) => {
                const { isToday, ampm, hour12, minutes, year, month, day } = timeHelper(
                  msg.createdAt.toISOString()
                )

                const isMyMessage = msg.memberId === DUMMY_USER.id
                console.log(msg, DUMMY_USER)

                return (
                  <div
                    key={msg.id}
                    className='flex items-start gap-3 mb-4'>
                    <Avatar
                      size='sm'
                      avatarUrl={isMyMessage ? DUMMY_USER.avatarUrl : friend.avatarUrl}
                      statusColor='black'
                      status={isMyMessage ? DUMMY_USER.customPresenceStatus : friend.status}
                    />

                    <div className='flex-1'>
                      <div className='text-sm font-bold text-discord-font-color-normal'>
                        {isMyMessage ? DUMMY_USER.name : friend.name}
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
        </div>

        <div className='sticky bottom-0'>
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
        </div>
      </div>
    </div>
  )
}

export default DmArea
