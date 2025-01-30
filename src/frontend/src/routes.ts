import { type RouteConfig, index, layout, prefix, route } from '@react-router/dev/routes'

export default [
  layout('layouts/root-layout.tsx', [
    index('pages/landing/index.tsx'),

    layout('layouts/auth.tsx', [
      route('login', 'pages/auth/login.tsx'),
      route('register', 'pages/auth/register.tsx')
    ]),

    layout('layouts/main-root.tsx', [
      ...prefix('channels', [
        route('@me', 'pages/my/index.tsx'),
        route(':serverId/:channelId', 'pages/channel/index.tsx')
      ])
    ])
  ])
] satisfies RouteConfig
