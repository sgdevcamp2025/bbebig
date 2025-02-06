import { type RouteConfig, index, layout, prefix, route } from '@react-router/dev/routes'

export default [
  layout('layouts/root-layout.tsx', [
    index('pages/landing/index.tsx'),

    layout('layouts/auth.tsx', [
      route('login', 'pages/auth/login.tsx'),
      route('register', 'pages/auth/register.tsx')
    ]),

    layout('layouts/main-layout/index.tsx', [
      ...prefix('channels', [
        layout('layouts/server-layout/index.tsx', [
          route(':serverId/:channelId', 'pages/channel/index.tsx')
        ]),
        layout('layouts/dm-layout/index.tsx', [route('@me', 'pages/dm/index.tsx')])
      ])
    ])
  ])
] satisfies RouteConfig
