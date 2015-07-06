# Continuous Monitoring (CM)

Harmonia used [McAfee Vulnerability Manager](http://www.mcafee.com/us/products/vulnerability-manager.aspx) to perform continuous monitoring of MedFinder.

McAfee Vulnerability scanner was installed on a Windows Server 2008 system, separate from the MedFinder application. We set up a new Workgroup for this project to separate the scan configurations from other projects' scans. We then created two scans, one for our internal stage server and one for the external AWS production server, which were provided with the respective MedFinder application URLs to scan. The two scans used the same configuration of vulnerability sets when performing the scan. It include an extensive number of both intrusive and non-intrusive checks including checks for security flaws listed in the [OWASP Top 10](https://www.owasp.org/index.php/Category:OWASP_Top_Ten_Project). The scans included vulnerability checks under the following categories:

* Web Server
* Cross Site Scripting
* Directory Traversal
* Server Attacks
* SQL Injection
* Database
* HTTP Header
* Information Leakage
* Authentication
* Server Side Code Injection
* Buffer Overflows
* CGI Attacks
* ECommerce
* Informational Crawl-Only Vulnerabilities

The individual checks that were performed in each category are listed in each results PDF in the section called *Vulnerability Check Configuration*. 

A selection of scan results are listed below:
* [1 - MedFinder Stage.pdf](1%20-%20MedFinder%20Stage.pdf)
* [2 - MedFinder Stage.pdf](2%20-%20MedFinder%20Stage.pdf)
* [3 - MedFinder Production (AWS).pdf](3%20-%20MedFinder%20Production%20(AWS).pdf)

The first two scans were performed on our internal stage server during development. The [first scan](1%20-%20MedFinder%20Stage.pdf) had false positive vulnerabilities relating to exposure of social security numbers. This was actually caused by a 9-digit zip code in the license information included on the WildFly welcome page that McAfee interpreted as a social security number. The WildFly welcome page was accessible because MedFinder was not deployed at the root context. We updated the deployment so that it would be deployed at the root context. The [second scan](2%20-%20MedFinder%20Stage.pdf) was performed after that change and shows that the social security vulnerabilities were resolved. 

The [third scan](3%20-%20MedFinder%20Production%20(AWS).pdf) was performed against our production server on AWS. It found an additional service because https is enabled on the production server. We also disabled the TRACE and TRACK HTTP methods, so the informational vulnerability pertaining to those methods is also gone.

We will be continuously monitoring the production system deployed on AWS for the next 30 days by running periodic scans. The results of the scans can be made available upon request.

Several CM products have been evaluated including open source solutions such as OpenSCAP and GovReady. Given the accelerated timeline and previous successful utilization within our organization, McAfee’s Vulnerability Scanner was ultimately chosen due to its scalability, flexible reporting capabilities and ease of use. McAfee is a well-known and trusted provider of security risk management software. It is widely used commercially and provides detection for the latest known vulnerabilities across all major platforms. The risk assessment scoring and correlated dashboard view are invaluable for quickly identifying issues and prioritizing remediation efforts. However, Harmonia can apply also open source CM products for task orders under the Solicitation.