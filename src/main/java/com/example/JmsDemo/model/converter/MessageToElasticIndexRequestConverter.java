package com.example.JmsDemo.model.converter;

import com.example.JmsDemo.model.Message;
import org.elasticsearch.action.index.IndexRequest;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;

import static com.example.JmsDemo.model.converter.ElasticsearchMessageFieldNames.CONTENT;
import static com.example.JmsDemo.model.converter.ElasticsearchMessageFieldNames.ID;
import static com.example.JmsDemo.model.converter.ElasticsearchMessageFieldNames.PROCESSED;

public class MessageToElasticIndexRequestConverter {

    private MessageToElasticIndexRequestConverter() {
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
}
