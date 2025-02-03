import type { Meta, StoryObj } from '@storybook/react'

import LoadingIcon from '.'

const meta = {
  title: 'Component/LoadingIcon',
  component: LoadingIcon,
  parameters: {
    layout: 'centered'
  }
} satisfies Meta<typeof LoadingIcon>

export default meta
type Story = StoryObj<typeof meta>

export const PrimaryLoadingIcon: Story = {
  args: {}
}
