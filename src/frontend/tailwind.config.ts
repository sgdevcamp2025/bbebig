import type { Config } from 'tailwindcss'

const config: Pick<Config, 'presets' | 'content'> = {
  content: ['./src/**/*.tsx']
}

export default config
