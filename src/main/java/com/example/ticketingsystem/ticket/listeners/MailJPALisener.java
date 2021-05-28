package com.example.ticketingsystem.ticket.listeners;

import com.example.ticketingsystem.ticket.mail.MailBuilder;
import com.example.ticketingsystem.ticket.response.Response;
import com.sendgrid.Content;
import com.sendgrid.Email;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.PostPersist;
import java.io.IOException;

public class MailJPALisener<E> {

    private Logger logger = LoggerFactory.getLogger(MailJPALisener.class);

    @PostPersist
    public void sendMail(E entity){
        Response response = (Response)entity;
        String recipient_email_id = response.getTicket().getCustomerMailId();
        MailBuilder mailBuilder = new MailBuilder(new Email(recipient_email_id), new Content("text/plain", response.getContent()));
        try {
            com.sendgrid.Response send_grid_response = mailBuilder.send();
            logger.info("Sendgrid Response: {}", send_grid_response);
        }
        catch (IOException e){
            logger.error("Failed to send email to "+ recipient_email_id);
        }
    }
}
