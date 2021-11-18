package com.example.JmsDemo.model.converter;

import com.example.JmsDemo.model.ApiMessageResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class ElasticsearchSearchResponseToApiMessageResponseTest {

    private static final UUID MESSAGE_ID = UUID.fromString("9c896b6d-cd20-46f5-9803-124cad0939b1");
    private static final String MESSAGE_CONTENT = "This is the message content";
    private static final String MESSAGE_PROCESSED = "2021-11-15T23:05:21+01:00";

    @Test
    @DisplayName("Test that the properties of the generated api message response are correct")
    void testCorrectMessageProperties() {
        Map<String, Object> elasticDocument = createElasticDocument();

        ApiMessageResponse apiMessageResponse = ElasticsearchSearchResponseToApiMessageResponse.toMessageResponse(elasticDocument);

        assertThat(apiMessageResponse.getId(), is(MESSAGE_ID));
        assertThat(apiMessageResponse.getContent(), is(MESSAGE_CONTENT));
        assertThat(apiMessageResponse.getProcessed(), is(MESSAGE_PROCESSED));
    }

    @Test
    @DisplayName("Test that the processed date is truncated to seconds")
    void testDateTruncatedToSeconds() {
        Map<String, Object> elasticDocument = createElasticDocument();
        elasticDocument.put("processed", "2021-11-15T23:05:21.261356+01:00");

        ApiMessageResponse apiMessageResponse = ElasticsearchSearchResponseToApiMessageResponse.toMessageResponse(elasticDocument);

        assertThat(apiMessageResponse.getProcessed(), is("2021-11-15T23:05:21+01:00"));
    }

    private Map<String, Object> createElasticDocument() {
        Map<String, Object> props = new HashMap<>();
        props.put("id", MESSAGE_ID);
        props.put("content", MESSAGE_CONTENT);
        props.put("processed", MESSAGE_PROCESSED);
        return props;
    }

}