package com.example.JmsDemo.handler;

import co.elastic.clients.elasticsearch.core.IndexResponse;
import com.example.JmsDemo.model.Message;
import com.example.JmsDemo.service.ElasticsearchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static com.example.JmsDemo.route.RequestParameters.SEARCH_QUERY_PARAM;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Slf4j
@Component
public final class MessageAPI {

    private final ElasticsearchService elasticsearchService;

    public MessageAPI(ElasticsearchService elasticsearchService) {
        this.elasticsearchService = elasticsearchService;
    }

    public Mono<ServerResponse> searchMessages(ServerRequest serverRequest) {
        String query = serverRequest.queryParam(SEARCH_QUERY_PARAM).orElse("");
        return elasticsearchService.searchInMessageContent(query)
                .collectList()
                .flatMap(apiMessages -> ServerResponse.ok().bodyValue(apiMessages))
                .switchIfEmpty(ServerResponse.status(INTERNAL_SERVER_ERROR).bodyValue("Failed to request data."));
    }
}
