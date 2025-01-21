import type { Config } from 'tailwindcss'

const config: Pick<Config, 'presets' | 'content' | 'theme'> = {
  content: ['./src/**/*.tsx'],
  theme: {
    extend: {
      colors: {
        brand: '#5865f2',
        'brand-gradient': 'linear-gradient(90deg, #8ea1e1 0%, #7289da 100%)',
        greyple: '#99aab5',
        'dark-not-black': '#2c2f33',
        'focus-border': '#00b0f4',
        'status-green': '#43b581',
        'text-link': '#00b0f4',
        'off-white': '#f6f6f6',
        white: '#fff',
        'black-10': '#000',
        'black-20': '#060a0b',
        'black-30': '#23272a',
        'red-10': '#de2761',
        'blue-10': '#1a2081',
        'blue-20': '#00002a',
        'blue-30': '#404eed'
      },
      backgroundImage: {
        'landing-background': "url('./src/assets/image/homepage/background-art.png')",
        'landing-hero-background': "url('./src/assets/image/homepage/hero-character.webp')",
        'landing-shine-1': "url('./src/assets/image/homepage/background-shine-1.webp')",
        'landing-shine-2': "url('./src/assets/image/homepage/background-shine-2.webp')",
        'video-banner-background': 'linear-gradient(135deg,#fff6,#ffffff12)',
        'bottom-border-background':
          'linear-gradient(135deg, #ffffff4d, #ffffff0d), linear-gradient(261deg, #2e1a8e, #0e0e5c)'
      },
      boxShadow: {
        'video-banner-box-shadow': '0 2px 52px #452a7c1a, inset 0 0 0 3px #fff3'
      },
      screens: {
        mobile: {
          max: '479px'
        },
        'mobile-tablet': {
          min: '480px'
        },
        tablet: {
          min: '768px'
        },
        'tablet-labtop': {
          min: '1024px'
        },
        'desktop-small': {
          min: '1280px'
        },
        'desktop-large': {
          min: '1440px'
        },
        'desktop-xlarge': {
          min: '1920px'
        },
        'mobile-range': {
          min: '0px',
          max: '767px'
        },
        'tablet-range': {
          min: '768px',
          max: '1279px'
        },
        'desktop-range': {
          min: '1280px'
        }
      }
    }
  }
}

export default config
