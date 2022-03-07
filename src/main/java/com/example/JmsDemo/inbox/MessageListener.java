package com.example.JmsDemo.inbox;

import com.example.JmsDemo.model.Message;

/**
 * Interface that defines the contract for message receivers.
 */
public interface MessageListener {

    /**
     * Receives a published message
     * @param message - the received message
     */
    void receive(Message message);

}
