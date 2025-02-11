import { ChangeEvent, useState } from 'react'

interface Props {
  placeholder?: string
  onSearch: (value: string) => void
}

function SearchInput({ placeholder = '검색하기', onSearch }: Props) {
  const [value, setValue] = useState('')

  const onChangeInput = (e: ChangeEvent<HTMLInputElement>) => {
    const inputValue = e.target.value
    setValue(inputValue)
    onSearch(inputValue)
  }

  const handleClear = () => {
    setValue('')
    onSearch('')
  }

  return (
    <div className='w-full flex items-center bg-discord-gray-800 rounded-md px-3 py-1.5 justify-between gap-2'>
      <input
        type='text'
        placeholder={placeholder}
        value={value}
        onChange={onChangeInput}
        className='w-full bg-transparent outline-none text-white-10'
      />
      {value ? (
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
