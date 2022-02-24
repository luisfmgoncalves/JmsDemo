package com.example.JmsDemo.handler;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.matching.UrlPathPattern;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.net.URI;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;
import static org.springframework.http.HttpHeaders.ACCEPT;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MessageAPITest {

    private static final String MESSAGE_ID = "9c896b6d-cd20-46f5-9803-124cad0939b1";
    private static final String MESSAGE_CONTENT = "This is the message content";
    private static final String MESSAGE_PROCESSED = "2022-02-22T23:48:15+01:00";

    private static final UrlPathPattern SEARCH_PATH = urlPathMatching("/search");

    private static WireMockServer wireMockServer;

    @Autowired
    private WebTestClient webTestClient;

    @BeforeEach
    void setup() throws Exception {
        wireMockServer = new WireMockServer(URI.create("http://localhost:8082").toURL().getPort());
        wireMockServer.start();
    }

    @AfterEach
    void teardown() {
        wireMockServer.stop();
    }

    @Test
    void testQueryMessages() {
        wireMockServer.stubFor(get(SEARCH_PATH)
                                       .withQueryParam("query", equalTo("content"))
                                       .willReturn(getResponse(OK)));

        webTestClient.get().uri("/search?query=content")
                     .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                     .exchange()
                     .expectStatus().isOk()
                     .expectBody()
                     .jsonPath("$.[0].id").isEqualTo(MESSAGE_ID)
                     .jsonPath("$.[0].content").isEqualTo(MESSAGE_CONTENT)
                     .jsonPath("$.[0].processed").isEqualTo(MESSAGE_PROCESSED);
    }

    private ResponseDefinitionBuilder getResponse(HttpStatus status) {
        return aResponse()
                .withStatus(status.value())
                .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .withHeader(ACCEPT, APPLICATION_JSON_VALUE)
                .withBody(createMessageBody());
    }

    private byte[] createMessageBody() {
        return  """
                        {
                        "id": "%s",
                        "content": "%s",
                        "processed": "%s"
                        }
                """.formatted(MESSAGE_ID, MESSAGE_CONTENT, MESSAGE_PROCESSED)
                .getBytes();
    }

}