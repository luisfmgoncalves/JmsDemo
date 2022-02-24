package com.example.JmsDemo.service;

import com.example.JmsDemo.model.Message;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.index.IndexResponse;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import static java.util.Optional.ofNullable;

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
                                   //no one to notify when indexing a message from an event, so just log information
                                   .doOnSuccess(response -> log.info("Message {} ingested in Elasticsearch.", message.getId()))
                                   .doOnError(throwable -> log.error("Fail to ingest message in Elasticsearch."));
    }
}
