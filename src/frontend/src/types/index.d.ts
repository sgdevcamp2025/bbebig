declare module '*.webp' {
  const src: string
  export default src
}

declare global {
  interface ImportMeta {
    readonly PRIVATE_ROUTE_ENABLE: string
  }
}
