import { useState } from 'react'
import { Outlet } from 'react-router'

import AllFriends from './components/all-friends'
import Header from './components/header'
import OnlineFriends from './components/online-friends'
import PendingFriends from './components/pending-friends'

type FriendTabType = 'ONLINE' | 'ALL' | 'PENDING'

function Dm() {
  const [currentTab, setCurrentTab] = useState<FriendTabType>('ONLINE')

  const renderPage = () => {
    switch (currentTab) {
      case 'ONLINE':
        return <OnlineFriends />
      case 'ALL':
        return <AllFriends />
      case 'PENDING':
        return <PendingFriends />
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
