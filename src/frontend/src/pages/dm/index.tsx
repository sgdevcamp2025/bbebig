import { ChangeEvent, Suspense, useState } from 'react'
import { useParams } from 'react-router-dom'

import ChatArea from '@/components/chat-area'
import CommonHeader from '@/components/common-header'
import LoadingIcon from '@/components/loading-icon'
import useGetSelfUser from '@/hooks/queries/user/useGetSelfUser'
import { useGetUserInfo } from '@/hooks/queries/user/useGetUserInfo'

import DmHeader from './components/dm-header'

function DmPage() {
  const { memberId } = useParams<{ memberId: string }>()
  const [searchValue, setSearchValue] = useState('')
  const memberInfo = useGetUserInfo(Number(memberId))
  const selfUserInfo = useGetSelfUser()

  const targetUser = {
    memberId: memberInfo.id,
    nickName: memberInfo.nickname,
    avatarUrl: memberInfo.avatarUrl,
    bannerUrl: memberInfo.bannerUrl,
    globalStatus: memberInfo.customPresenceStatus
  }

  const currentUser = {
    memberId: selfUserInfo.id,
    nickName: selfUserInfo.nickname,
    avatarUrl: selfUserInfo.avatarUrl,
    bannerUrl: selfUserInfo.bannerUrl,
    globalStatus: selfUserInfo.customPresenceStatus
  }

  const handleSearch = (e: ChangeEvent<HTMLInputElement>) => {
    setSearchValue(e.target.value)
  }

  const handleClear = () => {
    setSearchValue('')
  }

  if (!memberId) {
    return <div>사용자를 찾을 수 없습니다.</div>
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
        <DmHeader member={memberInfo} />
      </CommonHeader>
      <Suspense fallback={<LoadingIcon />}>
        <ChatArea
          chatKey={memberId}
          users={{
            currentUser: currentUser,
            targetUsers: [targetUser]
          }}
        />
      </Suspense>
    </div>
  )
}

export default DmPage
