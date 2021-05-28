package com.example.ticketingsystem.ticket.response;

import com.example.ticketingsystem.ticket.model.Ticket;
import com.example.ticketingsystem.ticket.listeners.MailJPALisener;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;

@Data
@Entity
@Table
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(value = {MailJPALisener.class})
public class Response {

    @Id
    @GeneratedValue
    private long id;

    @NotNull
    @JsonProperty(value = "content")
    private String content;

    @Override
    public String toString() {
        return "Response{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", user='" + user + '\'' +
                '}';
    }

    @JsonProperty(value = "user")
    private String user;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.EAGER, optional = false, cascade=CascadeType.ALL)
    @JoinColumn(name = "ticket_id", insertable = true, updatable = true)
    private Ticket ticket;
}
