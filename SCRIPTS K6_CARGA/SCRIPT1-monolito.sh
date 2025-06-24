#!/bin/bash

cd "C:\Users\pedro\Documents\PRUEBAS_CARGA\MONOLITO\PRODUCTS" 

echo "Iniciando tests PRODUCTS MONOLITO SEMIESTRUCTURADO"
sleep 3

k6 run -e BASE_URL=https://157.90.27.9:8020 -e K6_TLS_INSECURE_SKIP_VERIFY=1 --summary-export mo1_products_load_60.json   test_general_products_load_60.js
sleep 15

k6 run -e BASE_URL=https://157.90.27.9:8020 -e K6_TLS_INSECURE_SKIP_VERIFY=1 --summary-export mo1_products_load_80.json   test_general_products_load_80.js
sleep 15

k6 run -e BASE_URL=https://157.90.27.9:8020 -e K6_TLS_INSECURE_SKIP_VERIFY=1 --summary-export mo1_products_load_100.json  test_general_products_load_100.js
sleep 15

k6 run -e BASE_URL=https://157.90.27.9:8020 -e K6_TLS_INSECURE_SKIP_VERIFY=1 --summary-export mo1_products_load_200.json  test_general_products_load_200.js
sleep 15

k6 run -e BASE_URL=https://157.90.27.9:8020 -e K6_TLS_INSECURE_SKIP_VERIFY=1 --summary-export mo1_products_load_400.json  test_general_products_load_400.js
sleep 15

k6 run -e BASE_URL=https://157.90.27.9:8020 -e K6_TLS_INSECURE_SKIP_VERIFY=1 --summary-export mo1_products_load_600.json  test_general_products_load_600.js
sleep 15

k6 run -e BASE_URL=https://157.90.27.9:8020 -e K6_TLS_INSECURE_SKIP_VERIFY=1 --summary-export mo1_products_load_800.json  test_general_products_load_800.js

echo "Finalizado PRODUCTOS"


echo "INICIO USERS"


cd "C:\Users\pedro\Documents\PRUEBAS_CARGA\MONOLITO\USERS" 


sleep 15


k6 run -e BASE_URL=https://157.90.27.9:8020 -e K6_TLS_INSECURE_SKIP_VERIFY=1 --summary-export mo1_users_load_60.json  test_general_users_load_60.js

sleep 15
k6 run -e BASE_URL=https://157.90.27.9:8020 -e K6_TLS_INSECURE_SKIP_VERIFY=1 --summary-export mo1_users_load_80.json  test_general_users_load_80.js
sleep 15

k6 run -e BASE_URL=https://157.90.27.9:8020 -e K6_TLS_INSECURE_SKIP_VERIFY=1 --summary-export mo1_users_load_100.json test_general_users_load_100.js
sleep 15

k6 run -e BASE_URL=https://157.90.27.9:8020 -e K6_TLS_INSECURE_SKIP_VERIFY=1 --summary-export mo1_users_load_200.json test_general_users_load_200.js
sleep 15

k6 run -e BASE_URL=https://157.90.27.9:8020 -e K6_TLS_INSECURE_SKIP_VERIFY=1 --summary-export mo1_users_load_400.json test_general_users_load_400.js
sleep 15

k6 run -e BASE_URL=https://157.90.27.9:8020 -e K6_TLS_INSECURE_SKIP_VERIFY=1 --summary-export mo1_users_load_600.json test_general_users_load_600.js

sleep 15
k6 run -e BASE_URL=https://157.90.27.9:8020 -e K6_TLS_INSECURE_SKIP_VERIFY=1 --summary-export mo1_users_load_800.json test_general_users_load_800.js


echo "Finalizado USERS"

echo "INICIO STORAGE"


cd "C:\Users\pedro\Documents\PRUEBAS_CARGA\MONOLITO\STORAGE" 

sleep 15




k6 run -e BASE_URL=https://157.90.27.9:8020 -e K6_TLS_INSECURE_SKIP_VERIFY=1 -e JSESSION=099DC4C7191557C845F1280A7E7C37B1 --summary-export mo1_storage_load_60.json test_general_storage_load_60.js

sleep 15

k6 run -e BASE_URL=https://157.90.27.9:8020 -e K6_TLS_INSECURE_SKIP_VERIFY=1 -e JSESSION=099DC4C7191557C845F1280A7E7C37B1 --summary-export mo1_storage_load_80.json test_general_storage_load_80.js

sleep 15

k6 run -e BASE_URL=https://157.90.27.9:8020 -e K6_TLS_INSECURE_SKIP_VERIFY=1 -e JSESSION=099DC4C7191557C845F1280A7E7C37B1 --summary-export mo1_storage_load_100.json test_general_storage_load_100.js

sleep 15

k6 run -e BASE_URL=https://157.90.27.9:8020 -e K6_TLS_INSECURE_SKIP_VERIFY=1 -e JSESSION=099DC4C7191557C845F1280A7E7C37B1 --summary-export mo1_storage_load_200.json test_general_storage_load_200.js

sleep 15

k6 run -e BASE_URL=https://157.90.27.9:8020 -e K6_TLS_INSECURE_SKIP_VERIFY=1 -e JSESSION=099DC4C7191557C845F1280A7E7C37B1 --summary-export mo1_storage_load_400.json test_general_storage_load_400.js
sleep 15


k6 run -e BASE_URL=https://157.90.27.9:8020 -e K6_TLS_INSECURE_SKIP_VERIFY=1 -e JSESSION=099DC4C7191557C845F1280A7E7C37B1 --summary-export mo1_storage_load_600.json test_general_storage_load_600.js

sleep 15

k6 run -e BASE_URL=https://157.90.27.9:8020 -e K6_TLS_INSECURE_SKIP_VERIFY=1 -e JSESSION=099DC4C7191557C845F1280A7E7C37B1 --summary-export mo1_storage_load_800.json test_general_storage_load_800.js


echo "Finalizado STORAGE"

