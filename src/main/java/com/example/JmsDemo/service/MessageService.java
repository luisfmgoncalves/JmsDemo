package com.example.JmsDemo.service;

import com.example.JmsDemo.exception.ElasticsearchException;
import com.example.JmsDemo.model.Message;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.index.IndexResponse;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import static java.lang.String.format;
import static java.util.Optional.ofNullable;
import static org.elasticsearch.rest.RestStatus.CREATED;

@Service
@Slf4j
public class MessageService {

    private final ElasticsearchService elasticsearchService;

    public MessageService(ElasticsearchService elasticsearchService) {
        this.elasticsearchService = elasticsearchService;
    }

    public void processMessage(Message message) {
        ofNullable(message)
                .filter(m -> m.getId() != null)
                .ifPresent(m -> indexMessage(m).subscribe());

    }

    private Mono<IndexResponse> indexMessage(Message message) {
        return elasticsearchService.indexMessage(message)
                                   .doOnSuccess(response -> {
                                       if (response.status() != CREATED) {
                                           log.warn("Expected response status 201 but got {} when ingesting message {}", response.status(), message.getId());
                                       }
                                       log.info("Message {} ingested in Elasticsearch", message.getId());
                                   })
                                   .doOnError(throwable -> {
                                       throw new ElasticsearchException(format("Error posting message to Elasticsearch: %s", throwable.getMessage()), throwable);
                                   });
    }
}
