package com.rensoftoss.broadcastmessages.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "MESSAGES")
@NoArgsConstructor
@Data
public class CustomMessage {
    @Id
    @GeneratedValue
    private long id;

    @Column(name = "MESSAGETEXT")
    String messageText;
}
