declare module '*.webp' {
  const src: string

  export default src
}

declare global {
  interface ImportMeta {
    readonly VITE_SERVER_URL: string
    readonly VITE_SIGNALING_SERVER_URL: string
    readonly VITE_CHAT_SERVER_URL: string
    readonly VITE_SENTRY_DSN: string
    readonly VITE_SENTRY_AUTH_TOKEN: string
  }
}
