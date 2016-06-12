package com.rensoftoss.broadcastmessages.dao;


import com.rensoftoss.broadcastmessages.model.CustomMessage;
import org.springframework.data.repository.CrudRepository;

public interface MessagesRepository extends CrudRepository<CustomMessage, Long> {
}
