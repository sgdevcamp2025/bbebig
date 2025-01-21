import react from '@vitejs/plugin-react-swc'
import autoprefixer from 'autoprefixer'
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
    react({
      jsxImportSource: 'react'
    }),
    tsconfigPaths()
  ]
})
