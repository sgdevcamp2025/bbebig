import ChatArea from './chat-area'

type Props = {
  channelId: number
}

function TextComponent({ channelId }: Props) {
  return (
    <div className='flex-1 flex flex-col h-screen'>
      <ChatArea channelId={channelId} />
    </div>
  )
}

export default TextComponent
