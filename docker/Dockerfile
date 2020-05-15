FROM java:8
VOLUME /tmp
ADD BlogServer.jar blog-server.jar
RUN bash -c 'touch /blog-server.jar'
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "blog-server.jar"]