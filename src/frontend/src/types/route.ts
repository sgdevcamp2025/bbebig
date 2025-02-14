export interface ErrorBoundaryProps {
  error: unknown
}

export type LinksFunction = () => {
  rel: string
  href: string
  crossOrigin?: string
}[]
