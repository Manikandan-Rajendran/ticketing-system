package com.example.ticketingsystem.ticket.controller;

import com.example.ticketingsystem.ticket.model.CommonResponseModel;
import com.example.ticketingsystem.ticket.enums.Status;
import com.example.ticketingsystem.ticket.model.Ticket;
import com.example.ticketingsystem.ticket.response.Response;
import com.example.ticketingsystem.ticket.service.TicketRepository;
import com.example.ticketingsystem.ticket.utils.CommonUtil;
import com.example.ticketingsystem.user.model.User;
import com.example.ticketingsystem.user.service.UserRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1")
public class TicketController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TicketRepository ticketRepository;


    @PostMapping("/ticket")
    public Ticket create(@NotNull @RequestHeader("Authorization") String authenticationDetails, @Valid @RequestBody Ticket ticket) throws IllegalAccessException {
        CommonUtil commonUtil = new CommonUtil(authenticationDetails);
        User user = userRepository.findByUserName(commonUtil.getUserName());
        if(user==null || !user.passwordMatch(commonUtil.getPassword())){
            throw  new ResponseStatusException(HttpStatus.FORBIDDEN, "Invalid session");
        }
        if(user.isCustomer()){
            ticket.setCustomer(user.getName());
            ticket.setCustomerMailId(user.getMailId());
        }else{
            if(ticket.getCustomer()==null ||  ticket.getCustomerMailId()==null){
                throw  new ResponseStatusException(HttpStatus.BAD_REQUEST, "Customer info not found");
            }
        }
        ticket.setCreatedBy(commonUtil.getUserName());
        ticket.setDefaultPriority();
        ticket.setDefaultStatus();
        ticket.setAssignedTo(userRepository.findAssignedTo().getUserName());
        ticketRepository.save(ticket);
        return  ticket;
    }

    @GetMapping("/ticket/{id}")
    public Ticket getTicket(@NotNull @RequestHeader("Authorization") String authenticationDetails, @PathVariable long id) throws IllegalAccessException {
        CommonUtil commonUtil = new CommonUtil(authenticationDetails);
        User user = userRepository.findByUserName(commonUtil.getUserName());
        if(user==null || !user.passwordMatch(commonUtil.getPassword())){
            throw  new ResponseStatusException(HttpStatus.FORBIDDEN, "Invalid session");
        }
        Optional<Ticket> OptionalTicket = ticketRepository.findById(id);
        if( !OptionalTicket.isPresent()){
            throw  new ResponseStatusException(HttpStatus.NOT_FOUND, "Ticket not found");
        }
        Ticket ticket = OptionalTicket.get();
        if (user.isCustomer() && ticket.getCustomer()!= user.getName()) {
            throw  new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authorized");
        }
        return  ticket;
    }

    @GetMapping("/tickets")
    public List<Ticket> getTickets(@NotNull @RequestHeader("Authorization") String authenticationDetails, @RequestParam(name = "filterBy", required = false, defaultValue = "") String filterBy, @RequestParam(name="value", required = false) String value) {
        CommonUtil commonUtil = new CommonUtil(authenticationDetails);
        User user = userRepository.findByUserName(commonUtil.getUserName());
        if(user==null || !user.passwordMatch(commonUtil.getPassword())){
            throw  new ResponseStatusException(HttpStatus.FORBIDDEN, "Invalid session");
        }
        if (user.isCustomer()){
            throw  new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authorized");
        }
        if(filterBy.isEmpty())
            return ticketRepository.findAll();
        else
        {
            List<Ticket> tickets = null;
            switch (filterBy){
                case "customer":
                    tickets = ticketRepository.findAllByCustomer(value);
                    break;
                case "agent":
                    tickets = ticketRepository.findAllByAgent(value);
                    break;
                case "status":
                    tickets = ticketRepository.findAllByStatus(Status.valueOf(value.toUpperCase()).getValue());
                    break;
                default:
                    throw  new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid filter");
            }
            return tickets;
        }
    }

    @DeleteMapping("/ticket/{id}")
    public ResponseEntity<CommonResponseModel> deleteTicket(@NotNull @RequestHeader("Authorization") String authenticationDetails, @PathVariable long id) {
        CommonUtil commonUtil = new CommonUtil(authenticationDetails);
        User user = userRepository.findByUserName(commonUtil.getUserName());
        if(user==null || !user.passwordMatch(commonUtil.getPassword())){
            throw  new ResponseStatusException(HttpStatus.FORBIDDEN, "Invalid session");
        }
        if (user.isCustomer()){
            throw  new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authorized");
        }
        try{
        ticketRepository.deleteById(id);}
        catch (EmptyResultDataAccessException e){
            throw  new ResponseStatusException(HttpStatus.NOT_FOUND, "No ticket for id: "+id);
        }
        CommonResponseModel commonResponseModel = new CommonResponseModel();
        commonResponseModel.setResponseMessage("Ticket deleted Successfully");
        return new ResponseEntity<>(commonResponseModel, HttpStatus.OK);
    }

    @PatchMapping("/ticket/{id}")
    public ResponseEntity<CommonResponseModel> updateTicket(@NotNull @RequestHeader("Authorization") String authenticationDetails, @PathVariable long id, @NotNull @RequestBody Ticket updateTicketRequest) {
        CommonUtil commonUtil = new CommonUtil(authenticationDetails);
        User user = userRepository.findByUserName(commonUtil.getUserName());
        CommonResponseModel commonResponseModel = new CommonResponseModel();
        if(user==null || !user.passwordMatch(commonUtil.getPassword())){
            commonResponseModel.setResponseMessage("Invalid session");
            return new ResponseEntity<>(commonResponseModel, HttpStatus.FORBIDDEN);
        }
        if (user.isCustomer()){
            commonResponseModel.setResponseMessage("User not authorized");
            return new ResponseEntity<>(commonResponseModel, HttpStatus.UNAUTHORIZED);
        }
        Optional<Ticket> ticket = ticketRepository.findById(id);
        if(!ticket.isPresent()){
            commonResponseModel.setResponseMessage("Ticket not found");
            return  new ResponseEntity<>(commonResponseModel, HttpStatus.NOT_FOUND);
        }
        ticketRepository.save(CommonUtil.updateTicketDetails(ticket.get(), updateTicketRequest));
        commonResponseModel.setResponseMessage("Ticket updated Successfully");
        return new ResponseEntity<>(commonResponseModel, HttpStatus.OK);
    }

    @PatchMapping("/ticket/{id}/assign/{agent}")
    public ResponseEntity<CommonResponseModel> assignAgent(@NotNull @RequestHeader("Authorization") String authenticationDetails, @PathVariable long id, @PathVariable(value = "agent") String agentUserName){
        CommonUtil commonUtil = new CommonUtil(authenticationDetails);
        User user = userRepository.findByUserName(commonUtil.getUserName());
        CommonResponseModel commonResponseModel = new CommonResponseModel();
        if(user==null || !user.passwordMatch(commonUtil.getPassword())){
            commonResponseModel.setResponseMessage("Invalid session");
            return new ResponseEntity<>(commonResponseModel, HttpStatus.FORBIDDEN);
        }
        if (user.isCustomer()){
            commonResponseModel.setResponseMessage("User not authorized");
            return new ResponseEntity<>(commonResponseModel, HttpStatus.UNAUTHORIZED);
        }

        User agent = userRepository.findByUserName(agentUserName);
        if(agent==null){
            commonResponseModel.setResponseMessage("Agent not found");
            return new ResponseEntity<>(commonResponseModel, HttpStatus.NOT_FOUND);
        }
        if(agent.isCustomer()){
            commonResponseModel.setResponseMessage("Assignment should happen with agent not customer");
            return new ResponseEntity<>(commonResponseModel, HttpStatus.BAD_REQUEST);
        }
        Optional<Ticket> ticket = ticketRepository.findById(id);
        if(!ticket.isPresent()){
            commonResponseModel.setResponseMessage("Ticket not found");
            return  new ResponseEntity<>(commonResponseModel, HttpStatus.NOT_FOUND);
        }
        ticket.get().setAssignedTo(agentUserName);
        ticketRepository.save(ticket.get());
        commonResponseModel.setResponseMessage("Successfully assigned Agent "+ agentUserName +" to "+id);
        return new ResponseEntity<>(commonResponseModel, HttpStatus.ACCEPTED);
    }

    @PostMapping("/ticket/{id}/response")
    public ResponseEntity<CommonResponseModel> addResponse(@NotNull @RequestHeader("Authorization") String authenticationDetails, @PathVariable long id,@Valid @RequestBody Response response){
        CommonUtil commonUtil = new CommonUtil(authenticationDetails);
        User user = userRepository.findByUserName(commonUtil.getUserName());
        CommonResponseModel commonResponseModel = new CommonResponseModel();
        if(user==null || !user.passwordMatch(commonUtil.getPassword())){
            commonResponseModel.setResponseMessage("Invalid session");
            return new ResponseEntity<>(commonResponseModel, HttpStatus.FORBIDDEN);
        }

        Optional<Ticket> ticket = ticketRepository.findById(id);
        if(!ticket.isPresent()){
            commonResponseModel.setResponseMessage("Ticket not found");
            return  new ResponseEntity<>(commonResponseModel, HttpStatus.NOT_FOUND);
        }
        response.setUser(user.getUserName());
        response.setTicket(ticket.get());
        List<Response> responses = ticket.get().getResponses();
        responses.add(response);
        ticket.get().setResponses(responses);
        ticketRepository.save(ticket.get());
        commonResponseModel.setResponseMessage("Successfully added response to ticket "+id);
        return new ResponseEntity<>(commonResponseModel, HttpStatus.ACCEPTED);
    }
}
