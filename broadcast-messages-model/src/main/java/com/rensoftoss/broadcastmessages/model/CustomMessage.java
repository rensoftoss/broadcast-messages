package com.rensoftoss.broadcastmessages.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "MESSAGES")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CustomMessage {
    @Id
    @GeneratedValue
    private long id;

    @Column(name = "MESSAGETEXT")
    String messageText;
}
