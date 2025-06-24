import http from 'k6/http';
import { check } from 'k6';
import { textSummary } from 'https://jslib.k6.io/k6-summary/0.0.1/index.js';


const BASE_URL = __ENV.BASE_URL ;  

export const options = {
  stages: [
    { duration: '5s', target: 80 },   
    { duration: '40s',  target: 80 },   
    { duration: '5s', target: 0   },  
  ],
  insecureSkipTLSVerify: true,
  summaryTrendStats: ['avg', 'p(95)'],
};


export default function () {
  const res = http.get(
    `${BASE_URL}/api/v1/products/page?page=0&size=16&sort=name,ASC`
      );


  console.log(`REQUEST GET /products`);
  console.log(`STATUS ${res.status}`);
  console.log(`BODY  ${res.body}`);  

  check(res, { 'products 200': r => r.status === 200 });
}


export function handleSummary(data) {
  return { stdout: textSummary(data, { indent: ' ', enableColors: true }) };
}
