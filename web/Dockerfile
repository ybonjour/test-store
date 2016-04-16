FROM nginx

ADD nginx/sites-available/test-store.conf /etc/nginx/sites-available/test-store.conf
ADD nginx/nginx.conf /etc/nginx/nginx.conf
ADD www /var/www

RUN mkdir -p /etc/nginx/sites-enabled
RUN ln -s /etc/nginx/sites-available/test-store.conf /etc/nginx/sites-enabled/test-store.conf