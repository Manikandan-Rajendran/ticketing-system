package com.example.ticketingsystem.ticket.model;

import com.example.ticketingsystem.ticket.enums.Priority;
import com.example.ticketingsystem.ticket.enums.Status;
import com.example.ticketingsystem.ticket.response.Response;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.sql.Date;
import java.util.List;

@Data
@Entity
@Table
public class Ticket {

    @Id
    @GeneratedValue
    private long id;

    @NotBlank(message = "Title should not be blank")
    private String title;

    @NotBlank(message = "Description should not be blank")
    private String description;

    private String type;

    private String createdBy;


    private String customer;

    private String customerMailId;


    private String assignedTo;

    @JsonManagedReference
    @OneToMany(cascade=CascadeType.ALL)
    @JoinColumn(name = "ticket_id")
    private List<Response> responses;


    private Status status;

    @Override
    public String toString() {
        return "Ticket{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", createdBy='" + createdBy + '\'' +
                ", customer='" + customer + '\'' +
                ", assignedTo='" + assignedTo + '\'' +
                ", responses=" + responses +
                ", status=" + status +
                ", priority=" + priority +
                '}';
    }

    private Priority priority;

    private Date resolvedAt;

    public void setDefaultStatus(){
        this.status=Status.OPEN;
    }

    public void setDefaultPriority(){
        this.priority = Priority.P3;
    }
}
