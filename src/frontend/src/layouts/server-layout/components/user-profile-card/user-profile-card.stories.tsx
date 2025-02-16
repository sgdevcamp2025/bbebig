import type { Meta, StoryObj } from '@storybook/react'
import { MemoryRouter } from 'react-router-dom'

import { User } from '@/types/user'

import { Inner } from '.'

const user: User = {
  id: 1,
  name: '김예지',
  avatarUrl: '/image/common/default-avatar.png',
  bannerUrl: '/image/common/default-background.png',
  customPresenceStatus: 'ONLINE',
  introduce: { text: '안뇽', emoji: '👋' },
  email: 'yeji@gmail.com'
}

const meta: Meta<typeof Inner> = {
  title: 'Layout/ServerLayout/UserProfileCard',
  component: Inner,
  parameters: {
    layout: 'fullscreen'
  },
  tags: ['autodocs'],
  decorators: [
    (Story) => (
      <MemoryRouter>
        <Story />
      </MemoryRouter>
    )
  ]
}

export default meta
type Story = StoryObj<typeof Inner>

export const PrimaryUserProfileCard: Story = {
  args: {
    user,
    onSendFriendRequest: () => console.log('친구 요청'),
    onMoreButtonClick: () => console.log('더보기 클릭')
  }
}
