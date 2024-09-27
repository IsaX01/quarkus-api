package com.events.repositories;


import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import com.events.entities.Places;

@ApplicationScoped
public class PlacesRepository implements PanacheRepository<Places>{

    public Places findById(long id){
        return find("id", id).firstResult();
    }
}
