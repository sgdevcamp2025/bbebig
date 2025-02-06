import SelectBox from '@/components/select-box'
import { useState } from 'react'
import { LOCAL_STORAGE } from '@/constants/keys'
import Slider from '@/components/\bslider'
import CustomButton from '@/components/custom-button'

type VoiceSetting = {
  VOICE_OPTION: string
  OUTPUT_OPTION: string
  VOICE_PERCENTAGE: number
  OUTPUT_PERCENTAGE: number
}

function VoiceSetting() {
  const [voiceSetting, setVoiceSetting] = useState<VoiceSetting>({
    VOICE_OPTION: localStorage?.getItem(LOCAL_STORAGE.VOICE_OPTION) || 'Default',
    OUTPUT_OPTION: localStorage?.getItem(LOCAL_STORAGE.OUTPUT_OPTION) || 'Default',
    VOICE_PERCENTAGE: Number(localStorage?.getItem(LOCAL_STORAGE.VOICE_PERCENTAGE)) || 100,
    OUTPUT_PERCENTAGE: Number(localStorage?.getItem(LOCAL_STORAGE.OUTPUT_PERCENTAGE)) || 100
  })

  const [isTest, setIsTest] = useState(false)

  const handleChangeVoiceSetting = (option: keyof VoiceSetting, value: string | number) => {
    setVoiceSetting((prev) => ({ ...prev, [option]: value }))
    localStorage.setItem(LOCAL_STORAGE[option], value.toString())
  }

  const handleChangeVoiceOption = (value: string) => {
    handleChangeVoiceSetting('VOICE_OPTION', value)
  }

  const handleChangeOutputOption = (value: string) => {
    handleChangeVoiceSetting('OUTPUT_OPTION', value)
  }

  const handleChangeVoicePercentage = (value: number) => {
    handleChangeVoiceSetting('VOICE_PERCENTAGE', value)
  }

  const handleChangeOutputPercentage = (value: number) => {
    handleChangeVoiceSetting('OUTPUT_PERCENTAGE', value)
  }

  const selectOptions = [
    {
      label: '녹음 장치',
      options: ['Default', 'option2', 'option3'],
      value: voiceSetting.VOICE_OPTION,
      onChange: handleChangeVoiceOption,
      perceantageLabel: '입력 음량',
      perceantageValue: voiceSetting.VOICE_PERCENTAGE,
      onChangePerceantage: handleChangeVoicePercentage
    },
    {
      label: '출력 장치',
      options: ['Default', 'option2', 'option3'],
      value: voiceSetting.OUTPUT_OPTION,
      onChange: handleChangeOutputOption,
      perceantageLabel: '출력 음량',
      perceantageValue: voiceSetting.OUTPUT_PERCENTAGE,
      onChangePerceantage: handleChangeOutputPercentage
    }
  ]

  return (
    <section className='pt-[60px] px-[40px] pb-20 flex flex-col gap-4'>
      <h2 className='text-white-100 text-[24px] leading-[30px] font-bold'>음성 설정</h2>
      <div className='flex flex-col gap-4'>
        <div className='grid grid-cols-2 gap-4'>
          {selectOptions.map((option) => (
            <div>
              <div className='mb-5'>
                <h4 className='text-white-100 text-white-20 leading-[130%] text-[12px] mb-2 font-bold'>
                  {option.label}
                </h4>
                <SelectBox
                  key={option.label}
                  options={option.options}
                  value={option.value}
                  onChange={option.onChange}
                  forward='bottom'
                />
              </div>
              <div className='mb-5'>
                <h4 className='text-white-100 text-white-20 leading-[130%] text-[12px] mb-2 font-bold'>
                  {option.perceantageLabel}
                </h4>
                <div className='py-2'>
                  <Slider
                    value={option.perceantageValue}
                    onChange={(e) => option.onChangePerceantage(Number(e.target.value))}
                  />
                </div>
              </div>
            </div>
          ))}
        </div>
        <h4 className='text-white-100 text-white-20 leading-[130%] text-[12px] mb-2 font-bold'>
          마이크 테스트
        </h4>
        <div className='flex justify-between gap-4'>
          <CustomButton
            size='small'
            onClick={() => setIsTest(!isTest)}>
            {isTest ? '테스트 정지하기' : '마이크 테스트'}
          </CustomButton>
          <div className='flex items-center gap-2'>
            {Array.from({ length: 40 }).map((_, index) => (
              <div
                key={index}
                className='w-1 h-6 bg-white-20 rounded-full'></div>
            ))}
          </div>
        </div>
        <section className='flex flex-col gap-4'>
          <div className='h-[1px] bg-gray-80' />
          <h2 className='text-white-100 text-[24px] leading-[30px] font-bold'>영상 설정</h2>
          <div className='flex justify-between gap-4 bg-gray-20 border-[1px] h-[220px] w-full rounded-lg border-black'>
            <div className='flex flex-col items-center justify-center w-full gap-6'>
              <img
                src='/image/setting/camera.svg'
                alt='video-test'
                className='w-[166px] h-[101px]'
              />
              <CustomButton
                size='small'
                className='py-[2] px-4 text-[14px] w-fit'>
                영상 테스트
              </CustomButton>
            </div>
          </div>
          <span className='text-white-100 text-white-20 leading-[130%] text-[14px] mb-2'>
            미리 보기를 활성화하려면 Discord의
            <a
              href='#'
              className='text-text-link'>
              카메라 엑세스
            </a>
            를 허용해야 합니다.
          </span>
        </section>
      </div>
    </section>
  )
}

export default VoiceSetting
