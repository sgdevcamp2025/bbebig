import { MemoryRouter } from 'react-router-dom'

import { User } from '@/types/user'
import type { Meta, StoryObj } from '@storybook/react'
import UserProfileCard from '.'

const user: User = {
  id: '1',
  name: '김예지',
  avatarUrl: '/image/common/default-avatar.png',
  bannerUrl:
    'https://cdn.discordapp.com/banners/419411480677580820/a_b6a40ef738c6f8221793be39094cce00.png',
  customPresenceStatus: 'ONLINE',
  introduction: '안뇽',
  introductionEmoji: '👋',
  email: 'yeji@gmail.com'
}

const meta: Meta<typeof UserProfileCard> = {
  title: 'Layout/ServerLayout/UserProfileCard',
  component: UserProfileCard,
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
type Story = StoryObj<typeof UserProfileCard>

export const Default: Story = {
  args: {
    user: user,
    onSendFriendRequest: () => console.log('친구 요청'),
    onMoreButtonClick: () => console.log('더보기 클릭')
  }
}
