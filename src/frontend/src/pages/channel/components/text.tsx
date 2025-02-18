import ChatArea from '@/components/chat-area'

interface Props {
  channelId: number
}

function TextComponent({ channelId }: Props) {
  return (
    <div className='flex-1 flex flex-col h-screen'>
      <ChatArea
        channelId={channelId}
        isVoice={false}
      />
    </div>
  )
}

export default TextComponent
