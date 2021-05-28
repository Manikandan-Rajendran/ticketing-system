package com.example.ticketingsystem.ticket.mail;

import com.sendgrid.*;

import java.io.IOException;

public class MailBuilder {

//    @Value("${mail.sendgrid.api_key}")
    private String sendGridAPIKey = "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";

//    @Value("${mail.sendgrid.sender_mail_id}")
    private String senderMailId = "yogesh@sinecycle.com";

    private Email from;

    private Email to;

    private Content content;



    public MailBuilder(Email to, Content content){
        this.from =  new Email(senderMailId);
        this.to = to;
        this.content = content;
    }


    public Response send() throws IOException {
        final SendGrid mailer = new SendGrid(sendGridAPIKey);
        Mail mail = new Mail(from, "Response added", to, content);
        final Request request = new Request();
        request.setMethod(Method.POST);
        request.setEndpoint("mail/send");
        request.setBody(mail.build());
        return mailer.api(request);
    }
}
