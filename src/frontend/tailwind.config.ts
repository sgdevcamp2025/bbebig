import type { Config } from 'tailwindcss'
import tailwindCSSMotion from 'tailwindcss-motion'

const config: Pick<Config, 'presets' | 'content' | 'theme' | 'plugins'> = {
  content: ['./app/**/*.tsx', './src/**/*.tsx'],
  theme: {
    extend: {
      colors: {
        brand: '#5865f2',
        'brand-10': 'hsl(222.857 calc(1 * 6.667%) 20.588% / 1)',
        'brand-gradient': 'linear-gradient(90deg, #8ea1e1 0%, #7289da 100%)',
        greyple: '#99aab5',
        'dark-not-black': '#2c2f33',
        'focus-border': '#00b0f4',
        'status-green': '#43b581',
        'text-link': '#00b0f4',
        'text-normal': '#hsl(210 calc(1 * 9.091%) 87.059% / 1)',
        'off-white': '#f6f6f6',
        'white-10': 'hsl(220 calc(1 * 13.043%) 95.49% / 1)',
        'white-20': 'hsl(215 calc(1 * 8.824%) 73.333% / 1)',
        'white-100': '#fff',
        'black-10': '#000',
        'black-20': '#060a0b',
        'black-30': '#23272a',
        'black-40': '#565863',
        'black-50': '#484953',
        'black-60': '#384043',
        'black-70': '#252628',
        'black-80': 'hsl(225 calc(1 * 6.25%) 12.549% / 1)',
        'black-90': '#1a1c1e',
        'red-10': '#de2761',
        'blue-10': '#1a2081',
        'blue-20': '#00002a',
        'blue-30': '#404eed',
        'gray-10': 'hsl(215 calc(1 * 8.824%) 73.333% / 1)',
        'gray-20': 'hsl(220 calc(1 * 6.522%) 18.039% / 1)',
        'gray-30': '#37383d',
        'gray-40': '#2a2d31',
        'gray-50': '#b4b8bf',
        'gray-60': 'hsl(213.75 calc(1 * 8.081%) 61.176% / 1)',
        'gray-70': '#404248',
        'gray-80': 'hsl(228 calc(1 * 6.024%) 32.549% / 0.48)',

        'discord-gray': {
          50: '#ECEDEE',
          100: '#DCDDDE',
          200: '#B9BBBE',
          300: '#8E9297',
          400: '#72767D',
          500: '#4F545C',
          600: '#36393F',
          700: '#2F3136',
          800: '#202225',
          900: '#18191C'
        },
        'discord-status': {
          online: '#43B581',
          idle: '#FAA61A',
          dnd: '#F04747',
          offline: '#747F8D'
        },
        'discord-brand': {
          DEFAULT: '#5865F2',
          hover: '#4752C4',
          active: '#3C45A5'
        },
        'discord-font-color': {
          normal: '#dcddde',
          muted: '#72767d',
          link: '#00b0f4'
        }
      },
      backgroundImage: {
        'auth-background': "url('/image/auth/background.svg')",
        'landing-background': "url('/image/homepage/background-art.png')",
        'landing-hero-background': "url('/image/homepage/hero-character.webp')",
        'landing-shine-1': "url('/image/homepage/background-shine-1.webp')",
        'landing-shine-2': "url('/image/homepage/background-shine-2.webp')",
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
  },
  plugins: [tailwindCSSMotion]
}

export default config
