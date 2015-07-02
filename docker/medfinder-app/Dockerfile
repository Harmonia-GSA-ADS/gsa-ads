FROM jboss/wildfly

ADD customization /opt/jboss/wildfly/customization/
ADD modules /opt/jboss/wildfly/modules/

RUN /opt/jboss/wildfly/customization/execute.sh

ADD ads.war /opt/jboss/wildfly/standalone/deployments/

CMD ["/opt/jboss/wildfly/customization/startup.sh"]

