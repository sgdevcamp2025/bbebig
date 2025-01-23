import { Link } from 'react-router-dom'

import AuthInput from '@/components/auth-input'

function LoginPage() {
  return (
    <form className='p-8 bg-brand-10 rounded-lg motion-opacity-in-20 motion-translate-y-in-25 motion-blur-in-5'>
      <div className='flex flex-row w-[720px] gap-8'>
        <div className='flex-1'>
          <div className='flex flex-col text-center'>
            <h2 className='text-2xl font-bold mb-2 text-white-10'>돌아오신 것을 환영해요!</h2>
            <div className='leading-[1.25] text-white-20'>다시 만나다니 너무 반가워요!</div>
          </div>
          <div className='w-full mt-5'>
            <div className='mb-5'>
              <AuthInput
                label='이메일 또는 전화번호'
                required
              />
            </div>
            <AuthInput
              label='비밀번호'
              required
            />
            <button className='text-sm text-text-link mb-5'>비밀번호를 잊으셨나요?</button>
            <button className='rounded-[3px] w-full h-10 rpim bg-brand text-white-100 mb-1 leading-[24px]'>
              로그인
            </button>
            <div className='text-white-20 text-sm'>
              계정이 필요한가요?{' '}
              <Link
                to='/register'
                className='text-text-link'>
                가입하기
              </Link>
            </div>
          </div>
        </div>
        <div className='w-60 h-[344px] flex flex-col items-center justify-center text-center'>
          <div className='w-[178px] h-[178px] rounded-[4px] bg-white-10 mb-8' />
          <h2 className='text-2xl font-bold text-white-10'>QR 코드로 로그인</h2>
          <div className='text-gray-500 leading-[1.25]'>
            <strong>Discord 모바일 앱</strong>으로 스캔해 바로 로그인 하세요.
          </div>
          <button className='text-sm w-min-[130px] px-4 py-[2px] w-auto h-[44px] text-text-link'>
            <span>또는, 패스키로 로그인하세요</span>
          </button>
        </div>
      </div>
    </form>
  )
}

export default LoginPage
