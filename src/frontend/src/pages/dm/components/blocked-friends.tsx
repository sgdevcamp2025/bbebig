import SearchInput from '@/components/search-input'

function BlockedFriends() {
  return (
    <div className='flex flex-col p-4'>
      <SearchInput
        onSearch={() => {
          console.log('검색')
        }}
        placeholder='검색'
      />
    </div>
  )
}

export default BlockedFriends
