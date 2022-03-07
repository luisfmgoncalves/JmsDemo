### Intro

![Alt text](https://raw.githubusercontent.com/luisfmgoncalves/JmsDemo/master/docs/JmsDemo.svg)  


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

Otherwise, you can find a docker-compose file [here](https://github.com/luisfmgoncalves/docker/tree/master/elk) to start an instance locally.
Kibana can be used to look at the messages indexed in elasticsearch.
