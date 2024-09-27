package com.events.services;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import jakarta.inject.Inject;
import java.io.IOException;

import com.events.repositories.RoleRepository;
import com.events.entities.Role;

public class RoleDeserializer extends JsonDeserializer<Role> {

    @Inject
    RoleRepository roleRepository;

    @Override
    public Role deserialize(JsonParser p, DeserializationContext ctxt) 
            throws IOException, JsonProcessingException {
        Long roleId = Long.parseLong(p.getText());
        return roleRepository.findById(roleId);
    }
}