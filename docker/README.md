## docker-compose.yml
Starts a single node elasticsearch cluster, a kibana, logstash and heartbeat instance with xpack feature enabled

### Elasticsearch
Start the elasticsearch:
```
docker-compose up -d elasticsearch
```
Access the container:
```
docker-compose exec elasticsearch bash
```
Generate passwords for default users:
```
bin/elasticsearch-setup-passwords interactive
```
_NOTE_: The setup of the passwords for the default users just needs to be done once.

### Kibana
Start Kibana:
```
docker-compose up -d kibana
```

### Logstash
Start the logstash:
```
docker-compose up -d logstash
```

### Heartbeat
Start the heartbeat:
```
docker-compose up -d heartbeat
```

### APM server
Start the apm server:
```
docker-compose up -d apm-server
```