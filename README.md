# Broadcast messages to connected clients


## Introduction

Welcome to the **broadcast-messages** repository, which provides a sample chat server to send messages to connected browser clients.

The application uses the following technologies: 

* Java 8
* Spring Framework (Spring Boot, Spring Integration, Spring Data)
* Kafka
* REST
* PostgreSQL

The add message REST endpoint is taking a dummy JSON input, and the server puts the REST payload on a queue based on Kafka.
Additionally a consumer is running in the application, which takes the freshly received message from the queue and persists it in a PostgreSQL database. 
The list messages REST endpoint is also implemented for retrieving in JSON format all the messages that are persisted in the database.
The messages are also be pushed through Websockets for listening browser clients at the time the message was received 
on the add message REST endpoint.
A simple HTML page is availabe to show the real time message delivery on the connected browser clients. 
Only the last message which is sent will be displayed. 

## Installation

The installation can take place on an EC2 instance using Ubuntu 14.04 image on AWS platform.
For more information how to setup such an instance please visit [Amazon Web Services Homepage]

Also you need to setup a PostgreSQL database i.e. on an RDS instance.
After packaging the project the database schema is available in `schema.sql`, which can be used to
create the necessary objects in database.

The following details contains installation instructions after setting up and logged on an EC2 instance.

* Install Java 8 SDK:

    `sudo add-apt-repository ppa:openjdk-r/ppa`
    
    `sudo apt-get update`
    
    `sudo apt-get install openjdk-8-jdk`
    
     If you have more than one Java versions installed on your system. Run below command set the default Java:
     
    `sudo update-alternatives --config java`

* Download the binary of Maven packaging tool from **https://maven.apache.org/download.cgi**
* Extract the downloaded file and add the appropriate _bin_ directory to the PATH like follows:

    `export PATH=/home/ubuntu/temp/apache-maven-3.3.9/bin:$PATH`

* Next you need to download and extract the provided source code and
package it with Maven from the directory where the files have been extracted by
typing the following command:

    `mvn clean package`
    
    **Warning:** Compiling the provided source needs memory, so maybe the compilation need to be done locally
    in case the EC2 instance has small amount of memory.

* Go to the `deployments` directory in your folder and set permissions as follows:

    `chmod +x *.sh`

* Install the compiled sources:

    `mkdir /opt`
    
    `mkdir /opt/broadcast-messages`
    
    `sudo cp -r /home/ubuntu/temp/broadcast-messages-master/deployments/* /opt/broadcast-messages`

* Setup automatic start for the application. The config may need to be adjusted with some application parameters, 
which will be described later in **Coding details** section

    `sudo cp /opt/broadcast-messages/broadcast-messages.conf /etc/init`    

* Install Zookeeper on EC2 instance:

    `sudo apt-get install zookeeperd`
    
* Install latest Kafka distribution on EC2 instance:
    
    Download the latest distribution of Kafka and extract it into `/opt`
    The downloaded repository contains sample configuration for Kafka in directory **deployments/kafka**,
    which are the files named **producer.properties** and **server.properties**.
    The amount of memory used by Kafka can be set in **kafka-server-start.sh** as in the example.
    You need to setup the automatic start of Kafka as follows:
    
    `sudo cp deployments/kafka/kafka.conf /etc/init`
    
    **Warning:** the correct path to Kafka maybe need to be updated in `kafka.conf`
    
    You need to create the topics that the application uses like follows:
    
    `kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic messages`
    
    `kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic persistdb`

* Open the following ports on EC2 instance **8080** and **8180**. This should reflect the setup in the **broadcast-messages.conf**, which
 configures two instances from the application

* Additionally a load balancer with an elastic ip can be set on AWS platform to forward the requests to the configured server instances,
but this requires to configure more than one EC2 instances 
    
* Once the installation is completed the server can be restarted.
    

