import { sentryVitePlugin } from "@sentry/vite-plugin";
import { reactRouter } from '@react-router/dev/vite'
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
  plugins: [reactRouterDevTools(), reactRouter(), tsconfigPaths(), sentryVitePlugin({
    org: "bbebig-ck",
    project: "bissgcode"
  })],
  build: {
    cssMinify: true,
    ssr: true,
    sourcemap: true
  }
})