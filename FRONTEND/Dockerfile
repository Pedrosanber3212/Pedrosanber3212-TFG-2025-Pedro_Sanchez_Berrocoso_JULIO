FROM nginx:alpine

# Eliminar HTML por defecto de NGINX
RUN rm -rf /usr/share/nginx/html/*

# Copiar los archivos estáticos ya compilados
COPY /MONOLITO_FRONT/dist/FRONT-TFG/browser /usr/share/nginx/html

# Copiar configuración de nginx personalizada (usar puerto 4200)
COPY nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 4200

CMD ["nginx", "-g", "daemon off;"]
