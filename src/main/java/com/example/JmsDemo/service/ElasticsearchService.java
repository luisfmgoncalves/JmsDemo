package com.example.JmsDemo.service;

import co.elastic.clients.elasticsearch.ElasticsearchAsyncClient;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.example.JmsDemo.exception.ElasticsearchException;
import com.example.JmsDemo.model.Message;
import com.example.JmsDemo.model.api.ApiMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class ElasticsearchService {

    private static final String INDEX_NAME = "messages";
    private static final String MESSAGE_CONTENT_FIELD = "content";

    private final ElasticsearchAsyncClient elasticsearchAsyncClient;

    public ElasticsearchService(ElasticsearchAsyncClient elasticsearchAsyncClient) {
        this.elasticsearchAsyncClient = elasticsearchAsyncClient;
    }

    public Mono<IndexResponse> indexMessage(Message message) {
            return Mono.fromFuture(elasticsearchAsyncClient.index(b -> b.
                            index(INDEX_NAME).id(message.getId().toString()).document(message)))
                    .doOnSuccess(indexResponse -> log.info("Successfully indexed message in Elasticsearch."))
                    .onErrorMap(ex -> new ElasticsearchException("Error occurred while indexing data", ex));
    }

    public Flux<ApiMessage> searchInMessageContent(String query) {
            return Mono.fromFuture(elasticsearchAsyncClient.search(s -> s
                            .index(INDEX_NAME)
                            .query(q -> q
                                    .intervals(i -> i
                                            .field(MESSAGE_CONTENT_FIELD).allOf(a -> a
                                                    .intervals(in -> in
                                                            .match(m -> m.query(query)))))), ApiMessage.class))
                    .doOnNext(result -> log.info("Successfully retrieved {} messages from Elasticsearch", result.hits().hits().size()))
                    .flatMapMany(stream -> Flux.fromIterable(stream.hits().hits().stream().map(Hit::source).toList()))
                    .onErrorMap(ex -> new ElasticsearchException("Error occurred while fetching data", ex));
    }
}
