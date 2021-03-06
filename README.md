### Intro

![Alt text](./docs/JmsDemo.svg)  


This application:
 - Consume messages from an ActiveMQ broker instance or a kafka instance.
 - Process messages and index them in Elasticsearch.
 - Exposed an endpoint to search on all messages stored in Elasticsearch.


### Requirements
This application requires an Elasticseach instance. See the setup section to see how to set that up.

### Run application
Use profiles `activemq` and `kafka` to start the application to connect to the desired message provider.
```
mvn spring-boot:run -Dspring-boot.run.profiles=<profile_name>
```

### Run the tests
To run the tests you can simply execute
```
mvn clean install
```
_Note_: The integration tests start an instance of Elasticsearch which should be available in the Docker instance while
the tests are running.


### API
This application exposed one endpoint to performs text search on all messages in Elasticserch.
Use the following to call the endpoint:
```
curl --location --request GET 'localhost:8080/search?query=content'
```

### Setup
Section that describes the integration setup with the different tools.

##### ActiveMQ
To connect to an existing ActriveMQ instance, update `spring.activemq.broker-url` in the `application-activemq.yml` file.  

Otherwise, you can find a docker-compose file [here](https://github.com/luisfmgoncalves/docker/tree/master/activemq) to start an instance locally.
If you use the container, the ActiveMQ broker manager will be accessible at [http://localhost:8161/admin](http://localhost:8161/admin). Sign in with `admin:admin`.
A topic is automatically created when a message is send to it, so just use the following command to send a message:
```
curl -X POST 'http://admin:admin@localhost:8161/api/message?destination=topic://example-topic&_type=com.example.JmsDemo.model.Message' -H 'Content-Type: application/json' -d '{"id":"9c896b6d-cd20-46f5-9803-124cad0939b1", "content":"This is the message content"}'
```
NOTE: The `_type` parameter in the curl command specifies the fully qualified name of the Message object.

##### Kafka
To connect to an existing Kafka instance, update `spring.kafka.bootstrap-servers` in the `application-kafka.yml` file.  

Otherwise, you can find a docker-compose file [here](https://github.com/luisfmgoncalves/docker/tree/master/kafka) to start an instance locally.
Use [Conduktor](https://www.conduktor.io/) to send messages to topics

##### Elasticsearch
To connect to an existing Elasticsearch instance, update `elasticsearch.*` properties in the `application.yml` file.  

Otherwise, you can find a docker-compose file in `/docker` to start an instance locally.

### Monitoring
Elasticsearch instance is used not only for storing the messages processed from the events, but also for monitoring purposes.  
The docker-compose file present in `/docker` can be used to start a Kibana, Logstash, Heartbeat and APM-server instances which makes monitoring available for this application.  
Below is an image showing the different ELK components involved in the monitoring process:

![Alt text](./docs/Monitoring.svg)  