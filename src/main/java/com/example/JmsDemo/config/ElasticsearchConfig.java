package com.example.JmsDemo.config;

import co.elastic.clients.elasticsearch.ElasticsearchAsyncClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.http.HttpHost;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;
import static org.apache.http.auth.AuthScope.ANY;

@Configuration
public class ElasticsearchConfig {

    @Bean(name = "elasticSearchClient")
    public ElasticsearchAsyncClient elasticSearchClient(@Value("${elasticsearch.host}") String elasticsearchHost,
                                                        @Value("${elasticsearch.port}") Integer elasticsearchPort,
                                                        @Value("${elasticsearch.user}") String elasticsearchUser,
                                                        @Value("${elasticsearch.pass}") String elasticsearchPass) {

        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(ANY, new UsernamePasswordCredentials(elasticsearchUser, elasticsearchPass));

        // Create the low-level client
        RestClient restClient = RestClient.builder(new HttpHost(elasticsearchHost, elasticsearchPort))
                .setHttpClientConfigCallback(builder -> builder.disableAuthCaching().setDefaultCredentialsProvider(credentialsProvider))
                .build();

        // Create the transport with a Jackson mapper
        ObjectMapper objectMapper = JsonMapper.builder()
                .addModule(new JavaTimeModule())
                .disable(FAIL_ON_UNKNOWN_PROPERTIES)
                .build();
        ElasticsearchTransport transport = new RestClientTransport(restClient, new JacksonJsonpMapper(objectMapper));

        // And create the API client
        return new ElasticsearchAsyncClient(transport);
    }
}
