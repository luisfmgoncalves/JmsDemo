package com.example.JmsDemo.service;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import com.example.JmsDemo.TestLoggingUtils;
import com.example.JmsDemo.model.Message;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.index.shard.ShardId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static ch.qos.logback.classic.Level.ERROR;
import static ch.qos.logback.classic.Level.INFO;
import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MessageServiceTest {

    private static final UUID ID = UUID.fromString("9c896b6d-cd20-46f5-9803-124cad0939b1");

    @Mock
    private ElasticsearchService elasticsearchService;

    private TestLoggingUtils loggingUtils;
    private Message message;

    private MessageService testSubject;

    @BeforeEach
    void setup() {
        loggingUtils = new TestLoggingUtils(MessageService.class);
        message = Message.builder()
                         .id(ID)
                         .content("This is a message")
                         .build();

        testSubject = new MessageService(elasticsearchService);
    }

    @Test
    @DisplayName("Test the scenario where a message is successfully indexed in ES")
    void testSuccess() {
        when(elasticsearchService.indexMessage(any(Message.class))).thenReturn(Mono.just(mockIndexResponse()));

        testSubject.processMessage(message);

        assertLastLogMessage(INFO, format("Message %s ingested in Elasticsearch.", ID));
    }

    @Test
    void testError() {
        when(elasticsearchService.indexMessage(any(Message.class))).thenReturn(Mono.error(new RuntimeException("Something went wrong")));

        testSubject.processMessage(message);

        assertLastLogMessage(ERROR, "Fail to ingest message in Elasticsearch.");
    }

    private void assertLastLogMessage(Level logLevel, String message) {
        ILoggingEvent lastLoggedEvent = loggingUtils.getLastEvent();
        assertNotNull(lastLoggedEvent);
        assertEquals(lastLoggedEvent.getLevel(), logLevel);
        assertEquals(lastLoggedEvent.getFormattedMessage(), message);
    }

    private IndexResponse mockIndexResponse() {
        //IndexResponse[index=message,type=_doc,id=9c896b6d-cd20-46f5-9803-124cad0939b1,version=1,result=created,seqNo=0,primaryTerm=1,shards={"total":2,"successful":1,"failed":0}]
        ShardId shardId = new ShardId("message", "51980211-ef30-4543-b061-32ebded8b5f9", 1);
        return new IndexResponse(shardId, "_doc", "9c896b6d-cd20-46f5-9803-124cad0939b1", 0, 1, 1, false);
    }
}