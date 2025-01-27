import { BrowserRouter, Route, Routes } from 'react-router-dom'

import AuthLayout from '@/layouts/auth'
import LandingLayout from '@/layouts/ladning'
import MainRootLayout from '@/layouts/main-root'
import PrivateLayout from '@/layouts/private'
import ChannelPage from '@/pages/channel'
import LandingPage from '@/pages/landing'
import LoginPage from '@/pages/login'
import Mypage from '@/pages/my'
import RegisterPage from '@/pages/register'

function App() {
  return (
    <>
      <BrowserRouter>
        <Routes>
          <Route element={<LandingLayout />}>
            <Route
              path='/'
              element={<LandingPage />}
            />
          </Route>

          <Route element={<AuthLayout />}>
            <Route
              path='/login'
              element={<LoginPage />}
            />
            <Route
              path='/register'
              element={<RegisterPage />}
            />
          </Route>

          <Route element={<PrivateLayout />}>
            <Route element={<MainRootLayout />}>
              <Route
                path='/channels/@me'
                element={<Mypage />}
              />
              <Route
                path='/channels/:serverId/:channelId'
                element={<ChannelPage />}
              />
            </Route>
          </Route>
        </Routes>
      </BrowserRouter>
    </>
  )
}

export default App
