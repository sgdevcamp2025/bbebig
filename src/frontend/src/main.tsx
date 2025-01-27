import './styles/globals.css'

import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'

import App from './routes'
import { AuthProvider } from './store/auth/AuthProvider'

createRoot(document.getElementById('root') as HTMLElement).render(
  <StrictMode>
    <AuthProvider>
      <App />
    </AuthProvider>
  </StrictMode>
)
