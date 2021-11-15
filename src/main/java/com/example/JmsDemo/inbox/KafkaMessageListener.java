package com.example.JmsDemo.inbox;

import com.example.JmsDemo.model.Message;
import com.example.JmsDemo.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;


@Slf4j
@Profile("kafka")
@Component
public class KafkaMessageListener {

    private static final String TOPIC_NAME = "example-topic";
    private static final String SUBSCRIPTION_NAME = "example-topic-subscription";

    private final MessageService messageService;

    public KafkaMessageListener(MessageService messageService) {
        this.messageService = messageService;
    }

    @KafkaListener(topics = TOPIC_NAME, containerFactory = "kafkaListenerContainerFactory", groupId = SUBSCRIPTION_NAME)
    void receiveMessage(Message message) {
        messageService.processMessage(message);
    }

}

