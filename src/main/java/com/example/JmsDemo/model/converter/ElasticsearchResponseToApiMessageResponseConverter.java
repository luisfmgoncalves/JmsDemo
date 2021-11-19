package com.example.JmsDemo.model.converter;

import com.example.JmsDemo.model.ApiMessageResponse;
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

public class ElasticsearchResponseToApiMessageResponseConverter {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private ElasticsearchResponseToApiMessageResponseConverter() {
    }

    public static ApiMessageResponse toMessageResponse(Map<String, Object> esDocument) {
        return ApiMessageResponse.builder()
                                 .id(fromString(getStringValue(esDocument, ID)))
                                 .content(getStringValue(esDocument, CONTENT))
                                 .processed(getDateTimeValue(esDocument, PROCESSED))
                                 .build();
    }

    static String getStringValue(Map<String, Object> esDocument, String fieldName) {
        return ofNullable(esDocument)
                .map(map -> map.get(fieldName))
                .map(value -> objectMapper.convertValue(value, String.class))
                .orElse(null);
    }

    static String getDateTimeValue(Map<String, Object> esDocument, String fieldName) {
        return ofNullable(getStringValue(esDocument, fieldName))
                .map(ZonedDateTime::parse)
                .map(s -> s.truncatedTo(SECONDS))
                .map(ISO_OFFSET_DATE_TIME::format)
                .orElse(null);
    }
}
