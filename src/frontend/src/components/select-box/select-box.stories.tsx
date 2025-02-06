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
    },
    forward: {
      control: 'select',
      options: ['top', 'bottom']
    },
    mark: {
      control: 'boolean'
    }
  }
} satisfies Meta<typeof SelectBox>

export default meta
type Story = StoryObj<typeof meta>

const options: { label: string; value: string }[] = [
  { label: 'option1', value: 'option1' },
  { label: 'option2', value: 'option2' },
  { label: 'option3', value: 'option3' }
]

export const PrimarySelectBox: Story = {
  args: {
    label: '옵션 선택',
    options: options as { label: string; value: string }[],
    value: null,
    forward: 'top',
    onChange: fn()
  },
  render: (args) => {
    // eslint-disable-next-line react-hooks/rules-of-hooks
    const [value, setValue] = useState<string | null>(null)

    const newArgs = { ...args, value, onChange: setValue }
    return (
      <div className='w-full h-[500px] flex items-center'>
        <div className='w-[480px]'>
          {/** @ts-ignore */}
          <SelectBox {...newArgs} />
        </div>
      </div>
    )
  }
}
