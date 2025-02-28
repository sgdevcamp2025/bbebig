import type { Meta, StoryObj } from '@storybook/react'

import DmSideBar from '.'

const meta = {
  title: 'Layout/DmLayout/DmSideBar',
  component: DmSideBar,
  parameters: {
    layout: 'fullscreen'
  },
  tags: ['autodocs'],
  argTypes: {
    serverName: { control: 'text' }
  }
} satisfies Meta<typeof DmSideBar>

export default meta
type Story = StoryObj<typeof meta>

export const DmSidebar: Story = {
  args: { serverName: '서버 이름' }
}
