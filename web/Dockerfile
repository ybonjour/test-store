FROM nginx

RUN apt-get update && apt-get install -y netcat

ADD scripts /scripts

ADD nginx/sites-available/test-store.conf /etc/nginx/sites-available/test-store.conf
ADD nginx/nginx.conf /etc/nginx/nginx.conf
ADD www /var/www
ADD www2/dist/test-store-dashboard /var/www2

RUN mkdir -p /etc/nginx/sites-enabled
RUN ln -s /etc/nginx/sites-available/test-store.conf /etc/nginx/sites-enabled/test-store.conf

CMD /scripts/wait-for-backend.sh $BACKEND_HOST $BACKEND_PORT && nginx -g "daemon off;"
