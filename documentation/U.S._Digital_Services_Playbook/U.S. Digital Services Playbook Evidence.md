# U.S. Digital Services Evidence

This document describes how Harmonia followed the U.S. Digital Services Playbook while developing [MedFinder](https://medfinder.harmonia.com/) for the 18F Agile Delivery Services solicitation.

<a name="understand"></a>
## Understand what people need

Our normal practice when beginning a project is for Harmonia's staff trained as business analysts and in Human Factors to meet with prospective users, facilitate dialog, and understand their needs and goals. These needs are distilled into use cases and requirements, which then feed into the user stories for the product backlog that the development team works on in development sprints.

The solicitation recognized that finding real users would be difficult in the available time frame, and thus we did not have real users with whom we could have discussions. Instead, our Product Owner acted as a stand-in user as we developed a set of use cases that MedFinder would satisfy, and approved them. The Product Owner considered the primary users as two groups:  individual consumers, seeking information on side effects, and medical professionals, seeking to find medications by use and route of administration. We then derived requirements, which were also approved by our Product Owner, from the use cases. We identified that the users would be both consumers who wish to learn about the side effects of drugs and medical professionals who wish to find drugs appropriate for their patients. We hypothesized common questions that both categories of users would have that OpenFDA could answer. For example, a consumer wishing to research the side effects of Percocet reported by people of similar age and weight or a medical professional wishing to find an appropriate medication to treat nasal congestion for a patient who has trouble swallowing pills. See the [Use Cases & Requirements](../Use_Cases_and_Requirements).

[User stories](../User_Stories) were created to satisfy the requirements. These were then used for two development sprints during which the Product Owner representing what people need gave feedback on the MedFinder application. For example, the terminology used in the Consumer user interface was changed to layman's terms (e.g., *side effects* instead of *adverse events*) to simplify the user experience.

## Address the whole experience, from start to finish

It is our normal practice to discover and address existing pain points with the future users of any system, but as explained above the short term of this exercise precluded recruiting real users. We meet with focus groups of real people in the relevant field to understand the challenges and gaps they find with existing solutions. That information is taken into account when developing use cases and requirements. Typically, addressing the pain points becomes the primary goal of the solution being delivered.

We also have experience building software for different delivery needs, such as on a laptop or a mobile device. We often have to build applications that can run in a disconnected mode with the ability to sync up to a remote system when connectivity is restored.

On this project, as previously stated, we did not have real users with whom we could discuss challenges and ideal delivery mechanisms. Instead, with the Product Owner representing real users, we envisioned pain points in the way users currently find drug information on the Internet, which our MedFinder service could make easier:

* It is not easy for a consumer to search for drug side effects with respect to their personal criteria, such as age, weight, and gender.
* It is not easy for a medical professional to view the routes of administration for a particular drug.
* It is not easy for a medical professional to find a medication to address a particular need that can be taken via a route of administration that is acceptable to the patient.

In Harmonia's normal development process, we would conduct user testing with representative users. One method we use is to ask users to perform tasks, and then collect several metrics:  abandonment rate (users give up completing the task), the time taken to perform the task, count of negative versus positive utterances when users are asked to speak aloud their reactions as they perform tasks, etc.  This addresses Play 2's question of what metrics would best indicate how well the service is working for users. 

## Make it simple and intuitive

Harmonia employs Human Factors trained experts that ensure applications we build deliver the best user experience possible. For this solicitation, since we were developing an application for Pool 2 Development, the UI was not the primary focus. However, we still applied common-sense usability practices to the application. The interface is clean and streamlined so the user is not distracted by unnecessary information and can easily find the starting point to the task at hand. The language used within the consumer and medical professional sections was tailored for the different audiences as exemplified above in *[Understand what people need](#understand)* where we strove for "plain and universal" language.  We created style rules so that the user interface appearance and behavior for consumers versus medical professional is consistent in our service.  We added pop up help but limited in size so that we did not discourage reading the help text. 

## Build the service using agile and iterative practices

Play 4 was a hallmark of our approach to realizing MedFinder.  The process used to build the application for this solicitation can be found at in the [README file](../README.md). A detailed description of [Harmonia's Agile process](../Agile_Process/Harmonia's%20Agile%20Process.docx) is also available.

## Structure budgets and contracts to support delivery

For the prototype, we started by defining the scope and used the solicitation to guide our definition of deliverables. We scheduled time for researching activities (e.g., for the OpenFDA APIs) and for commercial cloud deployment on AWS, and set metrics/milestones for our team to complete all deliverables on time. Once the scope of the prototype was determined a budget for the effort was created. The team lead and the company Controller worked together to estimate the number of hours by team member that would be required for completion of the prototype. The budgeted hours included time for research, discovery and prototyping activities as well as development and testing. The Controller then priced these resources and hours to determine a project budget. For this solicitation prototype, the solicitation was treated as the "contract" which held the team to defined deliverables and defined time table.

## Assign one leader and hold that person accountable

Dr. Marc Abrams (Harmonia's Chief Technical Officer) was assigned as the Product Owner for MedFinder. He is experienced from serving as Product Owner in past contracts with NIH (for the RapidStat app in the Microsoft Excel store) and other projects with the U.S. Navy and DISA. From those experiences he has managerial and technical authority for the Product Owner role. Harmonia also uses a RoadMap document to act as an overall work plan to account for budgeted funds versus features planned, and anticipate funding sources.  He participated in all sprint meetings as well as requirements review. He reviewed, suggested changes for, and signed off on use cases, requirements, user stories, their acceptance tests, and whether they passed or failed.

## Bring in experienced teams

For the MedFinder project, we used Harmonia employees in our Blacksburg, VA office who applied Harmonia's standard Agile processes (appraised at CMMI DEV Level 3). They are experienced from using Agile since 2009 and Scrum since 2011.  Team Lead Kelly Eagan is experienced from building a critical portal for the U.S. Navy used by sailors worldwide on 140+ ships to obtain weather and oceanographic data for navigation:  The Navy Enterprise Portal Oceanographic (NEP-Oc).  

The core team building MedFinder (Ms. Eagan, Robert Harry, and Jeff Anway) were taken off a project for the U.S. Navy for designing a new mobile system called Personal Assistant for Navy Recruiters, which will also provide apps for members of the public that apply to enlist in the Navy.  The project envisions how recruiters across the U.S. can be technically relevant to 18 or 19 year olds through social media, mobile apps, secure texting, while providing career counseling, secure transfer of personally identifiable information, and greener use of resources (e.g., less miles driven by recruiters) by continuously matching recruiters to prospects based in part on geography. The work represents a true partnership of a team of Navy cyber experts, business system owners (Navy PEO EIS), contracting, recruiters as end users, and system engineers with Harmonia's Agile team applying continuous integration. We are exploring DevOps with continuous deployment, and the Navy has been very flexible in rethinking the SPAWAR system engineering process for their mobile business applications.

## Choose a modern technology stack

All [technologies](../Technologies/MedFinder%20Technologies.md) that were chosen for MedFinder are commonly-used, well-supported, open source technologies. All of them are also being actively maintained and developed, so they fit the criteria for modern and open.

Java was chosen as the programming language for the server component. It is deployable across all platforms. We chose a common J2EE technology stack including Java Persistence API (JPA), Enterprise JavaBeans (EJB), Java Transaction API (JTA), and Java API for RESTful Web Services (JAX-RS).  On the client, we are primarily using Bootstrap and jQuery. Both are very popular, modern web technologies that have very strong cross-browser and device support.  All are widely used by private-sector companies.

For development we use the popular open source Eclipse environment, Jenkins as our continuous integration server, Maven for builds, Junit for testing, and several other open source technologies.  For deployment we used WildFly as our application server with MySQL as the database, and Docker for containers. 

## Deploy in a flexible hosting environment

MedFinder is [deployed on Amazon Web Services (AWS)](../Continuous_Integration_and_Deployment/MedFinder%20AWS%20Instance.pdf). AWS provides numerous configuration and deployment options for provisioning and scaling on demand. We have designed MedFinder to deploy in a container, which in a real production setting would allow us to run multiple instances that can be spun up dynamically in response to surges in traffic.

## Automate testing and deployments

Harmonia applies automated testing as part of continuous integration for all of our in-house Agile projects, including the Agile Delivery Services project.  We have 60% unit test coverage for MedFinder, which includes tests for all server code that does not require deployment in a container. Some of the code that is not covered could have unit tests written using mocking, but the tests would serve no valuable purpose other than increasing the code coverage.  Given the artificial nature of MedFinder and the 18F solicitation, we did not have target usage volumes or other performance criteria to test against.

All development team members use Selenium for automated UI tests in other projects, but it was not used in this project since we were developing for Pool 2 Development.

The unit tests are run as part of our Jenkins builds, which are triggered automatically within five minutes of a commit to the code repository. Jenkins reports on tests failures and [code coverage](../Unit_Tests/MedFinder%20Unit%20Test%20Coverage.pdf). Upon successful builds with no test failures, Jenkins also [deploys the application](../Continuous_Integration_and_Deployment/README%20-%20Continuous%20Integration%20and%20Deployment.md) to the AWS instance by transferring updated build artifacts to the server through SSH and executing a deployment script. The script runs commands described in the [MedFinder Installation Procedure](../Continuous_Integration_and_Deployment/MedFinder%20Installation%20Procedure.md) to create or update the Docker container in which the application is deployed so that it uses the new WAR file.

## Manage security and privacy through reusable processes

MedFinder does not collect or store personal information. Nevertheless, we followed the HTTPS Everywhere guidance in [Office of Management and Budget M-15-13](https://www.whitehouse.gov/sites/default/files/omb/memoranda/2015/m-15-13.pd). This provides privacy to consumers and medical professionals who use MedFinder, for example from an eavesdropper who wishes to see what drugs a particular user is interested in by HTTPS's encryption of query parameters and response data. We also used the OWASP Enterprise Security API (ESAPI) library in the client and the server for parameter encoding to protect against injection attacks

MedFinder is deployed on AWS which is a FedRAMP Compliant Cloud Service Provider.

## Use data to drive decisions

While Harmonia addresses Play 12 in its contract supporting services by metrics, Play 12 did not really apply to the artificial exercise for the 18F solicitation, because MedFinder is a one-time exercise and not an ongoing service.  But for information purposes, Play 12 is paramount in our 80+ person support contract to the U.S. Department of Agriculture Farm Services Agency (FSA) Database Management Office (DBMO) contract, where we do operations and development for 250+ databases serving 3,000 USDA offices around the country.  In that case we petitioned USDA for a full time quality assurance specialist who was an expert in metrics to define comprehensive metrics including those areas in Play 12, publish those to USDA, and then use those to gauge the effectiveness of system improvements. Also in several cases across Harmonia we apply formal experiment design methods and statistical methods such as Analysis of Variance to identify which factors have the greatest influence on performance when navigating how to evolve services to maximize customer satisfaction. 

## Default to open
We defaulted to open because the source code for MedFinder is published publically in a [GitHub repository](https://github.com/HarmoniaHoldings/medfinder). Documentation describing aspects of the development process and development artifacts (e.g., use cases and requirements) are also available in the repository. GitHub provides an [issue reporting and tracking capability](https://github.com/HarmoniaHoldings/medfinder/issues?utf8=%E2%9C%93&q=is%3Aissue) that anyone can use to report bugs and feature requests.
