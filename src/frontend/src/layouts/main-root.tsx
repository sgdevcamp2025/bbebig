import { Outlet, useLocation, useNavigate } from 'react-router'

import ServerIcon from '@/components/server-icon'
import { cn } from '@/libs/cn'

const myServerList = [
  {
    serverId: 1,
    name: '서버 이름',
    image: 'https://placehold.co/75',
    alarm: true,
    channelId: 1
  },
  {
    serverId: 2,
    name: '서버 이름',
    image: 'https://placehold.co/75',
    alarm: false,
    channelId: 2
  },
  {
    serverId: 3,
    name: '서버 이름',
    image: 'https://placehold.co/75',
    alarm: false,
    channelId: 3
  }
] as const

const MainRootLayout = () => {
  const location = useLocation()
  const navigate = useNavigate()
  const pathname =
    location.pathname.split('/')[1] === 'channels' ? location.pathname.split('/')[2] : null

  const handleClickServer = (serverId: number, channelId: number) => {
    navigate(`/channels/${serverId}/${channelId}`)
  }

  const handleClickMyServer = () => {
    navigate('/channels/@me')
  }

  return (
    <div className='flex'>
      <nav className='bg-black-80 h-full min-h-screen w-[72px] pt-[12px]'>
        <ul className='w-[72px] flex flex-col gap-2'>
          <li>
            <button
              type='button'
              onClick={handleClickMyServer}
              className='flex items-center justify-center relative w-full'>
              <div
                className={cn(
                  'absolute top-1/2 translate-y-[-50%] left-[-4px] w-2 rounded-r-[4px] overflow-hidden transition-all duration-300 bg-white',
                  pathname !== '@me' ? 'h-0' : 'h-10'
                )}
              />
              <div
                className={cn(
                  'w-[48px] h-[48px] flex items-center justify-center rounded-[48px] bg-brand-10 overflow-hidden transition-all duration-300 hover:rounded-[14px]',
                  pathname === '@me' ? 'rounded-[14px] bg-brand text-text-normal' : ''
                )}>
                <svg
                  aria-hidden='true'
                  role='img'
                  width='30'
                  height='30'
                  viewBox='0 0 24 24'>
                  <path
                    fill='#ffffff'
                    d='M19.73 4.87a18.2 18.2 0 0 0-4.6-1.44c-.21.4-.4.8-.58 1.21-1.69-.25-3.4-.25-5.1 0-.18-.41-.37-.82-.59-1.2-1.6.27-3.14.75-4.6 1.43A19.04 19.04 0 0 0 .96 17.7a18.43 18.43 0 0 0 5.63 2.87c.46-.62.86-1.28 1.2-1.98-.65-.25-1.29-.55-1.9-.92.17-.12.32-.24.47-.37 3.58 1.7 7.7 1.7 11.28 0l.46.37c-.6.36-1.25.67-1.9.92.35.7.75 1.35 1.2 1.98 2.03-.63 3.94-1.6 5.64-2.87.47-4.87-.78-9.09-3.3-12.83ZM8.3 15.12c-1.1 0-2-1.02-2-2.27 0-1.24.88-2.26 2-2.26s2.02 1.02 2 2.26c0 1.25-.89 2.27-2 2.27Zm7.4 0c-1.1 0-2-1.02-2-2.27 0-1.24.88-2.26 2-2.26s2.02 1.02 2 2.26c0 1.25-.88 2.27-2 2.27Z'
                  />
                </svg>
              </div>
            </button>
          </li>
          <div className='w-full flex justify-center'>
            <div className='h-[2px] w-8 rounded-[1px] bg-gray-80' />
          </div>
          {myServerList.map((server) => (
            <li key={server.serverId}>
              <ServerIcon
                imageUrl={server.image}
                label={server.name}
                isActive={pathname === server.serverId.toString()}
                hasAlarm={server.alarm}
                onClick={() => {
                  handleClickServer(server.serverId, server.channelId)
                }}
              />
            </li>
          ))}
        </ul>
      </nav>
      <div className='flex-1 bg-gray-20'>
        <Outlet />
      </div>
    </div>
  )
}

export default MainRootLayout
