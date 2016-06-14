package com.rensoftoss.broadcastmessages;


import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MessageConverter {
    List<String> convertFrom(Message message) {
        Map<String, Map<Integer, List<String>>> payload = (Map<String, Map<Integer, List<String>>>) message.getPayload();

        return payload.values().stream().flatMap(value -> value.values().stream()).
                flatMap(Collection::stream).collect(Collectors.toList());
    }

    public Message convertTo(String message) {
        return new GenericMessage<>(message);
    }
}
