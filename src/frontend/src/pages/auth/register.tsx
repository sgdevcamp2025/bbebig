import { zodResolver } from '@hookform/resolvers/zod'
import { useCallback, useState } from 'react'
import { useForm } from 'react-hook-form'
import { useNavigate } from 'react-router-dom'
import { registerRequestSchema } from '@/apis/schema/auth'
import { RegisterSchema } from '@/apis/schema/types/auth'
import authService from '@/apis/service/auth'
import AuthInput from '@/components/auth-input'
import CheckBox from '@/components/check-box'
import CustomButton from '@/components/custom-button'
import DateInput from '@/components/date-input'

function RegisterPage() {
  const navigate = useNavigate()
  const [movePage, setMovePage] = useState(false)
  const [checked, setChecked] = useState(false)

  const {
    register,
    handleSubmit,
    setValue,
    formState: { errors }
  } = useForm<RegisterSchema>({
    resolver: zodResolver(registerRequestSchema),
    defaultValues: {
      email: '',
      password: '',
      name: '',
      nickname: '',
      birthDate: ''
    }
  })

  const handleMoveLoginPage = useCallback(() => {
    setMovePage(true)

    setTimeout(() => {
      navigate('/login')
    }, 500)
  }, [navigate])

  const setBirthdate = useCallback(
    (value: string) => {
      setValue('birthDate', value)
    },
    [setValue]
  )

  const signUp = useCallback(
    async (data: RegisterSchema) => {
      await authService.register(data)

      handleMoveLoginPage()
    },
    [handleMoveLoginPage]
  )

  return (
    <form
      onSubmit={handleSubmit(signUp)}
      className={`p-8 bg-brand-10 w-[480px] rounded-lg motion-opacity-in-20 motion-translate-y-in-25 motion-blur-in-5 ${
        movePage ? 'motion-opacity-out-20 motion-translate-y-out-25 motion-blur-out-100' : ''
      }`}>
      <div>
        <h1 className='font-medium text-white text-center leading-[1.25] text-2xl'>계정 만들기</h1>
        <div className='flex flex-col gap-5 mt-5'>
          <AuthInput
            label='이메일'
            error={errors.email?.message}
            required
            {...register('email')}
          />
          <AuthInput
            label='별명'
            error={errors.nickname?.message}
            {...register('nickname')}
          />
          <AuthInput
            label='사용자명'
            error={errors.name?.message}
            required
            {...register('name')}
          />
          <AuthInput
            label='비밀번호'
            type='password'
            error={errors.password?.message}
            required
            {...register('password')}
          />
          <DateInput
            label='생년월일'
            required
            error={errors.birthDate?.message}
            setDate={setBirthdate}
          />
          <div>
            <CheckBox
              label={
                '(선택사항) Discord 소식, 도움말, 특별 할인을 이메일로 보내주세요. 언제든지 취소하실 수 있어요.'
              }
              isChecked={checked}
              onChange={setChecked}
            />
          </div>
          <div>
            <CustomButton
              type='submit'
              variant='primary'
              size='small'
              width='full'
              className='mb-1'>
              계속하기
            </CustomButton>
            <div className='text-white-20 text-[12px] mt-1'>
              등록하는 순간 Discord의 <a className='text-text-link'>서비스 이용 약관</a>와
              <a className='text-text-link'>개인정보 보호 정책</a>에 동의하게 됩니다.
            </div>
            <button
              type='button'
              onClick={handleMoveLoginPage}
              className='text-sm w-min-[130px] mt-5 w-auto h-4 text-text-link'>
              <span>이미 계정이 있으신가요?</span>
            </button>
          </div>
        </div>
      </div>
    </form>
  )
}

export default RegisterPage
