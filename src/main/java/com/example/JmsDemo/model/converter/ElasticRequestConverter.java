package com.example.JmsDemo.model.converter;

import com.example.JmsDemo.model.Message;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;

import static com.example.JmsDemo.model.converter.ElasticsearchMessageFieldNames.CONTENT;
import static com.example.JmsDemo.model.converter.ElasticsearchMessageFieldNames.ID;
import static com.example.JmsDemo.model.converter.ElasticsearchMessageFieldNames.PROCESSED;
import static org.elasticsearch.action.support.IndicesOptions.fromOptions;
import static org.elasticsearch.core.TimeValue.timeValueSeconds;
import static org.elasticsearch.index.query.QueryBuilders.boolQuery;
import static org.elasticsearch.index.query.QueryBuilders.termQuery;

public class ElasticRequestConverter {

    private ElasticRequestConverter() {
    }

    public static IndexRequest toIndexRequest(String index, Message message) {
        String docId = message.getId().toString();
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put(ID, docId);
        jsonMap.put(CONTENT, message.getContent());
        jsonMap.put(PROCESSED, OffsetDateTime.now());

        return new IndexRequest(index)
                .id(docId)
                .source(jsonMap);
    }

    public static SearchRequest toSearchRequest(String index, String searchQuery) {
        SearchSourceBuilder builder = new SearchSourceBuilder();
        builder.timeout(timeValueSeconds(5));

        BoolQueryBuilder boolQueryBuilder = boolQuery();
        boolQueryBuilder.filter(termQuery(CONTENT, searchQuery));
        builder.query(boolQueryBuilder);

        SearchRequest searchRequest = new SearchRequest(index);
        searchRequest.indicesOptions(fromOptions(true, true, true, false));
        searchRequest.source(builder);
        return searchRequest;
    }
}
