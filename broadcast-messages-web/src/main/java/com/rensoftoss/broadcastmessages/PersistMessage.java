package com.rensoftoss.broadcastmessages;


import com.rensoftoss.broadcastmessages.dao.MessagesRepository;
import com.rensoftoss.broadcastmessages.model.CustomMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class PersistMessage {

    @Autowired
    MessagesRepository messagesRepository;

    @Transactional
    public void processMessage(Message message) {
        Map<String, Map<Integer, List<String>>> payload = (Map<String, Map<Integer, List<String>>>) message.getPayload();

        List<String> messageTexts = payload.values().stream().flatMap(value -> value.values().stream()).
                flatMap(Collection::stream).collect(Collectors.toList());

        // convert to custom messages
        List<CustomMessage> customMessages = messageTexts.stream().
                map(messageText -> new CustomMessage(0, messageText)).collect(Collectors.toList());

        messagesRepository.save(customMessages);
    }
}
