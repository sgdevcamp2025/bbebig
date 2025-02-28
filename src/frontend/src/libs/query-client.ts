import { QueryClient } from '@tanstack/react-query'

const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      // 데이터 유효 시간
      staleTime: 1000 * 60,
      // 데이터 캐시 유효 시간
      gcTime: 1000 * 60 * 5,
      // 재시도 횟수
      retry: 3,
      // 윈도우 포커스 시 데이터 갱신 여부
      refetchOnWindowFocus: false,
      // 마운트 시 데이터 갱신 여부
      refetchOnMount: false,
      // 재연결 시 데이터 갱신 여부
      refetchOnReconnect: false
    }
  }
})

export default queryClient
