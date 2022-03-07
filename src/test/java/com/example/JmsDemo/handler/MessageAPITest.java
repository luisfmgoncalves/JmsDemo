package com.example.JmsDemo.handler;

import com.example.JmsDemo.model.Message;
import com.example.JmsDemo.service.ElasticsearchService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.elasticsearch.ElasticsearchContainer;

import java.util.UUID;

import static java.time.OffsetDateTime.parse;
import static java.time.format.DateTimeFormatter.ofPattern;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;


@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Slf4j
class MessageAPITest {

    private static final UUID MESSAGE_ID = UUID.fromString("9c896b6d-cd20-46f5-9803-124cad0939b1");
    private static final String MESSAGE_CONTENT = "This is the message content";
    private static final String MESSAGE_PROCESSED = "2022-02-22T23:48:15.123+0100";

    private ElasticsearchContainer container;

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private ElasticsearchService elasticsearchService;

    @BeforeEach
    void setup() {
        container = new ElasticTestContainer().withPassword("elastic");
        container.start();
    }

    @AfterEach
    void teardown() {
        container.stop();
    }

    @Test
    void testQueryMessages() {
        indexTransaction(MESSAGE_ID, MESSAGE_CONTENT, MESSAGE_PROCESSED);

        webTestClient.get().uri("/search?query=content")
                     .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                     .exchange()
                     .expectStatus().isOk()
                     .expectBody()
                     .jsonPath("$.[0].id").isEqualTo(MESSAGE_ID.toString())
                     .jsonPath("$.[0].content").isEqualTo(MESSAGE_CONTENT)
                     .jsonPath("$.[0].processed").isEqualTo(MESSAGE_PROCESSED);
    }

    @Test
    @DisplayName("When multiple messages exist in elasticsearch, only the ones with 'body' in the content are retrieved")
    void testFilterMessageByContent() {
        indexTransaction(MESSAGE_ID, MESSAGE_CONTENT, MESSAGE_PROCESSED);
        indexTransaction(UUID.fromString("bccfe21d-6839-4ce5-af13-1023a5b37155"), "message body", "2022-02-27T23:48:15.123+0100");

        webTestClient.get().uri("/search?query=body")
                .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.length()").isEqualTo(1)
                .jsonPath("$.[0].id").isEqualTo("bccfe21d-6839-4ce5-af13-1023a5b37155")
                .jsonPath("$.[0].content").isEqualTo("message body")
                .jsonPath("$.[0].processed").isEqualTo("2022-02-27T23:48:15.123+0100");
    }

    /**
     * Use the ElasticsearchService to index a transaction in elasticsearch.
     */
    private void indexTransaction(UUID id, String content, String processed) {
            Message message = Message.builder()
                    .id(id)
                    .content(content)
                    .processed(parse(processed, ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ"))).build();
            elasticsearchService.indexMessage(message).block();

        try {
            Thread.sleep(1000); //make sure the message is available in the ES index
        } catch (InterruptedException ex) {
            log.error("Could not hold the thread.", ex);
        }
    }

}