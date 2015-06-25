#!/bin/bash

# stop server
sudo /sbin/service wildfly stop
 
# remove war file
sudo rm -rf /opt/wildfly/standalone/deployments/ads.war
 
# copy new deployment
sudo mv /home/ec2-user/ads.war /opt/wildfly/standalone/deployments/
 
# force deployment
sudo touch /opt/wildfly/standalone/deployments/ads.war.dodeploy
 
# restart server
sudo /sbin/service wildfly start