package com.events.repositories;

import com.events.entities.InventoryCategory;
import jakarta.enterprise.context.ApplicationScoped;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

@ApplicationScoped
public class InventoryCategoryRepository implements PanacheRepository<InventoryCategory> {
    
    public InventoryCategory findById(long id){
        return find("id", id).firstResult();
    }
}
