FROM tomcat:10.1.0-jre17

COPY ./happi-explorer-example/target/happi-explorer-example-0.0.9-SNAPSHOT.war /usr/local/tomcat/webapps
RUN cp -avT $CATALINA_HOME/webapps.dist/manager $CATALINA_HOME/webapps/manager

COPY tomcat-users.xml /usr/local/tomcat/conf/
RUN cat /usr/local/tomcat/conf/tomcat-users.xml

COPY context.xml /usr/local/tomcat/webapps/manager/META-INF/
RUN cat /usr/local/tomcat/webapps/manager/META-INF/context.xml

EXPOSE 8080 8443

CMD ["catalina.sh", "run"]