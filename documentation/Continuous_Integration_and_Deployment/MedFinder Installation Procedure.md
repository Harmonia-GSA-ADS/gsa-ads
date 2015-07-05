# MedFinder Installation Procedure

## Prerequisites

Ensure that you have a Docker-compatible host machine on which to build and install the containers. We recommend that the host machine be given 4GB of RAM and 10 GB of hard drive space. We used Red Hat Enterprise Linux 7 (RHEL7) on Amazon’s cloud infrastructure for our host environment.
In order to build MedFinder from the GitHub repository, your build machine will need to have the following build tools with at least the given versions: 
* [Java JDK 1.7](http://www.oracle.com/technetwork/java/javase/downloads/index.html)
* [Apache Maven 3.2.5](https://maven.apache.org/)
* [NodeJS 0.12.5](https://nodejs.org/)
* [Grunt 0.4.5](http://gruntjs.com/)

These can be installed from their respective websites and should be made available on the path in your environment. 

## Install Docker

Follow the instructions on the Docker page to perform the Docker Engine installation. There are instructions for many different platforms. We followed the instructions for Red Hat Enterprise Linux.

Docker RHEL Installation Instructions: [https://docs.docker.com/installation/rhel/](https://docs.docker.com/installation/rhel/)

## Database Container Setup

To install, configure, and run the database server, use a command similar to the following. The command below creates and runs a new Docker container named *medfinder-db* based on the mysql container provided by the Docker container repository. Each of the **-e** options specifies an environment variable specific to this Docker container to configure the application. The container uses these environment variables to initialize the database. The **-v** option maps a location on the host file system to the file system of the container. This ensures that all data written to the database is actually stored on the host file system. The **-d** option specifies that the container should run in daemon mode (as a service).

```
sudo docker run \
  --name medfinder-db \
  -e MYSQL_ROOT_PASSWORD=changeme \
  -e MYSQL_DATABASE=ads \
  -e MYSQL_USER=ads \
  -e MYSQL_PASSWORD=changeme \
  -v /data/mysql:/var/lib/mysql \
  -d \
  mysql
```

## Application Container Setup

The application container depends on JBoss Wildfly and requires some customization to the default container provided by JBoss before it can be started.

1\.	Check out the repository.

```
git clone https://github.com/HarmoniaHoldings/medfinder.git
```

2\.	Build the Application using Maven.

```
cd medfinder
mvn install
```

3\.	Copy the ads.war file into the docker/medfinder-app directory.

```
cp medfinder-webapp/target/ads.war docker/medfinder-app
```

4\.	Run the automated build which will build the Docker application container image.

```
cd docker/medfinder-app
sudo docker build --tag wildfly-medfinder .
```

5\.	Once the Docker application container is built, we can create the run Docker application container with the following command. The command below creates and runs a new Docker container named *medfinder-app* based on the wildfly-medfinder container we just created. The **--link** option tells Docker to allow the new container being created to communicate with the container named *medfinder-db* which hosts our database server. The **mysql** portion of this command forms the prefix for environment variables exposed by the *medfinder-db* container making them identifiable within the *medfinder-app* container. The **-p** option maps port 80 on the host to port 8080 on the container, allowing traffic on the host to access the container and changing the port in the process. The **-d** option specifies that the container should run in daemon mode (as a service).

```
sudo docker run \
  --name medfinder-app \
  --link medfinder-db:mysql \
  -p 80:8080 \
  -d \
  wildfly-medfinder
```

## Viewing Logs

Problems with startup or execution of these containers can be debugged by viewing the logs files from their startup and execution processes. The following commands make this easy to do by combining the ability to ask Docker for the ID of the container with the specified name and then asking Docker to display its logs.

```
sudo docker logs $(sudo docker ps -a -q --filter=name=medfinder-app)
sudo docker logs $(sudo docker ps -a -q --filter=name=medfinder-db)
```
