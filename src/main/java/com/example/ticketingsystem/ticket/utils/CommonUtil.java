package com.example.ticketingsystem.ticket.utils;

import com.example.ticketingsystem.ticket.enums.Status;
import com.example.ticketingsystem.ticket.model.Ticket;
import lombok.Getter;

import java.sql.Date;
import java.util.Base64;

@Getter
public class CommonUtil {
    private String userName;

    private String password;

    public CommonUtil(String authenticationDetails){
        String base64Credentials = authenticationDetails.substring("Basic".length()).trim();
        byte[] decodedBytes = Base64.getDecoder().decode(base64Credentials);
        String decodedString = new String(decodedBytes);
        String[] data = decodedString.split(":");
        userName = data[0];
        password = data[1];
    }

    public static Ticket updateTicketDetails(Ticket dbData, Ticket requestData){
        if(requestData.getCustomer()!=null && (dbData.getCustomer()==null || !requestData.getCustomer().equalsIgnoreCase(dbData.getCustomer()))){
            dbData.setCustomer(requestData.getCustomer());
        }

        if(requestData.getCustomerMailId()!=null && (dbData.getCustomerMailId()==null || !requestData.getCustomerMailId().equalsIgnoreCase(dbData.getCustomerMailId()))){
            dbData.setCustomerMailId(requestData.getCustomerMailId());
        }
        if(requestData.getTitle()!=null &&  (dbData.getTitle()==null || !requestData.getTitle().equalsIgnoreCase(dbData.getTitle()))){
            dbData.setTitle(requestData.getTitle());
        }
        if(requestData.getDescription()!=null && (dbData.getDescription()==null || !requestData.getDescription().equalsIgnoreCase(dbData.getDescription()))){
            dbData.setDescription(requestData.getDescription());
        }

        if(requestData.getStatus()!=null &&  requestData.getStatus() != Status.CLOSED && requestData.getStatus() != dbData.getStatus()){
            dbData.setStatus(requestData.getStatus());
            if(requestData.getStatus()== Status.RESOLVED){
                dbData.setResolvedAt(new Date((new java.util.Date()).getTime()));
            }
        }

        if(requestData.getPriority()!=null && requestData.getPriority()!=dbData.getPriority()){
            dbData.setPriority(requestData.getPriority());
        }
        return dbData;
    }
}
