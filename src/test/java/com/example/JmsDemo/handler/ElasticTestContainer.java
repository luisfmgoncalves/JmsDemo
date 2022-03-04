package com.example.JmsDemo.handler;

import org.testcontainers.elasticsearch.ElasticsearchContainer;

/**
 * Custom Elasticsearch test container to run integration tests.
 * This class is used to spin up an elasticsearch instance during the integration tests.
 */
public class ElasticTestContainer extends ElasticsearchContainer {

    private static final String DOCKER_ELASTIC = "docker.elastic.co/elasticsearch/elasticsearch:8.0.1";
    private static final String CLUSTER_NAME = "es-cluster";
    private static final String ELASTIC_SEARCH = "elasticsearch";

    public ElasticTestContainer() {
        super(DOCKER_ELASTIC);
        this.addFixedExposedPort(9200, 9200);
        this.addEnv(CLUSTER_NAME, ELASTIC_SEARCH);
    }

}
