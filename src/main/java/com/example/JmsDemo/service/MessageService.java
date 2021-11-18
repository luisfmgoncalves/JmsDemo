package com.example.JmsDemo.service;

import com.example.JmsDemo.elastic.ElasticsearchConnector;
import com.example.JmsDemo.model.Message;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.index.IndexResponse;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import static java.util.Optional.ofNullable;
import static org.elasticsearch.rest.RestStatus.CREATED;

@Service
@Slf4j
public class MessageService {

    private final ElasticsearchConnector connector;

    public MessageService(ElasticsearchConnector elasticsearchConnector) {
        this.connector = elasticsearchConnector;
    }

    public void processMessage(Message message) {
        ofNullable(message)
                .filter(m -> m.getId() != null)
                .ifPresent(m -> indexMessage(m).subscribe());

    }

    public Mono<IndexResponse> indexMessage(Message message) {
        return connector.indexMessage(message)
                        .doOnSuccess(response -> {
                            if (response.status() != CREATED) {
                                log.warn("Expected response status 201 but got {} when ingesting message {}",
                                         response.status(), message.getId());
                            }
                            log.warn("Message {} ingested in Elasticsearch", message.getId());
                        })
                        .doOnError(throwable -> {
                            log.error("Error posting message to Elasticsearch: {}", throwable.getMessage());
                        });
    }
}
