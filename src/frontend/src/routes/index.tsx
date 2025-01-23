import { BrowserRouter, Route, Routes } from 'react-router-dom'

import AuthLayout from '@/layouts/auth'
import LandingLayout from '@/layouts/ladning'
import LandingPage from '@/pages/landing'
import LoginPage from '@/pages/login'
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
        </Routes>
      </BrowserRouter>
    </>
  )
}

export default App
