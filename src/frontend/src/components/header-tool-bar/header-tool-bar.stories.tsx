import type { Meta, StoryObj } from '@storybook/react'
import { fn } from '@storybook/test'

import HeaderToolBar from '.'

const meta = {
  title: 'Component/HeaderToolBar',
  component: HeaderToolBar,
  parameters: {
    layout: 'centered'
  },
  tags: ['autodocs'],
  argTypes: {
    type: {
      control: 'select',
      options: ['DEFAULT', 'VOICE', 'DM']
    },
    isStatusBarOpen: {
      control: 'boolean'
    }
  }
} satisfies Meta<typeof HeaderToolBar>

export default meta
type Story = StoryObj<typeof meta>

export const PrimaryHeaderToolBar: Story = {
  args: {
    type: 'DEFAULT',
    isStatusBarOpen: false,
    onToggleStatusBar: () => fn(),
    searchProps: {
      value: '검색',
      onChange: () => fn(),
      handleClear: () => fn(),
      placeholder: '검색'
    }
  }
}

export const VoiceChannelHeaderToolBar: Story = {
  args: {
    type: 'VOICE',
    onClose: () => fn()
  }
}

export const DirectMessageHeaderToolBar: Story = {
  args: {
    type: 'DM',
    searchProps: {
      value: '검색',
      onChange: () => fn(),
      handleClear: () => fn(),
      placeholder: '검색'
    }
  }
}

export const StatusBarOpenHeaderToolBar: Story = {
  args: {
    type: 'DEFAULT',
    isStatusBarOpen: true,
    onToggleStatusBar: () => fn(),
    searchProps: {
      value: '검색',
      onChange: () => fn(),
      handleClear: () => fn(),
      placeholder: '검색'
    }
  }
}
