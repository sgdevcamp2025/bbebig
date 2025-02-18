import type { Meta, StoryObj } from '@storybook/react'
import { fn } from '@storybook/test'
import { useState } from 'react'

import CustomRadio from '.'

const meta = {
  title: 'Component/CustomRadio',
  component: CustomRadio,
  parameters: {
    layout: 'centered'
  },
  tags: ['autodocs']
} satisfies Meta<typeof CustomRadio>

export default meta
type Story = StoryObj<typeof meta>

export const Primary: Story = {
  args: {
    label: '채널 유형',
    items: [
      {
        id: '1',
        label: '텍스트',
        description: '메시지, 이미지, GIF, 이모지, 의견, 농담을 전송하세요',
        value: 'TEXT',
        icon: (
          <img
            src='/icon/channel/type-chat.svg'
            alt='텍스트'
            width={20}
            height={20}
          />
        )
      },
      {
        id: '2',
        label: '음성',
        description: '음성, 영상, 화면 공유로 함꼐 어울리세요',
        value: 'VOICE',
        icon: (
          <img
            src='/icon/channel/type-voice.svg'
            alt='음성'
            width={20}
            height={20}
          />
        )
      }
    ],
    selectedItem: { id: '1', label: '텍스트', value: 'TEXT' },
    onChange: () => fn()
  },
  render: (args) => {
    // eslint-disable-next-line react-hooks/rules-of-hooks
    const [selectedItem, setSelectedItem] = useState(args.selectedItem)
    return (
      <div className='w-[480px] h-[300px] bg-brand-10 p-4'>
        <CustomRadio
          {...args}
          selectedItem={selectedItem}
          onChange={setSelectedItem}
        />
      </div>
    )
  }
}
