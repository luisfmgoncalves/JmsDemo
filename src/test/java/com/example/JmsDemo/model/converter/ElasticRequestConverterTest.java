package com.example.JmsDemo.model.converter;

import com.example.JmsDemo.model.Message;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.UUID;

import static java.lang.Boolean.TRUE;
import static java.lang.String.format;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

class ElasticRequestConverterTest {

    private static final UUID MESSAGE_ID = UUID.fromString("9c896b6d-cd20-46f5-9803-124cad0939b1");
    private static final String MESSAGE_CONTENT = "This is the message content";

    private static final String INDEX_NAME = "message";

    private static final String SEARCH_QUERY = "content";

    @Test
    @DisplayName("Test that a valid index request is generated for an index name and a message object")
    void testValidIndexRequest() {
        Message message = createMessage();

        IndexRequest indexRequest = ElasticRequestConverter.toIndexRequest(INDEX_NAME, message);

        assertThat(indexRequest.validate(), is(nullValue()));
    }

    @Test
    @DisplayName("Test that the index request contains the correct index name")
    void testIndexNameInIndexRequest() {
        Message message = createMessage();

        IndexRequest indexRequest = ElasticRequestConverter.toIndexRequest(INDEX_NAME, message);

        String requestPattern = format("index {[%s][_doc][%s]", INDEX_NAME, MESSAGE_ID);
        assertThat(indexRequest.toString().contains(requestPattern), is(TRUE));
    }

    @Test
    @DisplayName("Test that the index request contains the correct source")
    void testValidMessageSourceInIndexRequest() {
        Message message = createMessage();

        IndexRequest indexRequest = ElasticRequestConverter.toIndexRequest(INDEX_NAME, message);

        Map<String, Object> source = indexRequest.sourceAsMap();
        assertThat(source.get("id"), is(MESSAGE_ID.toString()));
        assertThat(source.get("content"), is(MESSAGE_CONTENT));
    }

    @Test
    @DisplayName("Test that a valid search request is generated for an index name and a string search query")
    void testValidSearchRequest() {
        SearchRequest searchRequest = ElasticRequestConverter.toSearchRequest(INDEX_NAME, SEARCH_QUERY);

        assertThat(searchRequest.validate(), is(nullValue()));
    }

    @Test
    @DisplayName("Test that the index request contains the correct index name")
    void testIndexNameInSearchRequest() {
        SearchRequest searchRequest = ElasticRequestConverter.toSearchRequest(INDEX_NAME, SEARCH_QUERY);

        String requestPattern = format("SearchRequest{searchType=QUERY_THEN_FETCH, indices=[%s]", INDEX_NAME);
        assertThat(searchRequest.toString().contains(requestPattern), is(TRUE));
    }

    @Test
    @DisplayName("Test that the search request contains the correct source")
    void testValidMessageSource() {
        SearchRequest searchRequest = ElasticRequestConverter.toSearchRequest(INDEX_NAME, SEARCH_QUERY);

        String relevantRequestPattern = """
                "query":{"bool":{"filter":[{"term":{"content":{"value":"%s","boost":1.0}}}]""".formatted(SEARCH_QUERY);
        String requestString = searchRequest.source().toString();
        assertThat(requestString.contains(relevantRequestPattern), is(TRUE));
    }

    private Message createMessage() {
        return Message.builder()
                      .id(MESSAGE_ID)
                      .content(MESSAGE_CONTENT)
                      .build();
    }

}