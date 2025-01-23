import type { Meta, StoryObj } from '@storybook/react'
import { fn } from '@storybook/test'
import { useState } from 'react'

import CheckBox from '.'

const meta = {
  title: 'Component/CheckBox',
  component: CheckBox,
  parameters: {
    layout: 'centered'
  },
  tags: ['autodocs'],
  argTypes: {
    isChecked: {
      control: 'boolean'
    },
    label: {
      control: 'text'
    },
    onChange: {
      action: 'onChange'
    }
  }
} satisfies Meta<typeof CheckBox>

export default meta
type Story = StoryObj<typeof meta>

export const PrimaryCheckBox: Story = {
  args: {
    isChecked: false,
    label: '체크박스',
    onChange: fn()
  },
  render: (args) => {
    // eslint-disable-next-line react-hooks/rules-of-hooks
    const [isChecked, setIsChecked] = useState(args.isChecked)
    const handleChange = () => {
      setIsChecked(!isChecked)
    }
    return (
      <div className='w-[480px]'>
        <CheckBox
          {...args}
          isChecked={isChecked}
          onChange={handleChange}
        />
      </div>
    )
  }
}
