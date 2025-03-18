import { sentryVitePlugin } from '@sentry/vite-plugin'
import react from '@vitejs/plugin-react'
import autoprefixer from 'autoprefixer'
import { resolve } from 'path'
import tailwindcss from 'tailwindcss'
import { defineConfig, loadEnv } from 'vite'
import tsconfigPaths from 'vite-tsconfig-paths'

export default defineConfig(({ mode }) => {
  const env = loadEnv(mode, process.cwd(), '')
  return {
    base: '/',
    css: {
      postcss: {
        plugins: [autoprefixer, tailwindcss]
      }
    },
    server: {
      allowedHosts: ['specially-mighty-titmouse.ngrok-free.app']
    },
    plugins: [
      react(),
      tsconfigPaths(),
      sentryVitePlugin({
        org: 'bbebig',
        project: 'bbebig',
        telemetry: false,
        authToken: env.VITE_SENTRY_AUTH_TOKEN,
        sourcemaps: {
          assets: './build/client/**',
          ignore: ['node_modules', 'dist', 'build'],
          filesToDeleteAfterUpload: './build/client/**/*.map'
        }
      })
    ],
    optimizeDeps: {
      include: ['@mswjs/socket.io-binding']
    },
    build: {
      outDir: 'build/client',
      emptyOutDir: true,
      cssMinify: true,
      ssr: false,
      spa: true,
      sourcemap: true,
      chunkSizeWarningLimit: 500,
      rollupOptions: {
        input: {
          main: resolve(__dirname, 'index.html')
        },
        output: {
          dir: 'build/client',
          manualChunks: {
            'vendor-react': ['react', 'react-dom', 'react-router-dom'],
            layout: [
              './src/layouts/root-layout',
              './src/layouts/auth',
              './src/layouts/main-layout',
              './src/layouts/server-layout',
              './src/layouts/dm-layout'
            ],
            pages: ['./src/pages/landing', './src/pages/auth/login', './src/pages/auth/register'],
            'channel-pages': ['./src/pages/channel', './src/pages/friend', './src/pages/dm']
          }
        }
      },
      resolve: {
        alias: {
          '@': resolve(__dirname, './src')
        }
      }
    }
  }
})
