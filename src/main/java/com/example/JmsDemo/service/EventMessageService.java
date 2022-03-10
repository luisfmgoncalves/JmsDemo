package com.example.JmsDemo.service;

import co.elastic.clients.elasticsearch.core.IndexResponse;
import com.example.JmsDemo.model.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import static java.time.OffsetDateTime.now;
import static java.util.Optional.ofNullable;

@Service
@Slf4j
public class EventMessageService {

    private final ElasticsearchService elasticsearchService;

    public EventMessageService(ElasticsearchService elasticsearchService) {
        this.elasticsearchService = elasticsearchService;
    }

    public void processMessage(Message message) {
        ofNullable(message)
                .filter(m -> m.getId() != null)
                .map(m -> m.toBuilder().processed(now()).build())
                .ifPresent(m -> {
                    log.info("Received message {}. Processing it...", m.getId());
                    indexMessage(m).subscribe();
                });
    }

    private Mono<IndexResponse> indexMessage(Message message) {
        return elasticsearchService.indexMessage(message)
                //no one to notify when indexing a message from an event, so just log information
                .doOnSuccess(response -> log.info("Message {} ingested in Elasticsearch.", message.getId()))
                .doOnError(throwable -> log.error("Fail to ingest message in Elasticsearch."));
    }
}
