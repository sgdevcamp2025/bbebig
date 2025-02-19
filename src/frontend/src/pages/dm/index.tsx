import { useQueryClient } from '@tanstack/react-query'
import { ChangeEvent, Suspense, useState } from 'react'
import { useParams } from 'react-router'

import CommonHeader from '@/components/common-header'
import LoadingIcon from '@/components/loading-icon'
import { Friend } from '@/types/friend'

import DmArea from './components/dm-area'
import DmHeader from './components/dm-header'

function DmPage() {
  const { friendId } = useParams<{ friendId: string }>()
  const [searchValue, setSearchValue] = useState('')
  const queryClient = useQueryClient()

  const friendData = queryClient.getQueryData<{ result: { friends: Friend[] } }>(['friend', 'list'])

  const selectedFriend = friendData?.result.friends.find((f) => f.memberId === Number(friendId))

  const handleSearch = (e: ChangeEvent<HTMLInputElement>) => {
    setSearchValue(e.target.value)
  }

  const handleClear = () => {
    setSearchValue('')
  }
  if (!selectedFriend) {
    return <div>친구를 찾을 수 없습니다.</div>
  }

  return (
    <div className='flex flex-col h-screen'>
      <CommonHeader
        type='DM'
        searchProps={{
          value: searchValue,
          onChange: handleSearch,
          handleClear: handleClear,
          placeholder: '채팅 검색하기'
        }}>
        <DmHeader friend={selectedFriend} />
      </CommonHeader>
      <Suspense fallback={<LoadingIcon />}>
        <DmArea friend={selectedFriend} />
      </Suspense>
    </div>
  )
}

export default DmPage
