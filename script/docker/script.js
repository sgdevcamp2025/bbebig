import http from 'k6/http';
import { sleep, check } from 'k6';

// 테스트 구성 - 타임아웃 증가
export const options = {
  stages: [
    { duration: '30s', target: 300 },    // 30초 동안 300명까지 증가
    { duration: '30s', target: 750 },     // 1분 동안 750명까지 증가
    { duration: '30s', target: 1500 },    // 1분 동안 1500명까지 증가
    { duration: '1m', target: 2000 },    // 1분 동안 1500명까지 증가
    { duration: '2m', target: 2000 },    // 2분 동안 1500명 유지
    { duration: '30s', target: 0 },      // 30초 동안 0명으로 감소
  ],
  thresholds: {
    http_req_duration: ['p(95)<2000'],   // 95%의 요청이 2초 이하
    http_req_failed: ['rate<0.10'],      // 실패율 10% 미만
  }
};

// 기본 테스트 함수
export default function() {
  // 요청 헤더 설정
  const headers = {
    'Content-Type': 'application/json'
  };
  
  // 요청 옵션 - 타임아웃 설정 추가
  const params = {
    headers: headers,
    timeout: '120s'  // 타임아웃을 120초(2분)로 설정
  };
  
  // GET 요청 URL
  const url = `http://host.docker.internal:8089/test`;
  
  // GET 요청 전송 (늘어난 타임아웃 적용)
  const response = http.get(url, params);
  
  // 응답 확인
  check(response, {
    'HTTP 상태 코드 확인': (r) => r.status === 200 || r.status === 204,
  });
  
  // 적당한 대기 시간 (0.2-0.5초)
  sleep(Math.random() * 0.3 + 0.2);
}