**Hints:** Before restarting the server the following commands can be run to make sure the Zookeeper registers Kafka correctly

    sudo rm /var/lib/zookeeper/version-2/*
     
Also the logs can be deleted with folowing commands:

    sudo rm /var/log/zookeeper/*
    sudo rm /opt/kafka_2.11-0.10.0.0/logs/*
    sudo rm /tmp/broadcast-messages*

Checking of available application logs is possible on server side:

    ll /tmp/broadcast-messages*     
     
## Usage details     
     
Connect with browser to the correct address where the instance(s) or load balancer is running (i.e http://localhost:8080/)
     
The available REST endpoints are the followings (replace the address with the appropriate host and port of the running instance):

* [Add message REST endpoint]: can be used to send a JSON message to the clients.

Sending valid JSON:

    `curl -i -X POST -H "Content-Type:application/json" http://localhost:8080/add -d '{"title":"Hoops" }'`

After sending a valid JSON message the connected browser clients will receive and show the last message that has been sent.
    
Sending invalid JSON:

    `curl -i -X POST -H "Content-Type:application/json" localhost:8080/add -d '{"title":"Hoops }'`

In case the message is not a valid JSON the response will be in the following format:
   
    {
        "message":"Invalid message format !",
        "detail":"{\"title\":\"Hoops }",
        "uri":"/add"
    }

* [List messages REST endpoint]: can be used to list retrieve all available messages that has already been sent to the server in the past     

    `curl -v -H "Accept:application/json" -H "Accept-encoding:gzip" http://localhost:8080/list`


[Add message REST endpoint]: http://localhost:8080/add
[List messages REST endpoint]: http://localhost:8080/list
[Amazon Web Services Homepage]: http://aws.amazon.com

## Code details

The following application parameters may need to be adjusted before running the instances (see **broadcast-messages.conf** for details):
    
    # PostgreSQL database url and login information
    spring.datasource.url  
    spring.datasource.username
    spring.datasource.password

    kafka.hosts # list of available brokers host:port separated by comma 
    zookeeper.host # list of available brokers host:port separated by comma

    server.port # Port of the server instance
    server.server-name # Name of the server instance

The application is started using Spring Boot which import the available spring config xml resources.
The REST endpoints are implemented in `MessageController`, which also autowires the input channel `queueInputChannel` component defined
in Spring configuration xml files to be available to send the messages into Kafka. 

The main flows are implemented using Spring integration which are followed:

* Sending message from add message REST endpoint to Kafka (`producer-config.xml`)
* The common configuration of Kafka queue is found in `consumer-config.xml`. The application
 routes the messages to database and connected browser clients by using two topics named `messages` and `persistdb`.
These consumers are configured in  `consumer-db-config.xml` and `consumer-ws-config.xml`
When any message arrives from Kafka it will be persisted into the database and will be sent to the clients.
* Please notice that the consumer groups are different, because the messages in `persistdb` topic has to be consumed by
only one running application instance, while `messages` topic will be consumed by all instances.
* The websocket configuration in `websocket-config.xml` uses STOMP protocol to communicate with the clients. 
It also stores the `sessionId` and `subscriptionId` of the connected browser clients to communicate with them later.
The messages will be delivered to clients via the `sendMessageChannel`.

## Test details

Following test cases are identified:

* Send one JSON message: it will be shown to all connected clients
* Send a different JSON message: it will be shown to all connected clients
* Send invalid JSON message: the clients will not receive this message and the response 
from REST endpoint will inform about the wrong message  
* List of available messages: int he response all messages, which are in the database, are listed in JSON format
* Send message to different instance(s): the message will be shown for all connected browser clients 

It is also recommended to make load tests and scalability tests with SoapUI.
 
The above test cases are available under `testcases` directory. 
Please make sure you adapt the host and port according to your configuration of server instances.

## Useful resources

* [Stomp Chat]
* [Spring and Kafka]
* [Spring Integration Kafka]

[Stomp Chat]: https://github.com/spring-projects/spring-integration-samples/tree/master/applications/stomp-chat
[Spring and Kafka]: https://github.com/joshlong/spring-and-kafka
[Spring Integration Kafka]: https://github.com/spring-projects/spring-integration-kafka


