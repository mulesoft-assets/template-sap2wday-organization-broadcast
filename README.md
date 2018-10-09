
# Anypoint Template: SAP to Workday Organization Broadcast

# License Agreement
This template is subject to the conditions of the 
<a href="https://s3.amazonaws.com/templates-examples/AnypointTemplateLicense.pdf">MuleSoft License Agreement</a>.
Review the terms of the license before downloading and using this template. You can use this template for free 
with the Mule Enterprise Edition, CloudHub, or as a trial in Anypoint Studio.

# Use Case
This Anypoint Template should serve as a foundation for setting an online sync of Organizations from SAP to Workday. Every time there is a new organization or a change in an already existing one, SAP will send the IDoc with it to the running template which will create/update an organization in Workday target instance. Requirements have been set not only to be used as examples, but also to establish a starting point to adapt your integration to your requirements. As implemented, this Anypoint Template leverages the [Batch Module](http://www.mulesoft.org/documentation/display/current/Batch+Processing).	The batch job is divided in Input, Process and On Complete stages. The integration is triggered by a SAP Endpoint that receives the SAP	Organization as IDoc XML. This XML is passed to the batch process. The batch process handle the migration to Workday.

# Considerations

To make this Anypoint Template run, there are certain preconditions that must be considered.
All of them deal with the preparations in both source (SAP) and destination (WDAY) systems, that must be made in order for all to run smoothly.
**Failing to do so could lead to unexpected behavior of the template.**

Before continue with the use of this Anypoint Template, you may want to check out this [Documentation Page](http://www.mulesoft.org/documentation/display/current/SAP+Connector#SAPConnector-EnablingYourStudioProjectforSAP), that will teach you how to work 
with SAP and Anypoint Studio.

## Disclaimer

This Anypoint template uses a few private Maven dependencies in order to work. If you intend to run this template with Maven support, please continue reading.

You will find that there are three dependencies in the pom.xml file that begin with the following group id: 
	**com.sap.conn.jco** 
These dependencies are private for Mulesoft and will cause you application not to build from a Maven command line. You need to replace them with "provided" scope and copy the libraries into the build path.


## SAP Considerations

Here's what you need to know to get this template to work with SAP.

### As a Data Source

The SAP backend system is used as a source of data. The SAP connector is used to send and receive the data from the SAP backend. 
The connector can either use RFC calls of BAPI functions and/or IDoc messages for data exchange, and needs to be properly customized per the "Properties to Configure" section.

The Partner profile needs to have a customized type of logical system set as partner type. An outbound parameter of message type HRMD\_ABA should be defined in the partner profile. A RFC destination created earlier should be defined as Receiver Port. Idoc Type base type should be set as HRMD\_ABA01.




## Workday Considerations


### As a Data Destination

This template makes use of the `External ID` by Workday.

The templates uses the External ID in order to do xRef between the entities in both systems. The idea is, once an entity is created in WDAY it's decorated with an ID from the source system which will be used afteward for the template to reference it.






# Run it!
Simple steps to get SAP to Workday Organization Broadcast running.


## Running On Premises
In this section we help you run your template on your computer.


### Where to Download Anypoint Studio and the Mule Runtime
If you are a newcomer to Mule, here is where to get the tools.

+ [Download Anypoint Studio](https://www.mulesoft.com/platform/studio)
+ [Download Mule runtime](https://www.mulesoft.com/lp/dl/mule-esb-enterprise)


### Importing a Template into Studio
In Studio, click the Exchange X icon in the upper left of the taskbar, log in with your
Anypoint Platform credentials, search for the template, and click **Open**.


### Running on Studio
After you import your template into Anypoint Studio, follow these steps to run it:

+ Locate the properties file `mule.dev.properties`, in src/main/resources.
+ Complete all the properties required as per the examples in the "Properties to Configure" section.
+ Right click the template project folder.
+ Hover your mouse over `Run as`
+ Click `Mule Application (configure)`
+ Inside the dialog, select Environment and set the variable `mule.env` to the value `dev`
+ Click `Run`
In order to make this Anypoint Template run on Mule Studio there are a few extra steps that needs to be made.
Please check this Documentation Page:

+ [Enabling Your Studio Project for SAP](http://www.mulesoft.org/documentation/display/current/SAP+Connector#SAPConnector-EnablingYourStudioProjectforSAP)

### Running on Mule Standalone
Complete all properties in one of the property files, for example in mule.prod.properties and run your app with the corresponding environment variable. To follow the example, this is `mule.env=prod`. 


## Running on CloudHub
While creating your application on CloudHub (or you can do it later as a next step), go to Runtime Manager > Manage Application > Properties to set the environment variables listed in "Properties to Configure" as well as the **mule.env**.


### Deploying your Anypoint Template on CloudHub
Studio provides an easy way to deploy your template directly to CloudHub, for the specific steps to do so check this


## Properties to Configure
To use this template, configure properties (credentials, configurations, etc.) in the properties file or in CloudHub from Runtime Manager > Manage Application > Properties. The sections that follow list example values.
### Application Configuration
**SAP Connector configuration**

+ sap.jco.ashost `your.sap.address.com`
+ sap.jco.user `SAP_USER`
+ sap.jco.passwd `SAP_PASS`
+ sap.jco.sysnr `14`
+ sap.jco.client `800`
+ sap.jco.lang `EN`

**SAP Endpoint configuration**

+ sap.jco.operationtimeout `1000`
+ sap.jco.connectioncount `2`
+ sap.jco.gwhost `your.sap.addres.com`
+ sap.jco.gwservice `sapgw14`
+ sap.jco.idoc.programid `PROGRAM_ID`

**Workday Connector configuration**

+ wday.username `user`
+ wday.tenant `tenant_pt5t`
+ wday.password `secret`
+ wday.host `services.wday.com`
+ wday.org.subtype `Company`
+ wday.org.visibility `Everyone`
+ wday.ext.systemID `systemID`

# API Calls
There are no special considerations regarding API calls.


# Customize It!
This brief guide intends to give a high level idea of how this template is built and how you can change it according to your needs.
As Mule applications are based on XML files, this page describes the XML files used with this template.

More files are available such as test classes and Mule application files, but to keep it simple, we focus on these XML files:

* config.xml
* businessLogic.xml
* endpoints.xml
* errorHandling.xml


## config.xml
Configuration for connectors and configuration properties are set in this file. Even change the configuration here, all parameters that can be modified are in properties file, which is the recommended place to make your changes. However if you want to do core changes to the logic, you need to modify this file.

In the Studio visual editor, the properties are on the *Global Element* tab.


## businessLogic.xml
The business logic XML file creates or updates objects in the destination system for a represented use case. You can customize and extend the logic of this template in this XML file to more meet your needs.



## endpoints.xml
This file is formed by a flow containing the endpoints for triggering the template and for retrieving the objects that meet the defined criteria in the query. You can then execute the batch job process with the query results.



## errorHandling.xml
This is the right place to handle how your integration reacts depending on the different exceptions. 
This file provides error handling that is referenced by the main flow in the business logic.




