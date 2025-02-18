import type { Meta, StoryObj } from '@storybook/react'

import StatusIcon from '.'

const meta = {
  title: 'Component/StatusIcon',
  component: StatusIcon,
  parameters: {
    layout: 'centered'
  },
  tags: ['autodocs'],
  argTypes: {
    status: {
      control: 'select',
      options: ['ONLINE', 'OFFLINE', 'DND', 'INVISIBLE', 'AWAY']
    },
    size: {
      control: 'select',
      options: ['sm', 'lg']
    },
    defaultBackgroundColor: {
      control: 'color'
    },
    hover: {
      control: 'boolean'
    },
    hoverBackgroundColor: {
      control: 'color'
    }
  }
} satisfies Meta<typeof StatusIcon>

export default meta
type Story = StoryObj<typeof meta>

export const PrimaryStatusIcon: Story = {
  args: {
    status: 'ONLINE',
    size: 'lg',
    hover: false,
    hoverBackgroundColor: '#5865f2',
    defaultBackgroundColor: 'white'
  },
  render: (args) => {
    return (
      <div className='h-20 w-20 bg-black flex items-center justify-center'>
        <StatusIcon {...args} />
      </div>
    )
  }
}
