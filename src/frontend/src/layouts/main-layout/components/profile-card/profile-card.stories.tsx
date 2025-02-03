import type { Meta, StoryObj } from '@storybook/react'

import { Content } from '.'

const meta = {
  title: 'Layout/MainLayout/ProfileCard',
  component: Content,
  parameters: {
    layout: 'centered'
  }
} satisfies Meta<typeof Content>

export default meta
type Story = StoryObj<typeof meta>

export const PrimaryProfileCard: Story = {
  args: {
    name: 'John Doe',
    email: 'john.doe@example.com',
    status: 'ONLINE',
    avatarUrl: '/image/common/default-avatar.png',
    backgroundUrl:
      'https://cdn.discordapp.com/banners/419411480677580820/a_b6a40ef738c6f8221793be39094cce00.png'
  }
}
