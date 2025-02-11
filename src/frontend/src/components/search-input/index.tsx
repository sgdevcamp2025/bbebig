import { ComponentPropsWithoutRef } from 'react'

import { cn } from '@/libs/cn'

type Props = {
  handleClear: () => void
} & ComponentPropsWithoutRef<'input'>

function SearchInput({ handleClear, ...props }: Props) {
  return (
    <div className='w-full flex items-center bg-discord-gray-800 rounded-md px-3 py-1.5 justify-between gap-2'>
      <input
        className={cn('w-full bg-transparent outline-none text-white-10', props.className)}
        {...props}
      />
      {props.value ? (
        <button onClick={handleClear}>
          <img
            src='/icon/friend/close.svg'
            alt='close'
            className='w-4 h-4'
          />
        </button>
      ) : (
        <div>
          <img
            src='/icon/friend/search.svg'
            alt='search'
            className='w-4 h-4'
          />
        </div>
      )}
    </div>
  )
}

export default SearchInput
