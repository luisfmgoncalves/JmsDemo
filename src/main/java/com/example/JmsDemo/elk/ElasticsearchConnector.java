package com.example.JmsDemo.elk;

import com.example.JmsDemo.model.Message;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import static com.example.JmsDemo.model.converter.ElasticsearchMessageFieldNames.CONTENT;
import static com.example.JmsDemo.model.converter.MessageToElasticIndexRequestConverter.toIndexRequest;
import static org.elasticsearch.action.support.IndicesOptions.fromOptions;
import static org.elasticsearch.client.RequestOptions.DEFAULT;
import static org.elasticsearch.core.TimeValue.timeValueSeconds;
import static org.elasticsearch.index.query.QueryBuilders.boolQuery;
import static org.elasticsearch.index.query.QueryBuilders.termQuery;

@Slf4j
@Component
public class ElasticsearchConnector {

    private static final String INDEX_NAME = "message";

    private final RestHighLevelClient restHighLevelClient;

    public ElasticsearchConnector(RestHighLevelClient restHighLevelClient) {
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
        SearchRequest searchRequest = buildSearchRequest(searchQuery);
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

    private SearchRequest buildSearchRequest(String searchQuery) {
        SearchSourceBuilder builder = new SearchSourceBuilder();
        builder.timeout(timeValueSeconds(5));

        BoolQueryBuilder boolQueryBuilder = boolQuery();
        boolQueryBuilder.filter(termQuery(CONTENT, searchQuery));
        builder.query(boolQueryBuilder);

        SearchRequest searchRequest = new SearchRequest(INDEX_NAME);
        searchRequest.indicesOptions(fromOptions(true, true, true, false));
        searchRequest.source(builder);
        return searchRequest;
    }
}
