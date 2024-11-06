package com.jivitHealcare.service;


import com.jivitHealcare.Entity.Ticket;
import com.jivitHealcare.Repo.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TicketService {
    @Autowired
    private TicketRepository ticketRepository;
    public Ticket ticketRaise(Ticket ticket) {
       return ticketRepository.save(ticket);
    }

    public List<Ticket> ticketRaiseInfo() {
        return ticketRepository.findAll();
    }
}
