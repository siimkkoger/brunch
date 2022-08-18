package com.example.brunch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class EventController {

    Logger logger = LoggerFactory.getLogger(EventController.class);

    private final EventRepository repository;

    public EventController(EventRepository repository) {
        this.repository = repository;
    }

    // TODO : set up security and authentication
    @GetMapping("/hello")
    public String getEventByID() {
        return "Hello";
    }

    @GetMapping("/rest/event/get/{id}")
    public Event getEventByID(@PathVariable("id") long id) {
        return repository.getReferenceById(id);
    }

    @GetMapping("/rest/event/getAll")
    public List<Event> getAllEvents() {
        return repository.findAll();
    }

    @PostMapping("/rest/event/create")
    public Event createEvent(@RequestBody Event emp) {
        // TODO : don't save info sent from frontend
        repository.save(emp);
        return emp;
    }

    @DeleteMapping("/rest/event/delete/{id}")
    public boolean deleteEventByID(@PathVariable("id") long id) {
        try {
            repository.deleteById(id);
            return true;
        } catch (Exception e) {
            // TODO : set up logging
            logger.info(String.format("Delete Event (id:%d) failed with message: %s", id,
                    e.getMessage()));
        }
        return false;
    }
}
