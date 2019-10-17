
# nginx-centos7
# Here you can use whatever base image is relevant for your application.
FROM centos:centos7
LABEL maintainer="Albert Lacambra Basil <albert@lacambra.tech>"
ENV NGINX_VERSION=1.6.3

# Set the labels that are used for OpenShift to describe the builder image.
LABEL io.k8s.description="Nginx Webserver" \
    io.k8s.display-name="Nginx 1.6.3" \
    io.openshift.expose-services="8080:http" \
    io.openshift.tags="builder,webserver,html,nginx" \
    # this label tells s2i where to find its mandatory scripts
    # (run, assemble, save-artifacts)
    io.openshift.s2i.scripts-url="image:///usr/libexec/s2i"

# Install the nginx web server package and clean the yum cache
RUN yum install -y epel-release && \
    yum install -y --setopt=tsflags=nodocs nginx && \
    yum clean all

# Change the default port for nginx 
# Required if you plan on running images as a non-root user).
#
RUN sed -i 's/80/8080/' /etc/nginx/nginx.conf
#RUN sed -i 's/user nginx;/user nginx;/' /etc/nginx/nginx.conf

# Copy the S2I scripts to /usr/libexec/s2i since we set the label that way
COPY ./s2i/bin/ /usr/libexec/s2i

ENV NGINX_USER=nginx

#RUN adduser -u 1001 ${NGINX_USER}

RUN chown -R ${NGINX_USER}:${NGINX_USER} /usr/share/nginx
RUN chown -R ${NGINX_USER}:${NGINX_USER} /var/log/nginx
RUN chown -R ${NGINX_USER}:${NGINX_USER} /var/lib/nginx
RUN touch /run/nginx.pid
RUN chown -R ${NGINX_USER}:${NGINX_USER} /run/nginx.pid
RUN chown -R ${NGINX_USER}:${NGINX_USER} /etc/nginx

#Workaround to fix execution with no nginx user...
RUN chmod -R 777 /var/log/nginx
RUN chmod -R 777 /var/lib/nginx
RUN  chmod -R 777 /run/nginx.pid


USER 999

# Set the default port for applications built using this image
EXPOSE 8080

# Modify the usage script in your application dir to inform the user how to run
# this image.
CMD ["/usr/libexec/s2i/usage"]
