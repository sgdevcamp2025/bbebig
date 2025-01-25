import { Link } from 'react-router-dom'

import { discordLogo } from '@/libs/discord-assets-path'

const navigation = [
  {
    href: '#',
    name: '다운로드'
  },
  {
    href: '#',
    name: 'Nitro'
  },
  {
    href: '#',
    name: '찾기'
  },
  {
    href: '#',
    name: '퀘스트'
  },
  {
    href: '#',
    name: '보안센터'
  },
  {
    href: '#',
    name: '지원'
  },
  {
    href: '#',
    name: '블로그'
  },
  {
    href: '#',
    name: '인재채용'
  }
] as const

interface Props {
  isLogin: boolean
}

function Header({ isLogin }: Props) {
  return (
    <div className='absolute left-0 right-0 top-0 mobile-range:sticky mobile-range:top-0 mobile-range:z-50 mobile-range:bg-blue-30'>
      <header
        role='banner'
        className='mx-auto flex min-h-20 w-[90%] max-w-[1180px] justify-between'>
        <div className='flex w-full items-center justify-between gap-4'>
          <img
            alt='Click for home'
            src={discordLogo}
            loading='lazy'
            className='h-auto w-[124px]'
          />
          <ul className='flex flex-row mobile-range:hidden tablet-range:hidden'>
            {navigation.map((item) => (
              <li className='mx-[10px] p-[10px]'>
                <a
                  href={item.href}
                  className='focus:text-underline font-semibold text-white'>
                  {item.name}
                </a>
              </li>
            ))}
          </ul>
          <div className='flex h-[38px] items-center rounded-full bg-white'>
            <Link
              to={isLogin ? '/discord' : '/login'}
              className='px-4 py-[7px] text-[14px] font-semibold leading-6 text-black transition-colors hover:text-brand'>
              {isLogin ? 'Discord 열기' : '로그인'}
            </Link>
          </div>
        </div>
      </header>
    </div>
  )
}

export default Header
