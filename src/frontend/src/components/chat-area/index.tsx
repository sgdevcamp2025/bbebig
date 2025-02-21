import { ChangeEvent, useEffect, useRef, useState } from 'react'
import { useNavigate } from 'react-router-dom'

import Avatar from '@/components/avatar'
import CommonHeader from '@/components/common-header'
import useChattingStomp from '@/hooks/store/use-chatting-stomp'
import { cn } from '@/libs/cn'
import DmAreaHeader from '@/pages/dm/components/dm-area-header'
import useStatusBarStore from '@/stores/use-status-bar-store'
import { Message } from '@/types/message'
import { ChatUser } from '@/types/user'
import timeHelper from '@/utils/format-time'

export interface ChatProps {
  chatKey: string | number
  users: {
    currentUser: ChatUser
    targetUser: ChatUser
  }
  isVoice?: boolean
  channelName?: string
  onClose?: () => void
  initialMessage?: string
}

function ChatArea({
  chatKey,
  users,
  isVoice,
  channelName,
  onClose,
  initialMessage = ''
}: ChatProps) {
  const messagesRef = useRef<HTMLDivElement>(null)
  const inputRef = useRef<HTMLInputElement>(null)
  const [messages, setMessages] = useState<Record<string, Message[]>>({})
  const [searchValue, setSearchValue] = useState('')
  const { isStatusBarOpen, toggleStatusBar } = useStatusBarStore()
  const currentUser = users.currentUser
  const targetUser = users.targetUser
  const isChannel = channelName !== undefined
  const navigate = useNavigate()
  const { isConnected, publishToServerChatting } = useChattingStomp()

  const sendMessage = () => {
    if (!chatKey || !inputRef.current) return
    const text = inputRef.current.value.trim()
    if (!text) return

    if (!isChannel) {
      navigate(`/channels/@me/${targetUser.memberId}`, {
        state: { initialMessage: text }
      })
      return
    }

    console.log('[✅] 채팅 서버 연결 상태 : ', isConnected)
    const newMessage: Message = {
      id: crypto.randomUUID(),
      memberId: currentUser?.memberId.toString() ?? '',
      type: isChannel ? 'CHANNEL' : 'DM',
      contents: { text },
      createdAt: new Date(),
      updatedAt: new Date()
    }

    setMessages((prev) => ({
      ...prev,
      [chatKey]: [...(prev[chatKey] || []), newMessage]
    }))

    inputRef.current.value = ''

    publishToServerChatting({
      chatType: channelName ? 'CHANNEL' : 'DM',
      messageType: 'TEXT',
      type: 'MESSAGE_CREATE',
      serverId: Number(chatKey),
      channelId: Number(chatKey),
      sendMemberId: currentUser.memberId,
      content: text,
      createdAt: new Date().toISOString(),
      updatedAt: new Date().toISOString()
    })
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
        {isChannel ? (
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
            <span className='text-discord-font-color-normal font-medium'>{channelName}</span>
          </CommonHeader>
        ) : (
          targetUser && <DmAreaHeader member={targetUser} />
        )}

        {/* 채팅 영역 */}
        <div className='flex-1 overflow-y-auto p-4 flex flex-col justify-end'>
          {messages[chatKey]?.map((msg) => {
            const { isToday, ampm, hour12, minutes, year, month, day } = timeHelper(
              msg.createdAt.toISOString()
            )

            const isMyMessage = msg.memberId === currentUser?.memberId.toString()
            const messageUser = isMyMessage ? currentUser : targetUser

            return (
              <div
                key={msg.id}
                className='flex items-start gap-3 mb-4'>
                <Avatar
                  size='sm'
                  avatarUrl={messageUser?.avatarUrl ?? '/image/common/default-avatar.png'}
                  statusColor='black'
                  status={messageUser?.globalStatus ?? 'ONLINE'}
                  name={messageUser?.nickName ?? '닉네임'}
                />

                <div className='flex-1'>
                  <div className='text-sm font-bold text-discord-font-color-normal'>
                    {isMyMessage ? messageUser?.nickName : (messageUser?.nickName ?? '닉네임')}
                    <span className='ml-2 text-xs text-gray-400'>
                      {isToday
                        ? `오늘 ${ampm} ${hour12}:${minutes}`
                        : `${year}. ${month}. ${day}. ${ampm} ${hour12}:${minutes}`}
                    </span>
                  </div>
                  <div className='text-sm text-discord-font-color-normal'>{msg.contents.text}</div>
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
              defaultValue={initialMessage}
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
