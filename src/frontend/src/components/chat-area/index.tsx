import { ChangeEvent, useEffect, useRef, useState } from 'react'
import { useNavigate } from 'react-router-dom'

import Avatar from '@/components/avatar'
import CommonHeader from '@/components/common-header'
import { cn } from '@/libs/cn'
import DmAreaHeader from '@/pages/dm/components/dm-area-header'
import { useChatStore } from '@/stores/use-chat-store'
import { useChattingStomp } from '@/stores/use-chatting-stomp'
import useStatusBarStore from '@/stores/use-status-bar-store'
import { ChannelMessage } from '@/types/message'
import { ChatUser } from '@/types/user'
import timeHelper from '@/utils/format-time'

interface ChatProps {
  chatKey: string | number
  users: {
    currentUser: ChatUser
    targetUsers: ChatUser[]
  }
  isVoice?: boolean
  channelName?: string
  initialMessage?: string
  serverId?: number
  historyMessages?: ChannelMessage[]
  onClose?: () => void
}

function ChatArea({
  chatKey,
  users,
  isVoice,
  channelName,
  initialMessage = '',
  onClose,
  serverId,
  historyMessages
}: ChatProps) {
  const messagesRef = useRef<HTMLDivElement>(null)
  const inputRef = useRef<HTMLInputElement>(null)
  const { messages, setMessages } = useChatStore()

  const [searchValue, setSearchValue] = useState('')
  const { isStatusBarOpen, toggleStatusBar } = useStatusBarStore()

  const isChannel = channelName !== undefined
  const navigate = useNavigate()
  const { publishToServerChatting } = useChattingStomp()

  useEffect(() => {
    if (historyMessages && chatKey) {
      setMessages(Number(chatKey), historyMessages)
    }
  }, [historyMessages, chatKey])

  useEffect(() => {
    if (messagesRef.current) {
      messagesRef.current.scrollIntoView({ behavior: 'smooth' })
    }
  }, [messages])

  const sendMessage = async () => {
    if (!chatKey || !inputRef.current) return
    const text = inputRef.current.value.trim()
    if (!text) return

    if (!isChannel) {
      navigate(`/channels/@me/${users.targetUsers[0].memberId}`, {
        state: { initialMessage: text }
      })
      return
    }

    publishToServerChatting({
      chatType: channelName ? 'CHANNEL' : 'DM',
      messageType: 'TEXT',
      type: 'MESSAGE_CREATE',
      serverId: serverId ?? 0,
      channelId: Number(chatKey),
      content: text
    })

    inputRef.current.value = ''
  }

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
    <div className='flex-1 flex flex-col w-full h-full relative'>
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
          users.targetUsers && <DmAreaHeader member={users.targetUsers[0]} />
        )}

        {/* 채팅 영역 */}
        <div className='flex flex-col-reverse w-full h-full overflow-y-auto p-4'>
          <div ref={messagesRef} />
          {Array.isArray(messages[Number(chatKey)]) &&
            messages[Number(chatKey)]
              .sort((a, b) => (b.sequence ?? 0) - (a.sequence ?? 0))
              .map((msg) => {
                const { isToday, ampm, hour12, minutes, year, month, day } = timeHelper(
                  msg.createdAt ?? ''
                )

                const isMyMessage = msg.sendMemberId === users.currentUser?.memberId
                const messageUser = isMyMessage
                  ? users.currentUser
                  : users.targetUsers.find((user) => user.memberId === msg.sendMemberId) ||
                    users.targetUsers[0]

                const status =
                  [...users.targetUsers, users.currentUser]?.find(
                    (member) => member.memberId === messageUser?.memberId
                  )?.globalStatus ||
                  messageUser?.globalStatus ||
                  'ONLINE'

                return (
                  <div
                    key={`${msg.channelId}-${msg.id}`}
                    className='flex items-start gap-3 mb-4'>
                    <Avatar
                      size='sm'
                      avatarUrl={messageUser?.avatarUrl ?? '/image/common/default-avatar.png'}
                      statusColor='black'
                      status={status}
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
                      <div className='text-sm text-discord-font-color-normal'>{msg.content}</div>
                    </div>
                  </div>
                )
              })}
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
