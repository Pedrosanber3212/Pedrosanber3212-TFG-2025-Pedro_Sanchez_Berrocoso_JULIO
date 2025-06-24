import http from 'k6/http';
import { check, sleep } from 'k6';

const BASE_URL = __ENV.BASE_URL ;  


export const options = {
  stages: [
    { duration: '5s', target: 600 },   
    { duration: '40s',  target: 600 },   
    { duration: '5s', target: 0   },  
  ],
  insecureSkipTLSVerify: true,
  summaryTrendStats: ['avg', 'p(95)'],
};
export default function () {
  const resLogin = http.post(
    `${BASE_URL}/login?username=admin&password=admin`,
    null,
    { tags: { endpoint: 'login' } }
  );
  
  
   console.log(`REQUEST  GET /users`);
  console.log(`STATUS  ${resLogin.status}`);
  check(resLogin, { 'login 200': r => r.status === 200 });

  sleep(1);
}
