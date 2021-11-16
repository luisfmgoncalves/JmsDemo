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

    private final ElasticsearchConnector elasticsearchConnector;

    public MessageService(ElasticsearchConnector elasticsearchConnector) {
        this.elasticsearchConnector = elasticsearchConnector;
    }

    public void processMessage(Message message) {
        ofNullable(message)
                .ifPresent(m -> indexMessage(m).subscribe());

    }

    public Mono<IndexResponse> indexMessage(Message message) {
        return elasticsearchConnector.indexMessage(message)
                                     .doOnNext(response -> {
                                         if (response.status() != CREATED) {
                                             log.error("Error posting message to Elasticsearch: {}", response);
                                         }
                                     });
    }
}
