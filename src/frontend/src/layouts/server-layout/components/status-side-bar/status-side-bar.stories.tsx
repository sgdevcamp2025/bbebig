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
    users: [
      {
        id: '1',
        name: '김예지',
        avatarUrl: 'https://via.placeholder.com/150',
        bannerUrl: 'https://via.placeholder.com/150',
        customPresenceStatus: 'ONLINE',
        introduction: '안뇽',
        introductionEmoji: '👋',
        email: 'yeji@gmail.com'
      },
      {
        id: '2',
        name: '이지형',
        avatarUrl: 'https://via.placeholder.com/150',
        bannerUrl: 'https://via.placeholder.com/150',
        customPresenceStatus: 'NOT_DISTURB',
        introduction: '하이루',
        introductionEmoji: '👋',
        email: 'jihyung@gmail.com'
      },
      {
        id: '2',
        name: '이소은',
        avatarUrl: 'https://via.placeholder.com/150',
        bannerUrl: 'https://via.placeholder.com/150',
        customPresenceStatus: 'OFFLINE',
        introduction: '뇽안',
        introductionEmoji: '👋',
        email: 'soeun@gmail.com'
      }
    ]
  }
}
