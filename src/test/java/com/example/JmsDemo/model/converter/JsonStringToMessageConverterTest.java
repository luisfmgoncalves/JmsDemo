package com.example.JmsDemo.model.converter;

import com.example.JmsDemo.model.Message;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static java.util.UUID.fromString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

class JsonStringToMessageConverterTest {

    private static final UUID MESSAGE_ID = fromString("9c896b6d-cd20-46f5-9803-124cad0939b1");
    private static final String MESSAGE_CONTENT = "This is the message content";

    @Test
    @DisplayName("Test successful conversion from json string to Message object")
    void testSuccessdulConversionToMessage() {
        String jsonMessage = """
                {"id":"%s","content" : "%s"}"
                """.formatted(MESSAGE_ID, MESSAGE_CONTENT);

        Message message = JsonStringToMessageConverter.fromString(jsonMessage);

        assertThat(message.getId(), is(MESSAGE_ID));
        assertThat(message.getContent(), is(MESSAGE_CONTENT));
    }

    @Test
    @DisplayName("Test failed conversion from json string to Message object")
    void testFailedConversionToMessage() {
        String jsonMessage = """
                {"id":"%s","content" : "%s"}"
                """.formatted("invalid", MESSAGE_CONTENT);

        Message message = JsonStringToMessageConverter.fromString(jsonMessage);

        assertThat(message, is(nullValue()));
    }

}