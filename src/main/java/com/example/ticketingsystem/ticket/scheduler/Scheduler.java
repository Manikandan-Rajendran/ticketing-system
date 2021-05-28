package com.example.ticketingsystem.ticket.scheduler;

import com.example.ticketingsystem.ticket.enums.Status;
import com.example.ticketingsystem.ticket.model.Ticket;
import com.example.ticketingsystem.ticket.service.TicketRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.util.List;

@EnableAsync
@Component
public class Scheduler {

    @Autowired
    private TicketRepository ticketRepository;

    private final Logger logger = LoggerFactory.getLogger(Scheduler.class);

    @Async
    @Scheduled(fixedRate = 6*60*60*1000) //Every 6 hours
    public void closeResolvedTickets(){
        logger.info("Initiated Scheduled task");
        List<Ticket> tickets = ticketRepository.findAllByStatusBeforeAMonth(Status.RESOLVED.getValue(), new Date((new java.util.Date()).getTime()).toString());
        for(Ticket ticket : tickets){
            ticket.setStatus(Status.CLOSED);
        }
        ticketRepository.saveAll(tickets);
        logger.info("Finished Scheduled task");
    }
}
