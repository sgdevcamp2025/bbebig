import { useCallback, useEffect, useMemo, useState } from 'react'
import { useShallow } from 'zustand/shallow'

import CustomButton from '@/components/custom-button'
import SelectBox from '@/components/select-box'
import VolumeSlider from '@/components/volume-slider'
import useGetMediaDevices from '@/hooks/use-get-media-devices'
import useMediaControl from '@/hooks/use-media-control'
import useMediaSettingsStore from '@/stores/use-media-setting.store'
import { AudioDevice } from '@/types/media'

interface VoiceSetting {
  VOICE_OPTION: string
  OUTPUT_OPTION: string
  VOICE_PERCENTAGE: number
  OUTPUT_PERCENTAGE: number
}

function VoiceSetting() {
  const [isShowVideoTest, setIsShowVideoTest] = useState(false)
  const [isShowAudioTest, setIsShowAudioTest] = useState(false)

  const { devices: UserDevices } = useGetMediaDevices()

  const {
    devices,
    volume,
    setOutputVolume,
    setInputVolume,
    setInputDevice,
    setOutputDevice,
    setVideoDevice
  } = useMediaSettingsStore(
    useShallow((state) => ({
      devices: state.devices,
      volume: state.volume,
      setOutputVolume: state.setOutputVolume,
      setInputVolume: state.setInputVolume,
      setInputDevice: state.setInputDevice,
      setOutputDevice: state.setOutputDevice,
      setVideoDevice: state.setVideoDevice
    }))
  )

  const selectOptions = useMemo(
    () => [
      {
        label: '녹음 장치',
        options: UserDevices.audioDevices,
        value: UserDevices.audioDevices.find((device) => device.value === devices.audioInput),
        onChange: (value: AudioDevice) => setInputDevice(value.value),
        perceantageLabel: '입력 음량',
        perceantageValue: volume.input,
        onChangePerceantage: setInputVolume
      },
      {
        label: '출력 장치',
        options: UserDevices.outputDevices,
        value: UserDevices.outputDevices.find((device) => device.value === devices.audioOutput),
        onChange: (value: AudioDevice) => setOutputDevice(value.value),
        perceantageLabel: '출력 음량',
        perceantageValue: volume.output,
        onChangePerceantage: setOutputVolume
      }
    ],
    [
      UserDevices.audioDevices,
      UserDevices.outputDevices,
      devices.audioInput,
      devices.audioOutput,
      setInputDevice,
      setInputVolume,
      setOutputDevice,
      setOutputVolume,
      volume.input,
      volume.output
    ]
  )

  const { videoRef, startStream, stopStream } = useMediaControl()

  const handleVideoTest = useCallback(() => {
    setIsShowVideoTest((prev) => {
      const newState = !prev
      if (newState) {
        startStream()
      } else {
        stopStream()
      }
      return newState
    })
  }, [startStream, stopStream])

  const handleAudioTest = useCallback(() => {
    setIsShowAudioTest((prev) => !prev)
  }, [])

  useEffect(() => {
    return () => {
      stopStream()
    }
  }, [stopStream])

  return (
    <section className='pt-[60px] px-[40px] pb-20 flex flex-col gap-4'>
      <h2 className='text-white-100 text-[24px] leading-[30px] font-bold'>음성 설정</h2>
      <div className='flex flex-col gap-4'>
        <div className='grid grid-cols-2 gap-4'>
          {selectOptions.map((option) => {
            const currentValue =
              option.options.find((item) => item.value === option.value?.value) ||
              UserDevices.audioDevices[0]
            return (
              <div key={option.label}>
                <div className='mb-5'>
                  <h4 className='text-white-20 leading-[130%] text-[12px] mb-2 font-bold'>
                    {option.label}
                  </h4>
                  <SelectBox
                    key={option.label}
                    options={option.options}
                    value={currentValue}
                    onChange={(e) => {
                      option.onChange(e)
                    }}
                    forward='bottom'
                  />
                </div>
                <div className='mb-5'>
                  <h4 className='text-white-20 leading-[130%] text-[12px] mb-2 font-bold'>
                    {option.perceantageLabel}
                  </h4>
                  <div className='py-2'>
                    <VolumeSlider
                      label={option.perceantageLabel}
                      value={option.perceantageValue}
                      onChange={(e) => option.onChangePerceantage(Number(e.target.value))}
                    />
                  </div>
                </div>
              </div>
            )
          })}
        </div>
        <h4 className='text-white-20 leading-[130%] text-[12px] mb-2 font-bold'>마이크 테스트</h4>
        <div className='flex justify-between gap-4'>
          <CustomButton
            size='small'
            onClick={handleAudioTest}>
            {isShowAudioTest ? '테스트 정지하기' : '마이크 테스트'}
          </CustomButton>
          <div className='flex items-center gap-2'>
            {Array.from({ length: 42 }).map((_, index) => (
              <div
                key={index}
                className='w-1 h-6 bg-white-20 rounded-full'></div>
            ))}
          </div>
        </div>
        <section className='flex flex-col gap-4'>
          <div className='h-[1px] bg-gray-80' />
          <h2 className='text-white-100 text-[24px] leading-[30px] font-bold'>영상 설정</h2>
          <SelectBox
            key='video-device'
            options={UserDevices.videoDevices}
            value={
              UserDevices.videoDevices.find((device) => device.value === devices.video) ||
              UserDevices.videoDevices[0]
            }
            onChange={(e) => {
              setVideoDevice(e.value)
            }}
            forward='bottom'
          />
          <div className='flex justify-between gap-4 bg-gray-20 border-[1px] min-h-[220px] w-full rounded-lg border-black'>
            {!isShowVideoTest ? (
              <div className='flex flex-col items-center justify-center w-full gap-6'>
                <img
                  src='/image/setting/camera.svg'
                  alt='video-test'
                  className='w-[166px] h-[101px]'
                />
                <CustomButton
                  type='button'
                  onClick={handleVideoTest}
                  size='small'
                  className='py-[2] px-4 text-[14px] w-fit'>
                  영상 테스트
                </CustomButton>
              </div>
            ) : (
              <video
                ref={videoRef}
                className='w-full h-full object-cover'
                autoPlay
                playsInline
                muted
              />
            )}
          </div>
          <span className='text-white-20 leading-[130%] text-[14px] mb-2'>
            미리 보기를 활성화하려면 Discord의{' '}
            <a
              href='#'
              className='text-text-link'>
              카메라 엑세스
            </a>
            를 허용해야 합니다.
          </span>
        </section>
        {isShowVideoTest && (
          <CustomButton
            type='button'
            onClick={handleVideoTest}
            size='small'
            className='py-[2] px-4 text-[14px] w-fit'>
            영상 테스트 중지
          </CustomButton>
        )}
      </div>
    </section>
  )
}

export default VoiceSetting
