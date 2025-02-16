import { reactRouter } from '@react-router/dev/vite'
import { sentryVitePlugin } from '@sentry/vite-plugin'
import autoprefixer from 'autoprefixer'
import { reactRouterDevTools } from 'react-router-devtools'
import tailwindcss from 'tailwindcss'
import { defineConfig } from 'vite'
import tsconfigPaths from 'vite-tsconfig-paths'
// https://vite.dev/config/
export default defineConfig({
  css: {
    postcss: {
      plugins: [autoprefixer, tailwindcss]
    }
  },
  plugins: [
    reactRouterDevTools(),
    reactRouter(),
    tsconfigPaths(),
    sentryVitePlugin({
      org: 'bbebig-ck',
      project: 'bissgcode',
      sourcemaps: {
        assets: './build/**',
        ignore: ['node_modules', 'dist', 'build'],
        filesToDeleteAfterUpload: './build/**'
      }
    })
  ],
  build: {
    cssMinify: true,
    ssr: true,
    sourcemap: true
  }
})
