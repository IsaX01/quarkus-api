package com.events.repositories;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import com.events.entities.Role;

@ApplicationScoped
public class RoleRepository implements PanacheRepository<Role>{

    public Role findById(long id){
        return find("id", id).firstResult();
    }
}
