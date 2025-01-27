import type { Meta, StoryObj } from '@storybook/react'
import { fn } from '@storybook/test'
import { useState } from 'react'

import SelectBox from '.'

const meta = {
  title: 'Component/SelectBox',
  component: SelectBox,
  parameters: {
    layout: 'centered'
  },
  tags: ['autodocs'],
  argTypes: {
    value: {
      control: 'select',
      options: ['option1', 'option2', 'option3']
    },
    onChange: {
      type: 'function'
    }
  }
} satisfies Meta<typeof SelectBox>

export default meta
type Story = StoryObj<typeof meta>

const options: string[] = ['option1', 'option2', 'option3']

export const PrimarySelectBox: Story = {
  args: {
    label: '옵션 선택',
    options,
    value: null,
    onChange: fn()
  },
  render: (args) => {
    // eslint-disable-next-line react-hooks/rules-of-hooks
    const [value, setValue] = useState<string | null>(null)

    return (
      <div className='w-full h-[500px] flex items-center'>
        <div className='w-[480px]'>
          <SelectBox
            {...args}
            value={value}
            onChange={setValue}
          />
        </div>
      </div>
    )
  }
}
