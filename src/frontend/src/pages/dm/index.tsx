import { useQueryClient } from '@tanstack/react-query'
import { ChangeEvent, Suspense, useState } from 'react'
import { useParams } from 'react-router'

import ChatArea from '@/components/chat-area'
import CommonHeader from '@/components/common-header'
import LoadingIcon from '@/components/loading-icon'
import { Friend } from '@/types/friend'

import DmHeader from './components/dm-header'
<<<<<<< HEAD

const DUMMY_FRIENDS: Friend[] = [
  {
    friendId: 1,
    friendMemberId: 1,
    friendName: '이지형',
    friendNickname: '지형',
    friendAvatarUrl: '/image/common/default-avatar.png',
    friendBannerUrl: '/image/common/default-banner.png',
    friendIntroduce: '안녕하세요',
    friendEmail: 'test@test.com'
  },
  {
    friendId: 2,
    friendMemberId: 2,
    friendName: '김예지',
    friendNickname: '예지',
    friendAvatarUrl: '/image/common/default-avatar.png',
    friendBannerUrl: '/image/common/default-banner.png',
    friendIntroduce: '안녕하세요',
    friendEmail: 'test@test.com'
  },
  {
    friendId: 3,
    friendMemberId: 3,
    friendName: '서정우',
    friendNickname: '정우',
    friendAvatarUrl: '/image/common/default-avatar.png',
    friendBannerUrl: '/image/common/default-banner.png',
    friendIntroduce: '안녕하세요',
    friendEmail: 'test@test.com'
  }
]
=======
>>>>>>> dev/fe

function DmPage() {
  const { friendId } = useParams<{ friendId: string }>()
  const [searchValue, setSearchValue] = useState('')
<<<<<<< HEAD
  const selectedFriend = DUMMY_FRIENDS.find((friend) => friend.friendId === Number(friendId))
=======
  const queryClient = useQueryClient()

  const friendData = queryClient.getQueryData<{ result: { friends: Friend[] } }>(['friend', 'list'])

  const selectedFriend = friendData?.result.friends.find((f) => f.memberId === Number(friendId))

>>>>>>> dev/fe
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
<<<<<<< HEAD
      <ChatArea friend={selectedFriend} />
=======
      <Suspense fallback={<LoadingIcon />}>
        <DmArea friend={selectedFriend} />
      </Suspense>
>>>>>>> dev/fe
    </div>
  )
}

export default DmPage
