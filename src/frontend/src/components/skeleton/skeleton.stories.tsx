import type { Meta, StoryObj } from '@storybook/react'

import { Skeleton } from '.'

const meta = {
  title: 'Component/Skeleton',
  component: Skeleton,
  parameters: {
    layout: 'centered'
  },
  tags: ['autodocs'],
  argTypes: {
    variant: { control: 'select' }
  }
} satisfies Meta<typeof Skeleton>

export default meta
type Story = StoryObj<typeof meta>

export const Primary: Story = {
  args: {
    variant: 'rounded'
  },
  render: (args) => {
    return (
      <div className='w-[480px]'>
        <Skeleton
          className='w-full h-10 animate-pulse bg-discord-gray-700/10'
          {...args}
        />
      </div>
    )
  }
}
