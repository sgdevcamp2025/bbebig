import { createBrowserRouter } from 'react-router-dom'

import AuthLayout from './layouts/auth'
import DmLayout from './layouts/dm-layout'
import MainLayout from './layouts/main-layout'
import RootLayout from './layouts/root-layout'
import ServerLayout from './layouts/server-layout'
import LoginPage from './pages/auth/login'
import RegisterPage from './pages/auth/register'
import ChannelPage from './pages/channel'
import DmPage from './pages/dm'
import FriendPage from './pages/friend'
import LandingPage from './pages/landing'

// ... 다른 imports ...

export const router = createBrowserRouter([
  {
    path: '/',
    element: <RootLayout />,
    children: [
      {
        index: true,
        element: <LandingPage />
      },
      {
        element: <AuthLayout />,
        children: [
          { path: 'login', element: <LoginPage /> },
          { path: 'register', element: <RegisterPage /> }
        ]
      },
      {
        path: 'channels',
        element: <MainLayout />, // MainLayout 추가
        children: [
          {
            element: <ServerLayout />,
            children: [{ path: ':serverId/:channelId', element: <ChannelPage /> }]
          },
          {
            element: <DmLayout />,
            children: [
              { path: '@me', element: <FriendPage /> },
              { path: '@me/:friendId', element: <DmPage /> }
            ]
          }
        ]
      }
    ]
  }
])
