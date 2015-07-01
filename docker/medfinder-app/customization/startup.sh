#!/bin/bash

# Usage: startup.sh
#

JBOSS_HOME=/opt/jboss/wildfly
XMLSTARLET_CMD=/usr/bin/xmlstarlet

STANDALONE_XML=$JBOSS_HOME/standalone/configuration/standalone.xml

echo "=> Updating the database configuration"

DB_NAMESPACE="urn:jboss:domain:datasources:2.0"
DS_JNDI_NAME="java:jboss/datasources/ADS_DS"

CONNECTION_URL="jdbc:mysql://${MYSQL_PORT_3306_TCP_ADDR}:${MYSQL_PORT_3306_TCP_PORT}/${MYSQL_ENV_MYSQL_DATABASE}?useUnicode=true&amp;characterEncoding=UTF-8"

$XMLSTARLET_CMD ed -P -L -N ns=$DB_NAMESPACE -u "//ns:datasource[@jndi-name='$DS_JNDI_NAME']/ns:connection-url" -v "$CONNECTION_URL" $STANDALONE_XML
$XMLSTARLET_CMD ed -P -L -N ns=$DB_NAMESPACE -u "//ns:datasource[@jndi-name='$DS_JNDI_NAME']/ns:security/ns:user-name" -v "$MYSQL_ENV_MYSQL_USER" $STANDALONE_XML
$XMLSTARLET_CMD ed -P -L -N ns=$DB_NAMESPACE -u "//ns:datasource[@jndi-name='$DS_JNDI_NAME']/ns:security/ns:password" -v "$MYSQL_ENV_MYSQL_PASSWORD" $STANDALONE_XML

echo "=> Starting WildFly server"
$JBOSS_HOME/bin/standalone.sh -b 0.0.0.0 -c standalone.xml

