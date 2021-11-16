package com.example.JmsDemo.model.converter;

import com.example.JmsDemo.model.MessageResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.ZonedDateTime;
import java.util.Map;

import static com.example.JmsDemo.model.converter.ElasticsearchMessageFieldNames.CONTENT;
import static com.example.JmsDemo.model.converter.ElasticsearchMessageFieldNames.ID;
import static com.example.JmsDemo.model.converter.ElasticsearchMessageFieldNames.PROCESSED;
import static java.time.format.DateTimeFormatter.ISO_OFFSET_DATE_TIME;
import static java.time.temporal.ChronoUnit.SECONDS;
import static java.util.Optional.ofNullable;
import static java.util.UUID.fromString;

public class ElasticsearchSearchResponseToServerResponse {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private ElasticsearchSearchResponseToServerResponse() {
    }

    public static MessageResponse toMessage(Map<String, Object> esDocument) {
        return MessageResponse.builder()
                .id(fromString(getStringValue(esDocument, ID)))
                .content(getStringValue(esDocument, CONTENT))
                .processed(getDateTimeValue(esDocument, PROCESSED))
                .build();
    }

    static String getStringValue(Map<String, Object> esDocument, String fieldName) {
        Object value = esDocument.get(fieldName);
        return value == null ? "" : objectMapper.convertValue(value, String.class);
    }

    static String getDateTimeValue(Map<String, Object> esDocument, String fieldName) {
        return ofNullable(getStringValue(esDocument, fieldName))
                .map(ZonedDateTime::parse)
                .map(s -> s.truncatedTo(SECONDS))
                .map(ISO_OFFSET_DATE_TIME::format)
                .orElse(null);
    }
}
