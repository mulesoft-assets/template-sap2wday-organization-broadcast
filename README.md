
# Anypoint Template: SAP to Workday Organization Broadcast	

<!-- Header (start) -->
Broadcasts changes or creations of organizations in SAP to Workday in real time. The detection criteria and fields to move are configurable. Additional systems can be added to be notified of the changes. 

Real time synchronization is achieved via rapid polling of SAP. This template uses Mule batching and watermarking to capture only recent changes, and to efficiently process many records at a time.

![fdb57c5e-7ac3-4f44-9629-742fbce35717-image.png](https://exchange2-file-upload-service-kprod.s3.us-east-1.amazonaws.com:443/fdb57c5e-7ac3-4f44-9629-742fbce35717-image.png)

<!-- Header (end) -->

# License Agreement
This template is subject to the conditions of the <a href="https://s3.amazonaws.com/templates-examples/AnypointTemplateLicense.pdf">MuleSoft License Agreement</a>. Review the terms of the license before downloading and using this template. You can use this template for free with the Mule Enterprise Edition, CloudHub, or as a trial in Anypoint Studio. 
# Use Case
<!-- Use Case (start) -->
This template helps you perform an online sync of organizations from SAP to Workday. Each time a new organization or a change in an already existing one occurs, SAP sends an IDoc to the running template which creates or updates an organization in the Workday target instance. Requirements have been set not only to be used as examples, but also to establish a starting point to adapt your integration to your requirements. As implemented, this template leverages the Mule batch module. The batch job is divided in input, process, and on complete stages. The integration is triggered by the SAP endpoint that receives the SAP organization as an IDoc XML. This XML is passed to the batch process. The batch process handles the migration to Workday.
<!-- Use Case (end) -->

# Considerations
<!-- Default Considerations (start) -->

<!-- Default Considerations (end) -->

<!-- Considerations (start) -->
To make this template run, there are certain preconditions that must be considered for the preparations in both source (SAP) and destination (WDAY) systems, that must be made for the template to run smoothly. Failing to do so could lead to unexpected behavior of the template.

Before continuing with the use of this template, see [Install the SAP Connector in Studio](https://docs.mulesoft.com/connectors/sap/sap-connector#install-the-sap-connector-in-studio), to teach you how to work with SAP and Anypoint Studio.

## Disclaimer

This template uses a few private Maven dependencies to work. If you intend to run this template with Maven support,  continue reading. There are dependencies in the pom.xml file that begin with the following group ID: 	**com.sap.conn.jco** - These dependencies are private for MuleSoft and cause your application not to build from a Maven command line. You need to replace them with "provided" scope and copy the libraries into the build path.
<!-- Considerations (end) -->


## SAP Considerations

Here's what you need to know to get this template to work with SAP.

### As a Data Source

The SAP backend system is used as a source of data. The SAP connector is used to send and receive data from the SAP backend. The connector can either use RFC calls of BAPI functions and/or IDoc messages for data exchange, and needs to be properly customized per the "Properties to Configure" section.

The partner profile needs to have a customized type of logical system set as partner type. Define an outbound parameter of message type HRMD_ABA in the partner profile. A RFC destination created earlier should be defined as the Receiver Port. IDoc Type base type should be set as HRMD_ABA01.

## Workday Considerations

The following sections provide more info.

### As a Data Destination

This template makes use of the `External ID` by Workday.

The template uses the External ID to handle cross references between the entities in both systems. The idea is, once an entity is created in Workday it's decorated with an ID from the source system which is used afterward for the template to reference it.

# Run it!
Simple steps to get this template running.
<!-- Run it (start) -->

<!-- Run it (end) -->

## Running On Premises
In this section we help you run this template on your computer.
<!-- Running on premise (start) -->

<!-- Running on premise (end) -->

### Where to Download Anypoint Studio and the Mule Runtime
If you are new to Mule, download this software:

+ [Download Anypoint Studio](https://www.mulesoft.com/platform/studio)
+ [Download Mule runtime](https://www.mulesoft.com/lp/dl/mule-esb-enterprise)

**Note:** Anypoint Studio requires JDK 8.
<!-- Where to download (start) -->

<!-- Where to download (end) -->

### Importing a Template into Studio
In Studio, click the Exchange X icon in the upper left of the taskbar, log in with your Anypoint Platform credentials, search for the template, and click Open.
<!-- Importing into Studio (start) -->

<!-- Importing into Studio (end) -->

### Running on Studio
After you import your template into Anypoint Studio, follow these steps to run it:

1. Locate the properties file `mule.dev.properties`, in src/main/resources.
1. Complete all the properties required as per the examples in the "Properties to Configure" section.
1. Right click the template project folder.
1. Hover your mouse over `Run as`.
1. Click `Mule Application (configure)`.
1. Inside the dialog, select Environment and set the variable `mule.env` to the value `dev`.
1. Click `Run`.
<!-- Running on Studio (start) -->
To make this template run in Anypoint Studio there are a few extra steps that needs to be made. See [Install the SAP Connector in Studio](https://docs.mulesoft.com/connectors/sap/sap-connector#install-the-sap-connector-in-studio) for more information.
<!-- Running on Studio (end) -->

### Running on Mule Standalone
Update the properties in one of the property files, for example in mule.prod.properties, and run your app with a corresponding environment variable. In this example, use `mule.env=prod`. 

## Running on CloudHub
When creating your application in CloudHub, go to Runtime Manager > Manage Application > Properties to set the environment variables listed in "Properties to Configure" as well as the mule.env value.
<!-- Running on Cloudhub (start) -->

<!-- Running on Cloudhub (end) -->

### Deploying a Template in CloudHub
In Studio, right click your project name in Package Explorer and select Anypoint Platform > Deploy on CloudHub.
<!-- Deploying on Cloudhub (start) -->

<!-- Deploying on Cloudhub (end) -->

## Properties to Configure
To use this template, configure properties such as credentials, configurations, etc.) in the properties file or in CloudHub from Runtime Manager > Manage Application > Properties. The sections that follow list example values.
### Application Configuration
<!-- Application Configuration (start) -->
**SAP Connector Configuration**

+ sap.jco.ashost `your.sap.address.com`
+ sap.jco.user `SAP_USER`
+ sap.jco.passwd `SAP_PASS`
+ sap.jco.sysnr `14`
+ sap.jco.client `800`
+ sap.jco.lang `EN`

**SAP Endpoint Configuration**

+ sap.jco.operationtimeout `1000`
+ sap.jco.connectioncount `2`
+ sap.jco.gwhost `your.sap.addres.com`
+ sap.jco.gwservice `sapgw14`
+ sap.jco.idoc.programid `PROGRAM_ID`

**Workday Connector Configuration**

+ wday.username `user`
+ wday.tenant `tenant_pt5t`
+ wday.password `secret`
+ wday.host `services.wday.com`
+ wday.org.subtype `Company`
+ wday.org.visibility `Everyone`
+ wday.ext.systemID `systemID`
<!-- Application Configuration (end) -->

# API Calls
<!-- API Calls (start) -->
There are no special considerations regarding API calls.
<!-- API Calls (end) -->

# Customize It!
This brief guide provides a high level understanding of how this template is built and how you can change it according to your needs. As Mule applications are based on XML files, this page describes the XML files used with this template. More files are available such as test classes and Mule application files, but to keep it simple, we focus on these XML files:

* config.xml
* businessLogic.xml
* endpoints.xml
* errorHandling.xml
<!-- Customize it (start) -->

<!-- Customize it (end) -->

## config.xml
<!-- Default Config XML (start) -->
This file provides the configuration for connectors and configuration properties. Only change this file to make core changes to the connector processing logic. Otherwise, all parameters that can be modified should instead be in a properties file, which is the recommended place to make changes.
<!-- Default Config XML (end) -->

<!-- Config XML (start) -->

<!-- Config XML (end) -->

## businessLogic.xml
<!-- Default Business Logic XML (start) -->
The business logic XML file creates or updates objects in the destination system for a represented use case. You can customize and extend the logic of this template in this XML file to more meet your needs.
<!-- Default Business Logic XML (end) -->

<!-- Business Logic XML (start) -->

<!-- Business Logic XML (end) -->

## endpoints.xml
<!-- Default Endpoints XML (start) -->
This file contains the endpoints for triggering the template and for retrieving the objects that meet the defined criteria in a query. You can execute a batch job process with the query results.
<!-- Default Endpoints XML (end) -->

<!-- Endpoints XML (start) -->

<!-- Endpoints XML (end) -->

## errorHandling.xml
<!-- Default Error Handling XML (start) -->
This file handles how your integration reacts depending on the different exceptions. This file provides error handling that is referenced by the main flow in the business logic.
<!-- Default Error Handling XML (end) -->

<!-- Error Handling XML (start) -->

<!-- Error Handling XML (end) -->

<!-- Extras (start) -->

<!-- Extras (end) -->
