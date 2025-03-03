const COOKIE_KEYS = {
  ACCESS_TOKEN: 'access_token',
  REFRESH_TOKEN: 'refresh_token'
} as const

const LOCAL_STORAGE = {
  VIDEO: 'video',
  AUDIO_INPUT: 'audio_input',
  AUDIO_OUTPUT: 'audio_output',
  VOLUME_INPUT: 'volume_input',
  VOLUME_OUTPUT: 'volume_output',
  MUTE_VIDEO: 'mute_video',
  MUTE_AUDIO_INPUT: 'mute_audio_input',
  MUTE_AUDIO_OUTPUT: 'mute_audio_output',
  VIDEO_QUALITY: 'video_quality'
} as const

export { COOKIE_KEYS, LOCAL_STORAGE }
