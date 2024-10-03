package com.events.repositories;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import com.events.entities.Event;

@ApplicationScoped
public class EventRepository implements PanacheRepository<Event> {
    public Event findById(long id){
        return find("id", id).firstResult();
    }
}
