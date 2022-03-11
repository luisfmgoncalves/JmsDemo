package com.example.JmsDemo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

import static co.elastic.apm.attach.ElasticApmAttacher.attach;

@Configuration
@Profile("apm")
public class ApmMonitoring {

    @PostConstruct
    public void attachApmAgent(@Value("${apm-server.url}") String apmServerUrl,
                               @Value("${apm-server.secret_token}") String apmServerSecret) {
        Map<String, String> apmConfig = new HashMap<>();

        apmConfig.put("server_urls", apmServerUrl);
        apmConfig.put("secret_token", apmServerSecret);
        apmConfig.put("environment", "dev");
        apmConfig.put("enable_log_correlation", "true");
        apmConfig.put("service_name", "jms-demo");
        apmConfig.put("application_packages", "com.example.JmsDemo");
        apmConfig.put("trace_methods", "com.example.JmsDemo.*");

        attach(apmConfig);
    }

}
