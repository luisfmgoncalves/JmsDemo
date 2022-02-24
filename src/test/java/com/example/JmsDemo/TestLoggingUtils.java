package com.example.JmsDemo;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

/**
 * Utils class that can be used to test logging messages
 * This class starts a {@link TestLogAppender} which is appended to the Logger of the tested class.
 * Any log messages created when running functionality on the tested class will the intercepted.
 *
 * Usage:
 * ----------------------------------
 * @Test
 * void testLogging() {
 *     TestLoggingUtils loggingUtils = new TestLoggingUtils(<class_to_intercept_logs>);
 *
 *     //run the test that generate the logs
 *
 *     ILoggingEvent lastLoggedEvent = loggingUtils.getLastLoggedEvent();
 *     assertNotNull(lastLoggedEvent);
 *     assertEquals(lastLoggedEvent.getFormattedMessage(), "<expected_log_message>");
 * }
 * ------------------------------------
 */
public class TestLoggingUtils {
    private final TestLogAppender logAppender;

    /**
     * @param clazz the class to intercept the logs
     */
    public TestLoggingUtils(Class<?> clazz) {
        logAppender = new TestLogAppender();
        ((Logger) LoggerFactory.getLogger(clazz)).addAppender(logAppender);
        logAppender.start();
    }

    public ILoggingEvent getLastEvent() {
        return logAppender.getLastLoggedEvent();
    }


    static class TestLogAppender extends AppenderBase<ILoggingEvent> {
        ArrayList<ILoggingEvent> loggingEvents = new ArrayList<>();

        @Override
        protected void append(ILoggingEvent eventObject) {
            loggingEvents.add(eventObject);
        }

        public ILoggingEvent getLastLoggedEvent() {
            if (loggingEvents.isEmpty()) {
                return null;
            }
            return loggingEvents.get(loggingEvents.size() - 1);
        }
    }
}
