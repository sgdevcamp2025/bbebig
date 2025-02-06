import { Outlet } from 'react-router'
import Header from './components/header'
import { useState } from 'react'
import OnlineFriends from './components/online-friends'
import AllFriends from './components/all-friends'
import BlockedFriends from './components/blocked-friends'
import PendingFriends from './components/pending-friends'

type FriendTabType = 'ONLINE' | 'ALL' | 'BLOCK' | 'PENDING' | 'BLOCK_FRIENDS'

function Dm() {
  const [currentTab, setCurrentTab] = useState<FriendTabType>('ONLINE')

  const renderPage = () => {
    switch (currentTab) {
      case 'ONLINE':
        return <OnlineFriends />
      case 'ALL':
        return <AllFriends />
      case 'BLOCK':
        return <BlockedFriends />
      case 'PENDING':
        return <PendingFriends />
      case 'BLOCK_FRIENDS':
        return <BlockedFriends />
    }
  }

  return (
    <div className='flex-1 flex flex-col h-screen'>
      <Header
        onTabChange={setCurrentTab}
        currentTab={currentTab}
      />
      <div className='flex-1'>
        <Outlet />
        {renderPage()}
      </div>
    </div>
  )
}

export default Dm
