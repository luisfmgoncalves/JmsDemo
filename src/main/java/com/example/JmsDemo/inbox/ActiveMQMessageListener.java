package com.example.JmsDemo.inbox;

import com.example.JmsDemo.model.Message;
import com.example.JmsDemo.model.converter.JsonStringToMessageConverter;
import com.example.JmsDemo.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Slf4j
@Profile("activemq")
@Component
public class ActiveMQMessageListener {

    private static final String TOPIC_NAME = "example-topic";
    private static final String SUBSCRIPTION_NAME = "example-topic-subscription";

    private final MessageService messageService;

    public ActiveMQMessageListener(MessageService messageService) {
        this.messageService = messageService;
    }

    @JmsListener(destination = TOPIC_NAME, containerFactory = "topicJmsListenerContainerFactory", subscription = SUBSCRIPTION_NAME)
    public void receiveMessage(String stringMessage) {
        Message message = JsonStringToMessageConverter.fromString(stringMessage);
        messageService.processMessage(message);
    }

}
