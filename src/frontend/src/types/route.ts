export namespace Route {
  export type LinksFunction = () => {
    rel: string
    href: string
    crossOrigin?: string
  }[]

  export interface ErrorBoundaryProps {
    error: unknown
  }
}
