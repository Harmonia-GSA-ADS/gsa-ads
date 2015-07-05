# Continuous Monitoring

Harmonia used [McAfee Vulnerability Manager](http://www.mcafee.com/us/products/vulnerability-manager.aspx) to perform continuous monitoring of MedFinder.

A selection of scan results are listed below:
* [1 - MedFinder Stage.pdf](1%20-%20MedFinder%20Stage.pdf)
* [2 - MedFinder Stage.pdf](2%20-%20MedFinder%20Stage.pdf)
* [3 - MedFinder Production (AWS).pdf](3%20-%20MedFinder%20Production%20(AWS).pdf)

The checks that were performed are listed in each results PDF in the section called *Vulnerability Check Configuration*. It performed both an extensive number of intrusive and non-intrusive checks including checks for security flaws listed in the OWASP Top 10.

The first two scans were performed on our internal stage server during development. The [first scan](1%20-%20MedFinder%20Stage.pdf) had false positive vulnerabilities relating to exposure of social security numbers. This was actually caused by content in the license information included on the WildFly welcome page, which was accessible because MedFinder was not deployed at the root context. We updated the deployment so that it would be deployed at the root context. The [second scan](2%20-%20MedFinder%20Stage.pdf) was performed after that change and shows that the social security vulnerabilities were resolved. 

The [third scan](3%20-%20MedFinder%20Production%20(AWS).pdf) was performed against our production server on AWS. It found an additional service because https is enabled on the production server. We also disabled the TRACE and TRACK HTTP methods, so the informational vulnerability pertaining to those methods is also gone.
