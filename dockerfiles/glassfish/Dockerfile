FROM argragas/glassfish6
COPY ../../happi-explorer-example/target/happi-explorer-example-0.0.9-SNAPSHOT.war /opt/glassfish6/domains/domain1/autodeploy/happi-explorer-example-0.0.9-SNAPSHOT.war
COPY ../../deploy/run.sh /tmp/run.sh

CMD sh /tmp/run.sh