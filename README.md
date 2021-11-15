# Intro
This application shows how to:
 - Consume and process messages from an ActiveMQ broker instance.
 - Consume and process messages from a Kafka instance.

### Run application
Use profiles `activemq` and `kafka` to start the application to connect to the desired message provider.
```
mvn spring-boot:run -Dspring-boot.run.profiles=<profile_name>
```

## ActiveMQ
To connect to an existing instance of ActiveMQ, update `spring.activemq.broker-url` in the `application-activemq.yml` file.  

Otherwise, you can find a docker-compose file [here](https://github.com/luisfmgoncalves/docker/tree/master/activemq) to start an instance locally.
If you use the container, the ActiveMQ broker manager will be accessible at [http://localhost:8161/admin](http://localhost:8161/admin). Sign in with `admin:admin`.

#### Publish a message to a topic
A topic is automatically created when a message is send to it, so just use the following command to send a message:
```
curl -X POST 'http://admin:admin@localhost:8161/api/message?destination=topic://example-topic' -H 'Content-Type: application/json' -d '{"id":"9c896b6d-cd20-46f5-9803-124cad0939b1", "content":"This is the message content"}'
```

## Kafka
To connect to an existing instance of ActiveMQ, update `spring.kafka.bootstrap-servers` in the `application-kafka.yml` file.  

Otherwise, you can find a docker-compose file [here](https://github.com/luisfmgoncalves/docker/tree/master/kafka) to start an instance locally.

#### Publish a message to a topic
Use [Conduktor](https://www.conduktor.io/)