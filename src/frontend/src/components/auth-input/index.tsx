import { ComponentProps } from 'react'

type Props = {
  label: string
  required?: boolean
  error?: string
} & ComponentProps<'input'>

function AuthInput({ label, required, error, ...props }: Props) {
  return (
    <div className=''>
      <label className='text-white-20 mb-2 block font-bold text-[12px] leading-[1.33] bold'>
        {label} {required && <span className='text-red-10'>*</span>}
        {error && <span className='text-red-10'>{error}</span>}
      </label>
      <input
        className='bg-black-80 w-full h-[44px] rounded-[3px] text-white-100 outline-none p-[10px]'
        {...props}
      />
    </div>
  )
}

export default AuthInput
