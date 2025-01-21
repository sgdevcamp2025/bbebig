import { BrowserRouter, Route, Routes } from 'react-router-dom'

import LandingLayout from '@/layouts/ladning'
import Landing from '@/pages/landing'

function App() {
  return (
    <>
      <BrowserRouter>
        <Routes>
          <Route element={<LandingLayout />}>
            <Route
              path='/'
              element={<Landing />}
            />
          </Route>
        </Routes>
      </BrowserRouter>
    </>
  )
}

export default App
