import Avatar from '@/components/avatar'
import { Message } from '@/types/message'
import formatTime from '@/utils/format-time'
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
          messages[channelId]?.map((msg) => (
            <div
              key={msg.id}
              className='flex items-start gap-3'>
              <Avatar
                size='sm'
                avatarUrl='data:image/jpeg;base64,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'
                statusColor='black'
                status='ONLINE'
              />

              <div className='flex-1'>
                <div className='text-sm font-bold'>
                  유저유저
                  <span className='ml-2 text-xs text-gray-400'>
                    {formatTime(msg.createdAt.toISOString())}
                  </span>
                </div>
                <div className='text-sm'>{msg.contents.text}</div>
              </div>
            </div>
          ))}
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
            <img src='/image/chat/gift.svg' />
            <img src='/image/chat/gif.svg' />
            <img src='/image/chat/sticker.svg' />
            <img src='/image/chat/emoji.svg' />
          </div>

          <button
            className='flex ml-5 px-3'
            onClick={sendMessage}>
            <img src='/image/chat/send.svg' />
          </button>
        </div>
      </div>
    </div>
  )
}

export default ChannelPage
