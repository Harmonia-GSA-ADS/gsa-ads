# Continuous Integration and Deployment

Harmonia uses an internally hosted Jenkins as our continuous integration platform. We use the [Git Flow Workflow](https://www.atlassian.com/git/tutorials/comparing-workflows/gitflow-workflow) model within our Git repository which provides two main branches named develop and master. The develop branch is where developers commit code as they work towards the next release. The master branch holds the latest released version of the code along with tags for previous versions. Any release can always be built from the master branch. We configured four Jenkins jobs for MedFinder seen below.
 
![Jenkins Jobs for MedFinder](MedFinder_Builds.png "Jenkins Jobs for MedFinder") 
 
<a name="tests"></a>
The [MedFinder\_Develop job](MedFinder_Develop%20%5bJenkins%5d.pdf) compiles the application code from the develop branch into a deployable WAR file along with running unit tests and static code analysis tools. The page shows the charts produced from the static code analysis tools like FindBugs and PMD (to detect any code exhibiting NIST’s Common Weakness Enumeration), warnings from Java and JavaDoc, unit test results, and [unit test code coverage](../Unit_Tests/MedFinder%20Unit%20Test%20Coverage.pdf).

The [MedFinder\_Develop\_Stage job](MedFinder_Develop_Stage%20[Jenkins].pdf) runs automatically upon a successful build of *MedFinder_Develop*. It deploys the WAR file to the stage server.

The [MedFinder\_Master job](MedFinder_Master%20[Jenkins].pdf) performs the same activities as the *MedFinder_Develop* build but for the master branch.

The [MedFinder\_Master\_Stage job](MedFinder_Master_Stage%20[Jenkins].pdf) runs automatically upon a successful build of *MedFinder_Master*. Through Jenkins, we use continuous deployment to a Docker container on an [Amazon Web Service (AWS) Platform as a Service instance](MedFinder%20AWS%20Instance.pdf).

<a name="container"></a>
# Container Deployment

Harmonia deployed MedFinder using Docker, which is a platform that enables rapid deployment of applications in isolation from other applications on a system. We install MySQL using the official MySQL Docker into a container named *medfinder-db*. We install Wildfly using the official Wildfly Docker container and make several customizations, through the Docker configuration file, that are needed to run MedFinder. The MedFinder application is deployed in the WildFly instance. The figure below is a screenshot showing the output of the docker ps command which lists the running containers.

 ![Docker Container Listing](MedFinder_Docker.png "Docker Container Lister")
 
See the [configuration files](../../docker) used to support the deployment and the [installation procedure](MedFinder%20Installation%20Procedure.md).
 
On larger projects, we use Puppet to manage the configurations for sets of servers and the configurations are committed to the Git repositories for the projects.  Puppet can be used to manage Docker containers in sophisticated deployment scenarios. Because existing Docker images were readily available which met MedFinder's requirements, the complexity of the deployment did not require the use of Puppet for container configuration management.