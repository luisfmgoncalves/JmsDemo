### Run application with profile
```
mvn spring-boot:run -Dspring-boot.run.profiles=<profile_name>
```

## ActiveMQ
Once the ActiveMQ container is running, the ActiveMQ broker manager is accessible at [http://localhost:8161/admin](http://localhost:8161/admin). Sign in with `admin:admin`.

### Publish message to topic
```
curl -X POST 'http://admin:admin@localhost:8161/api/message?destination=topic://example-topic' -H 'Content-Type: application/json' -d '{"id":"9c896b6d-cd20-46f5-9803-124cad0939b1", "content":"This is the message content"}'
```