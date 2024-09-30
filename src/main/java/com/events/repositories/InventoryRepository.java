package com.events.repositories;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import com.events.entities.Inventory;

@ApplicationScoped
public class InventoryRepository implements PanacheRepository<Inventory>{
        public Inventory findById(long id){
            return find("id", id).firstResult();
        }
}
