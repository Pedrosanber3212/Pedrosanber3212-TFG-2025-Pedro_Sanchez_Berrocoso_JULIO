import http from 'k6/http';
import { check, sleep } from 'k6';
import { textSummary } from 'https://jslib.k6.io/k6-summary/0.0.1/index.js';


// leer el binario UNA sola vez (fase init)
const IMG_PATH = './imag.jpg';
const IMG_FILE = http.file(open(IMG_PATH, 'b'), 'imag.jpg');

const BASE_URL = __ENV.BASE_URL ;  
const jsid = __ENV.JSESSION ; 

export const options = {
  stages: [
    { duration: '5s', target: 400 },   
    { duration: '40s',  target: 400 },   
    { duration: '5s', target: 0   },  
  ],
  insecureSkipTLSVerify: true,
  summaryTrendStats: ['avg', 'p(95)'],
};


export default function () {

  const jar = http.cookieJar();
  jar.set(BASE_URL, 'JSESSIONID', jsid, {
    path: '/', secure: true, httpOnly: true, sameSite: 'None',
  });

  const payload = {
    file: IMG_FILE,
    productUUID: 'imagen_pedro_prueba2',
  };

  const resUp = http.post(`${BASE_URL}/api/v1/fileStorage/productImage`, payload);
  console.log(`UPLOAD  status ${resUp.status}`);
  console.log(`UPLOAD body   ${resUp.body}`);

  check(resUp, {
    'upload 201|200': r => r.status === 201 || r.status === 200,
  });

  sleep(1);
}

export function handleSummary(data) {
  return { stdout: textSummary(data, { indent: ' ', enableColors: true }) };
}
