import { Meta, StoryObj } from '@storybook/react'

import StatusSideBar from '.'

const meta = {
  title: 'Layout/StatusLayout/StatusSideBar',
  component: StatusSideBar
} satisfies Meta<typeof StatusSideBar>

export default meta

type Story = StoryObj<typeof meta>

export const PrimaryStatusSidebar: Story = {
  args: {
    channelUserList: [
      {
        memberId: 1,
        nickname: '김예지',
        profileImageUrl: 'https://via.placeholder.com/150',
        joinAt: '2024-01-01',
        customPresenceStatus: 'ONLINE'
      },
      {
        memberId: 2,
        nickname: '이지형',
        profileImageUrl: 'https://via.placeholder.com/150',
        joinAt: '2024-01-01',
        customPresenceStatus: 'NOT_DISTURB'
      },
      {
        memberId: 3,
        nickname: '이소은',
        profileImageUrl: 'https://via.placeholder.com/150',
        joinAt: '2024-01-01',
        customPresenceStatus: 'OFFLINE'
      }
    ]
  }
}
