package com.example.ticketingsystem.ticket.service;

import com.example.ticketingsystem.ticket.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;
@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    @Query(value = "SELECT * FROM ticket WHERE ticket.customer=:value", nativeQuery = true)
    List<Ticket> findAllByCustomer(@Param("value") String value);

    @Query(value = "SELECT * FROM ticket WHERE ticket.assigned_to=:value", nativeQuery = true)
    List<Ticket> findAllByAgent(@Param("value") String value);

    @Query(value = "SELECT * FROM ticket WHERE ticket.status=:value", nativeQuery = true)
    List<Ticket> findAllByStatus(@Param("value") Integer value);

    @Query(value = "SELECT * FROM ticket WHERE ticket.status=:value and ticket.resolved_at<DATEADD(DAY, 1, :date)", nativeQuery = true)
    List<Ticket> findAllByStatusBeforeAMonth(@Param("value") Integer value, @Param("date") String date);
}
