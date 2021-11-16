package com.example.JmsDemo.config;

import org.apache.http.HttpHost;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.apache.http.auth.AuthScope.ANY;

@Configuration
public class ElasticsearchConfig {

    @Bean(name = "elasticSearchClient", destroyMethod = "close")
    public RestHighLevelClient elasticSearchClient(@Value("${elasticsearch.host}") String elasticsearchHost,
                                                   @Value("${elasticsearch.user}") String elasticsearchUser,
                                                   @Value("${elasticsearch.pass}") String elasticsearchPass) {

        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(ANY, new UsernamePasswordCredentials(elasticsearchUser, elasticsearchPass));

        return new RestHighLevelClient(
                RestClient.builder(HttpHost.create(elasticsearchHost))
                          .setHttpClientConfigCallback(builder -> builder
                                  .disableAuthCaching()
                                  .setDefaultCredentialsProvider(credentialsProvider))
        );
    }
}
