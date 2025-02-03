import Avatar from '@/components/avatar'
import { Message } from '@/types/message'
import { useEffect, useRef, useState } from 'react'
import { useParams } from 'react-router'

function formatTime(timestamp: string) {
  const date = new Date(timestamp)
  const today = new Date()
  const isToday = date.toDateString() === today.toDateString()

  const hours = date.getHours()
  const minutes = date.getMinutes().toString().padStart(2, '0')
  const ampm = hours >= 12 ? '오후' : '오전'
  const hour12 = hours % 12 || 12

  if (isToday) {
    return `오늘 ${ampm} ${hour12}:${minutes}`
  }

  const year = date.getFullYear()
  const month = date.getMonth() + 1
  const day = date.getDate()

  return `${year}. ${month}. ${day}. ${ampm} ${hour12}:${minutes}`
}

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
          <svg
            width='20'
            height='20'
            viewBox='0 0 14 15'
            fill='none'>
            <path
              d='M3.01868 13.4974C2.81203 13.4974 2.65554 13.3101 2.69166 13.1059L3.09404 10.8313H0.832232C0.625894 10.8313 0.469496 10.6446 0.505061 10.4406L0.62131 9.77409C0.649123 9.61466 0.787134 9.49828 0.948481 9.49828H3.32654L4.03067 5.49914H1.76887C1.56253 5.49914 1.40613 5.31236 1.4417 5.10843L1.55794 4.44191C1.58576 4.28244 1.72377 4.16609 1.88512 4.16609H4.26317L4.68616 1.77502C4.7143 1.61592 4.85214 1.5 5.01318 1.5H5.66708C5.87371 1.5 6.03022 1.68734 5.99408 1.8915L5.59173 4.16609H9.5774L10.0004 1.77502C10.0285 1.61592 10.1664 1.5 10.3274 1.5H10.9813C11.188 1.5 11.3445 1.68734 11.3083 1.8915L10.906 4.16609H13.1678C13.3741 4.16609 13.5305 4.35287 13.4949 4.5568L13.3787 5.22333C13.3509 5.38279 13.2129 5.49914 13.0515 5.49914H10.6735L9.96933 9.49828H12.2311C12.4375 9.49828 12.5939 9.68504 12.5583 9.889L12.442 10.5555C12.4143 10.715 12.2762 10.8313 12.1149 10.8313H9.73683L9.31382 13.2224C9.28572 13.3815 9.14788 13.4974 8.98679 13.4974H8.33294C8.12629 13.4974 7.96978 13.3101 8.00592 13.1059L8.40827 10.8313H4.4226L3.99961 13.2224C3.97147 13.3815 3.83362 13.4974 3.67258 13.4974H3.01868ZM5.35953 5.49914L4.65539 9.49828H8.64104L9.34517 5.49914H5.35953Z'
              fill='#72767D'
            />
          </svg>
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
          <svg
            className='ml-3'
            width='25'
            height='25'
            viewBox='0 0 16 17'
            fill='none'>
            <path
              d='M8 0.5C3.5888 0.5 0 4.0888 0 8.50001C0 12.9112 3.5888 16.5 8 16.5C12.4112 16.5 16 12.9112 16 8.50001C16 4.0888 12.4112 0.5 8 0.5ZM12 9.30001H8.8V12.5H7.2V9.30001H4V7.70001H7.2V4.5H8.8V7.70001H12V9.30001Z'
              fill='#B9BBBE'
            />
          </svg>

          <input
            type='text'
            ref={inputRef}
            placeholder='메시지 보내기'
            className='w-full bg-transparent text-white px-3 py-2 outline-none focus-none'
            onKeyDown={handleKeyDown}
          />

          <div className='flex gap-3 items-center'>
            <svg
              width='16'
              height='17'
              viewBox='0 0 16 17'
              fill='none'>
              <path
                fill-rule='evenodd'
                clip-rule='evenodd'
                d='M14.4 5.28346H11.9088C12.2696 5.09518 12.6112 4.86524 12.8968 4.58002C13.832 3.64344 13.832 2.11879 12.8968 1.1798C11.9888 0.273666 10.4112 0.272865 9.5032 1.1806C8.1936 2.49214 8.0176 4.96058 8.0016 5.23779C8.00075 5.24622 8.0026 5.25397 8.00446 5.26177L8.00464 5.26252C8.00632 5.26931 8.008 5.27613 8.008 5.28346H7.992C7.992 5.2762 7.9936 5.26944 7.99528 5.26266C7.99728 5.25444 7.99928 5.2462 7.9984 5.23699C7.9832 4.95978 7.8064 2.49134 6.4968 1.1798C5.5888 0.273666 4.0096 0.273666 3.1032 1.18141C2.1672 2.11799 2.1672 3.64263 3.1032 4.58002C3.3888 4.86524 3.7304 5.09518 4.0912 5.28346H1.6C0.7176 5.28346 0 6.00212 0 6.88582V8.48818H16V6.88582C16 6.00212 15.2832 5.28346 14.4 5.28346ZM0.8 14.8976V10.0905H7.2V16.5H2.4C1.5176 16.5 0.8 15.7813 0.8 14.8976ZM8.8 10.0905V16.5H13.6C14.4832 16.5 15.2 15.7813 15.2 14.8976V10.0905H8.8ZM4.23443 2.31347C3.92243 2.62593 3.92243 3.13388 4.23443 3.44715C4.70323 3.91664 5.55603 4.19385 6.26003 4.34046C6.11283 3.63382 5.83763 2.78617 5.36563 2.31347C5.21523 2.16205 5.01443 2.07953 4.80003 2.07953C4.58563 2.07953 4.38483 2.16205 4.23443 2.31347ZM10.6344 2.31428C10.1616 2.78697 9.88723 3.63462 9.73923 4.34207C10.4456 4.19625 11.2928 3.92064 11.7656 3.44715C12.0776 3.13388 12.0768 2.62593 11.7648 2.31267C11.6152 2.16205 11.4144 2.07953 11.2 2.07953C10.9856 2.07953 10.7848 2.16205 10.6344 2.31428Z'
                fill='#B9BBBE'
              />
            </svg>

            <svg
              width='18'
              height='15'
              viewBox='0 0 18 15'
              fill='none'>
              <path
                d='M1.4702 0C0.658231 0 0 0.671572 0 1.5V13.5C0 14.3285 0.65823 15 1.4702 15H16.1722C16.9842 15 17.6424 14.3285 17.6424 13.5V1.5C17.6424 0.671572 16.9842 0 16.1722 0H1.4702ZM7.17784 7.086V10.11C6.54272 10.533 5.79291 10.767 4.9549 10.767C3.02306 10.767 1.9557 9.471 1.9557 7.554C1.9557 5.628 3.11127 4.323 4.99019 4.323C5.73999 4.323 6.36629 4.503 6.85146 4.782L6.64857 6.123C6.18987 5.826 5.65177 5.592 5.02547 5.592C3.97575 5.592 3.46412 6.384 3.46412 7.545C3.46412 8.715 3.99339 9.534 5.04311 9.534C5.37832 9.534 5.61649 9.462 5.86348 9.336V8.229H4.72555V7.086H7.17784ZM8.489 4.44H9.99743V10.65H8.489V4.44ZM15.0492 4.44V5.727H12.9057V6.996H14.5994V8.283H12.9057V10.65H11.4061V4.44H15.0492Z'
                fill='#B9BBBE'
              />
            </svg>

            <svg
              width='16'
              height='15'
              viewBox='0 0 16 15'
              fill='none'>
              <path
                fill-rule='evenodd'
                clip-rule='evenodd'
                d='M8.14233 4.0656V0.045823H2.14233C1.74451 0.045823 1.36298 0.20385 1.08167 0.485139C0.800369 0.766428 0.642334 1.14794 0.642334 1.54574L0.642334 13.5001C0.642334 13.8979 0.800369 14.2794 1.08167 14.5607C1.36298 14.842 1.74451 15 2.14233 15H14.1423C14.5402 15 14.9217 14.842 15.203 14.5607C15.4843 14.2794 15.6423 13.8979 15.6423 13.5001V7.50041H11.5773C10.6681 7.49452 9.79785 7.13074 9.15494 6.48786C8.51203 5.84499 8.14823 4.97475 8.14233 4.0656ZM8.14233 11.6627C7.47996 11.6627 6.84471 11.3996 6.37633 10.9312C5.90796 10.4629 5.64483 9.82766 5.64483 9.16532H6.64233C6.64233 9.56312 6.80037 9.94463 7.08167 10.2259C7.36298 10.5072 7.74451 10.6652 8.14233 10.6652C8.54016 10.6652 8.92169 10.5072 9.20299 10.2259C9.4843 9.94463 9.64233 9.56312 9.64233 9.16532H10.6398C10.6398 9.82766 10.3767 10.4629 9.90833 10.9312C9.43996 11.3996 8.80471 11.6627 8.14233 11.6627ZM9.64233 4.0506V0.495798C9.64066 0.397732 9.66861 0.301442 9.72252 0.21951C9.77644 0.137577 9.85382 0.0738135 9.94455 0.036551C10.0353 -0.000711444 10.1351 -0.00973968 10.2311 0.0106461C10.327 0.0310318 10.4146 0.0798834 10.4823 0.150817L15.4998 5.16054C15.5675 5.23016 15.6132 5.31815 15.6313 5.41354C15.6493 5.50894 15.6389 5.60754 15.6013 5.69706C15.5637 5.78658 15.5007 5.86308 15.42 5.91704C15.3392 5.97099 15.2444 6.00001 15.1473 6.0005H11.5923C11.336 6.00149 11.082 5.95173 10.8449 5.85409C10.6079 5.75645 10.3925 5.61286 10.2113 5.4316C10.03 5.25033 9.88639 5.03499 9.78875 4.79797C9.6911 4.56095 9.64134 4.30694 9.64233 4.0506ZM4.26302 9.8272C4.26302 10.3985 3.79987 10.8616 3.22854 10.8616C2.65721 10.8616 2.19406 10.3985 2.19406 9.8272C2.19406 9.25591 2.65721 8.79278 3.22854 8.79278C3.79987 8.79278 4.26302 9.25591 4.26302 9.8272ZM13.0561 10.8616C13.6275 10.8616 14.0906 10.3985 14.0906 9.8272C14.0906 9.25591 13.6275 8.79278 13.0561 8.79278C12.4848 8.79278 12.0216 9.25591 12.0216 9.8272C12.0216 10.3985 12.4848 10.8616 13.0561 10.8616Z'
                fill='#B9BBBE'
              />
            </svg>

            <svg
              width='18'
              height='17'
              viewBox='0 0 18 17'
              fill='none'>
              <path
                d='M9.14233 17C13.8368 17 17.6423 13.1944 17.6423 8.5C17.6423 3.80558 13.8368 0 9.14233 0C4.44791 0 0.642334 3.80558 0.642334 8.5C0.642334 13.1944 4.44791 17 9.14233 17Z'
                fill='#B9BBBE'
              />
              <path
                d='M9.1422 9.91668C7.43134 9.91668 6.29612 9.7174 4.8922 9.44446C4.57156 9.3826 3.94775 9.44446 3.94775 10.3889C3.94775 12.2778 6.11761 14.6389 9.1422 14.6389C12.1663 14.6389 14.3366 12.2778 14.3366 10.3889C14.3366 9.44446 13.7128 9.38212 13.3922 9.44446C11.9883 9.7174 10.8531 9.91668 9.1422 9.91668Z'
                fill='#40444B'
              />
              <path
                d='M4.89233 10.3889C4.89233 10.3889 6.309 10.8611 9.14233 10.8611C11.9757 10.8611 13.3923 10.3889 13.3923 10.3889C13.3923 10.3889 12.4479 12.2778 9.14233 12.2778C5.83678 12.2778 4.89233 10.3889 4.89233 10.3889Z'
                fill='white'
              />
              <path
                d='M6.30897 8.02778C6.96098 8.02778 7.48953 7.28781 7.48953 6.37501C7.48953 5.4622 6.96098 4.72223 6.30897 4.72223C5.65697 4.72223 5.12842 5.4622 5.12842 6.37501C5.12842 7.28781 5.65697 8.02778 6.30897 8.02778Z'
                fill='#40444B'
              />
              <path
                d='M11.9757 8.02778C12.6277 8.02778 13.1563 7.28781 13.1563 6.37501C13.1563 5.4622 12.6277 4.72223 11.9757 4.72223C11.3237 4.72223 10.7952 5.4622 10.7952 6.37501C10.7952 7.28781 11.3237 8.02778 11.9757 8.02778Z'
                fill='#40444B'
              />
            </svg>
          </div>

          <button
            className='px-2'
            onClick={sendMessage}>
            <svg
              width='17'
              height='15'
              viewBox='0 0 17 15'
              fill='none'>
              <path
                d='M8.66219 7.9981L2.34338 8.609L0.6806 13.9269C0.581456 14.2413 0.678712 14.585 0.92892 14.8003C1.17818 15.0155 1.53227 15.0609 1.82877 14.9164L15.6767 8.21059C15.9477 8.0784 16.1196 7.80363 16.1196 7.50242C16.1196 7.20121 15.9476 6.92645 15.6767 6.79425L1.8382 0.0836024C1.54171 -0.060864 1.18763 -0.0155414 0.938347 0.199742C0.688118 0.415027 0.590873 0.757778 0.690027 1.07221L2.35279 6.39006L8.6593 7.00191C8.91423 7.0274 9.10875 7.24174 9.10875 7.49762C9.10875 7.75352 8.91424 7.96784 8.6593 7.99334L8.66219 7.9981Z'
                fill='#949CF7'
              />
            </svg>
          </button>
        </div>
      </div>
    </div>
  )
}

export default ChannelPage
