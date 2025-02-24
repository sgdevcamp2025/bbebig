import { lazy, Suspense } from 'react'
import { createBrowserRouter } from 'react-router-dom'

import LoadingModal from './components/loading-modal'

// 레이아웃 컴포넌트 동적 임포트
const RootLayout = lazy(() => import('./layouts/root-layout'))
const AuthLayout = lazy(() => import('./layouts/auth'))
const MainLayout = lazy(() => import('./layouts/main-layout'))
const ServerLayout = lazy(() => import('./layouts/server-layout'))
const DmLayout = lazy(() => import('./layouts/dm-layout'))

// 페이지 컴포넌트 동적 임포트
const LandingPage = lazy(() => import('./pages/landing'))
const LoginPage = lazy(() => import('./pages/auth/login'))
const RegisterPage = lazy(() => import('./pages/auth/register'))
const ChannelPage = lazy(() => import('./pages/channel'))
const FriendPage = lazy(() => import('./pages/friend'))
const DmPage = lazy(() => import('./pages/dm'))

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
        element: (
          <Suspense fallback={<LoadingModal isOpen />}>
            <AuthLayout />
          </Suspense>
        ),
        children: [
          {
            path: 'login',
            element: (
              <Suspense fallback={<LoadingModal isOpen />}>
                <LoginPage />
              </Suspense>
            )
          },
          {
            path: 'register',
            element: (
              <Suspense fallback={<LoadingModal isOpen />}>
                <RegisterPage />
              </Suspense>
            )
          }
        ]
      },
      {
        path: 'channels',
        element: (
          <Suspense fallback={<LoadingModal isOpen />}>
            <MainLayout />
          </Suspense>
        ),
        children: [
          {
            element: (
              <Suspense fallback={<LoadingModal isOpen />}>
                <ServerLayout />
              </Suspense>
            ),
            children: [
              {
                path: ':serverId/:channelId',
                element: (
                  <Suspense fallback={<LoadingModal isOpen />}>
                    <ChannelPage />
                  </Suspense>
                )
              }
            ]
          },
          {
            element: (
              <Suspense fallback={<LoadingModal isOpen />}>
                <DmLayout />
              </Suspense>
            ),
            children: [
              {
                path: '@me',
                element: (
                  <Suspense fallback={<LoadingModal isOpen />}>
                    <FriendPage />
                  </Suspense>
                )
              },
              {
                path: '@me/:memberId',
                element: (
                  <Suspense fallback={<LoadingModal isOpen />}>
                    <DmPage />
                  </Suspense>
                )
              }
            ]
          }
        ]
      }
    ]
  }
])
