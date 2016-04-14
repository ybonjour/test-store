FROM nginx

ADD sites-available/test-store.conf /etc/nginx/sites-available/test-store.conf
ADD www /var/www
ADD nginx.conf /etc/nginx/nginx.conf

RUN mkdir -p /etc/nginx/sites-enabled
RUN ln -s /etc/nginx/sites-available/test-store.conf /etc/nginx/sites-enabled/test-store.conf