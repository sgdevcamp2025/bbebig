import { reactRouter } from '@react-router/dev/vite'
import autoprefixer from 'autoprefixer'
import tailwindcss from 'tailwindcss'
import { defineConfig } from 'vite'
import tsconfigPaths from 'vite-tsconfig-paths'
import { reactRouterDevTools } from 'react-router-devtools'
// https://vite.dev/config/
export default defineConfig({
  css: {
    postcss: {
      plugins: [autoprefixer, tailwindcss]
    }
  },
  plugins: [reactRouterDevTools(), reactRouter(), tsconfigPaths()],
  build: {
    cssMinify: true,
    ssr: false
  }
})
