type DmSideBarProps = {
  serverName?: string
}

function DmSideBar({ serverName }: DmSideBarProps) {
  return (
    <div className='w-60 bg-discord-gray-700 h-screen flex flex-col'>
      <div className='h-12 px-4 flex items-center justify-between border-b border-discord-gray-800'>
        <span className='text-discord-font-color-normal font-medium'>개인 대화창</span>
        <span className='text-discord-font-color-normal font-medium'>{serverName}</span>
      </div>
    </div>
  )
}

export default DmSideBar
