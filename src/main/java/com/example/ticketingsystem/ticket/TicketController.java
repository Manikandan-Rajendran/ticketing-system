package com.example.ticketingsystem.ticket;

import com.example.ticketingsystem.user.User;
import com.example.ticketingsystem.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;

@RestController
public class TicketController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @PostMapping("/api/v1/ticket")
    public Ticket create(@RequestHeader("Authorization") String authenticationDetails, @RequestBody Ticket ticket) throws IllegalAccessException {
        String base64Credentials = authenticationDetails.substring("Basic".length()).trim();
        byte[] decodedBytes = Base64.getDecoder().decode(base64Credentials);
        String decodedString = new String(decodedBytes);
        String userName;
        String password;
        String[] _ = decodedString.split(":");
        userName = _[0];
        password = _[1];
        User user = userRepository.findByUserName(userName);
        if(!user.passwordMatch(password)){
            throw  new IllegalAccessException();
        }
        if(user.isCustomer()){
            ticket.setCustomer(user.getName());
            ticket.setCreatedBy(userName);
        }
        ticketRepository.save(ticket);
        return  ticket;
    }
}
