package com.rensoftoss.broadcastmessages;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.rensoftoss.broadcastmessages.dao.MessagesRepository;
import com.rensoftoss.broadcastmessages.model.CustomMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.MessageChannel;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@RestController
@Slf4j
public class MessagesController {

    @Qualifier(value = "queueInputChannel")
    @Autowired
    MessageChannel inputQueue;

    @Autowired
    MessagesRepository messagesRepository;

    @Autowired
    MessageConverter messageConverter;

    @RequestMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<String> getMessages() {
        Iterable<CustomMessage> messages = messagesRepository.findAll();
        Stream<CustomMessage> messagesStream = StreamSupport.stream(messages.spliterator(), false);
        return messagesStream.map(CustomMessage::getMessageText).collect(Collectors.toList());
    }

    @RequestMapping(value = "/add", consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
    public void addMessage(@RequestBody String message) throws InvalidMessageFormatException {
        if (!isJSONValid(message)) {
            throw new InvalidMessageFormatException(message);
        }

        log.info("Message input: " + message);
        inputQueue.send(messageConverter.convertTo(message));
    }

    @ExceptionHandler(InvalidMessageFormatException.class)
    public ResponseEntity<ClientErrorInformation> handleInvalidMessageException(InvalidMessageFormatException e, HttpServletRequest request) {
        log.error(e.getMessage(), e);
        ClientErrorInformation clientErrorInformation = new ClientErrorInformation("Invalid message format !", e.getMessage(), request.getRequestURI());
        return new ResponseEntity<>(clientErrorInformation, HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ClientErrorInformation> handleException(InvalidMessageFormatException e, HttpServletRequest request) {
        log.error(e.getMessage(), e);
        ClientErrorInformation clientErrorInformation = new ClientErrorInformation("Error occured ! Please contact server administrator !", e.getMessage(), request.getRequestURI());
        return new ResponseEntity<>(clientErrorInformation, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private static boolean isJSONValid(String jsonInString) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            mapper.readTree(jsonInString);
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
