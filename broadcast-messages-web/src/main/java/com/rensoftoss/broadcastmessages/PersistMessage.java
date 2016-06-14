package com.rensoftoss.broadcastmessages;


import com.rensoftoss.broadcastmessages.dao.MessagesRepository;
import com.rensoftoss.broadcastmessages.model.CustomMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class PersistMessage {

    @Autowired
    MessagesRepository messagesRepository;

    @Autowired
    MessageConverter messageConverter;

    @Transactional
    public void processMessage(Message message) {
        log.info("Persist into database: " + message);

        // convert to custom messages
        List<CustomMessage> customMessages = messageConverter.convertFrom(message).stream().
                map(messageText -> new CustomMessage(0, messageText)).collect(Collectors.toList());

        messagesRepository.save(customMessages);
    }
}
