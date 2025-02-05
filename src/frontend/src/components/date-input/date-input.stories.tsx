import type { Meta, StoryObj } from '@storybook/react'

import DateInput from '.'

const meta = {
  title: 'Component/DateInput',
  component: DateInput,
  parameters: {
    layout: ''
  },
  tags: ['autodocs'],
  argTypes: {
    label: {
      control: 'text'
    },
    required: {
      control: 'boolean'
    }
  }
} satisfies Meta<typeof DateInput>

export default meta
type Story = StoryObj<typeof meta>

export const PrimaryDateInput: Story = {
  args: {
    label: '날짜 선택',
    required: true
  },
  render: (args) => (
    <div className='w-full h-[500px] flex justify-center items-center'>
      <div className='w-[480px]'>
        <DateInput {...args} />
      </div>
    </div>
  )
}
