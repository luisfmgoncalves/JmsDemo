package com.example.JmsDemo.inbox;

import com.example.JmsDemo.model.Message;
import com.example.JmsDemo.service.MessageService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@Profile("activemq")
public class MessageListener {

    private static final String TOPIC_NAME = "example-topic";
    private static final String SUBSCRIPTION_NAME = "example-topic-subscription";
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private final MessageService messageService;

    public MessageListener(MessageService messageService) {
        this.messageService = messageService;
    }

    @JmsListener(destination = TOPIC_NAME, containerFactory = "topicJmsListenerContainerFactory", subscription = SUBSCRIPTION_NAME)
    public void receiveMessage(String stringMessage) {
        log.info("received message from ActiveMQ: {}", stringMessage);
        messageService.processMessage(deserialize(stringMessage));
    }

    private Message deserialize(String stringMessage) {
        Message message = null;
        try {
            message = OBJECT_MAPPER.readValue(stringMessage, Message.class);
        } catch (Exception ex) {
            log.error("Could not deserialize {}", stringMessage, ex);
        }
        return message;
    }
}
