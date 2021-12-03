package com.example.JmsDemo.service;

import com.example.JmsDemo.model.ApiMessageResponse;
import com.example.JmsDemo.model.converter.ElasticsearchResponseToApiMessageResponseConverter;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.List;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Slf4j
@Service
public class ApiService {

    private final ElasticsearchService elasticsearchService;

    public ApiService(ElasticsearchService elasticsearchService) {
        this.elasticsearchService = elasticsearchService;
    }

    public Mono<ServerResponse> searchMessages(ServerRequest request) {
        String searchQuery = request.queryParam("query").orElse("");
        return elasticsearchService.searchInMessageContent(searchQuery)
                                   .flatMap(response -> ServerResponse.ok()
                                                                        .bodyValue(toMessageList(response)))
                                   .switchIfEmpty(ServerResponse.status(INTERNAL_SERVER_ERROR)
                                                                  .bodyValue("Failed to request data from Elasticsearch"));
    }

    public static List<ApiMessageResponse> toMessageList(SearchResponse searchResponse) {
        System.err.println("test");
        return stream(searchResponse.getHits().getHits())
                .map(SearchHit::getSourceAsMap)
                .map(ElasticsearchResponseToApiMessageResponseConverter::toMessageResponse)
                .collect(toList());

    }
}
