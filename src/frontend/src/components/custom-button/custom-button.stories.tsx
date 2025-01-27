import type { Meta, StoryObj } from '@storybook/react'

import CustomButton from '.'

const meta = {
  title: 'Component/CustomButton',
  component: CustomButton,
  parameters: {
    layout: 'centered'
  },
  tags: ['autodocs'],
  argTypes: {
    variant: {
      control: 'select',
      options: ['primary', 'secondary']
    },
    size: {
      control: 'select',
      options: ['small', 'medium', 'large']
    },
    width: {
      control: 'select',
      options: ['full', 'half']
    }
  }
} satisfies Meta<typeof CustomButton>

export default meta
type Story = StoryObj<typeof meta>

export const PrimaryButton: Story = {
  args: {
    variant: 'primary',
    children: 'click me!',
    size: 'small',
    width: 'full'
  },
  render: (args) => (
    <div className='w-[480px]'>
      <CustomButton {...args} />
    </div>
  )
}
