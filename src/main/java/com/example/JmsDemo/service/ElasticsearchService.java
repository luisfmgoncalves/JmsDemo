package com.example.JmsDemo.service;

import com.example.JmsDemo.model.Message;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import static com.example.JmsDemo.model.converter.ElasticRequestConverter.toIndexRequest;
import static com.example.JmsDemo.model.converter.ElasticRequestConverter.toSearchRequest;
import static org.elasticsearch.client.RequestOptions.DEFAULT;

@Slf4j
@Component
public class ElasticsearchService {

    private static final String INDEX_NAME = "message";

    private final RestHighLevelClient restHighLevelClient;

    public ElasticsearchService(RestHighLevelClient restHighLevelClient) {
        this.restHighLevelClient = restHighLevelClient;
    }

    public Mono<IndexResponse> indexMessage(Message message) {
        IndexRequest indexRequest = toIndexRequest(INDEX_NAME, message);
        return Mono.create(monoSink -> restHighLevelClient.indexAsync(indexRequest, DEFAULT, new ActionListener<>() {
            @Override
            public void onResponse(IndexResponse indexResponse) {
                monoSink.success(indexResponse);
            }

            @Override
            public void onFailure(Exception e) {
                log.warn("Failed to perform index request for message : {}", message.toString());
                monoSink.error(e);
            }
        }));
    }

    public Mono<SearchResponse> searchInMessageContent(String searchQuery) {
        SearchRequest searchRequest = toSearchRequest(INDEX_NAME, searchQuery);
        return Mono.create(monoSink -> restHighLevelClient.searchAsync(searchRequest, DEFAULT, new ActionListener<>() {
            @Override
            public void onResponse(SearchResponse searchResponse) {
                monoSink.success(searchResponse);
            }

            @Override
            public void onFailure(Exception e) {
                log.warn("Failed to perform search request for query : {}", searchQuery);
                monoSink.error(e);
            }
        }));
    }
}
