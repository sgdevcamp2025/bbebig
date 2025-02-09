type VideoConfig = {
  videoDeviceId?: string
  videoQuality?: 'SD' | 'HD' | 'FHD'
}

type AudioConfig = {
  audioInputDeviceId?: string
  audioOutputDeviceId?: string
}

type Config = VideoConfig & AudioConfig

class MediaController {
  // 비디오 설정
  private _videoElement: HTMLVideoElement | null = null

  // 음성 설정
  private audioContext: AudioContext
  private inputGainNode: GainNode
  private outputGainNode: GainNode

  // 미디어 스트림
  private streams: {
    video: MediaStream | null
    audioInput: MediaStream | null
    audioOutput: MediaStream | null
  }

  constructor() {
    this.audioContext = new AudioContext()
    this.inputGainNode = this.audioContext.createGain()
    this.outputGainNode = this.audioContext.createGain()
    this.outputGainNode.connect(this.audioContext.destination)

    this.streams = {
      video: null,
      audioInput: null,
      audioOutput: null
    }
  }

  async initialize(config: Config) {
    try {
      const { videoDeviceId, audioInputDeviceId, audioOutputDeviceId, videoQuality } = config

      if (videoDeviceId) {
        await this.initializeVideo(videoDeviceId, videoQuality)
      }
      if (audioInputDeviceId) {
        await this.initializeAudioInput(audioInputDeviceId)
      }
      if (audioOutputDeviceId) {
        await this.initializeAudioOutput(audioOutputDeviceId)
      }
    } catch (error) {
      console.error('Failed to initialize media:', error)
      throw error
    }
  }

  // 초기 비디오 설정시 사용
  private async initializeVideo(deviceId: string, quality?: 'SD' | 'HD' | 'FHD') {
    const constraints = {
      width: quality === 'FHD' ? 1920 : quality === 'HD' ? 1280 : 640,
      height: quality === 'FHD' ? 1080 : quality === 'HD' ? 720 : 480,
      deviceId: deviceId ? deviceId : undefined
    }

    try {
      this.streams.video = await navigator.mediaDevices.getUserMedia({
        video: constraints
      })
    } catch (error) {
      console.error('Failed to initialize video:', error)
      throw error
    }
  }

  // 초기 음성 설정시 사용
  private async initializeAudioInput(deviceId?: string) {
    try {
      const stream = await navigator.mediaDevices.getUserMedia({
        audio: deviceId ? { deviceId: { exact: deviceId } } : true
      })

      this.streams.audioInput = stream
      const source = this.audioContext.createMediaStreamSource(stream)
      source.connect(this.inputGainNode)

      return stream
    } catch (error) {
      console.error('Failed to initialize input device:', error)
      this.defaultAudioInputSetting()
    }
  }

  // 초기 음성 설정시 못찾을 경우 사용
  private async defaultAudioInputSetting() {
    try {
      const stream = await navigator.mediaDevices.getUserMedia({
        audio: true
      })
      this.streams.audioInput = stream
      const source = this.audioContext.createMediaStreamSource(stream)
      source.connect(this.inputGainNode)

      return stream
    } catch (error) {
      console.error('Failed to default audio input device:', error)
      throw error
    }
  }

  // 초기 출력 설정시 사용
  async initializeAudioOutput(deviceId?: string) {
    try {
      // @ts-ignore - setSinkId는 아직 실험적 기능입니다
      if (this.audioContext.destination.setSinkId) {
        // @ts-ignore
        await this.audioContext.destination.setSinkId(deviceId || 'default')
      }
    } catch (error) {
      console.error('Failed to initialize output device:', error)
      throw error
    }
  }

  getStreams() {
    return this.streams
  }

  bindVideoElement(element: HTMLVideoElement) {
    this._videoElement = element
    this._videoElement.srcObject = this.streams.video
    this._videoElement.autoplay = true
  }

  // 요소 업데이트 메서드 추가
  async refreshVideoElement() {
    if (!this._videoElement) return

    // 강제 재생 트리거
    try {
      await this._videoElement.play()
    } catch (error) {
      console.error('비디오 재생 실패:', error)
    }
  }

  setInputVolume(value: number) {
    this.inputGainNode.gain.value = value / 100
  }

  setOutputVolume(value: number) {
    this.outputGainNode.gain.value = value / 100
  }

  cleanup() {
    Object.values(this.streams).forEach((stream) => {
      stream?.getTracks().forEach((track) => track.stop())
    })
  }
}

export default MediaController
