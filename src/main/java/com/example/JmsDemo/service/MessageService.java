package com.example.JmsDemo.service;

import com.example.JmsDemo.model.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class MessageService {

    public void processMessage(Message message) {
        Optional.ofNullable(message)
                .ifPresent(m -> log.info("Processed message '{}'", m.getId()));

    }
}
