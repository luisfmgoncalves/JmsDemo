package com.example.JmsDemo.model.converter;

import com.example.JmsDemo.model.Message;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JsonStringToMessageConverter {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static Message fromString(String stringMessage) {
        try {
            return objectMapper.readValue(stringMessage, Message.class);
        } catch (Exception ex) {
            log.error("Could not deserialize {}", stringMessage, ex);
            return null;
        }
    }

}
