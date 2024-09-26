package com.events.resource;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.UUID;
import com.events.repositories.*;
import com.events.entities.*;
import com.events.resource.*;
import com.events.services.*;


@Path("/api/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {

    @Inject
    UserRepository userRepository;

    @GET
    @RolesAllowed({"admin"})
    public List<User> getAllUsers() {
        return userRepository.listAll();
    }

    @GET
    @Path("{id}")
    @RolesAllowed({"admin", "event_manager"})
    public User getUser(@PathParam("id") UUID id) {
        return userRepository.findById(id);
    }

    @POST
    @Transactional
    @RolesAllowed({"admin"})
    public Response createUser(User user) {
        user.setPasswordHash(PasswordUtils.hashPassword(user.getPasswordHash()));
        userRepository.persist(user);
        return Response.status(Response.Status.CREATED).build();
    }

    @PUT
    @Path("{id}")
    @Transactional
    @RolesAllowed({"admin"})
    public Response updateUser(@PathParam("id") UUID id, User user) {
        User entity = userRepository.findById(id);
        if (entity == null) {
            throw new WebApplicationException("User not found", 404);
        }
        entity.setUsername(user.getUsername());
        entity.setEmail(user.getEmail());
        // Update other fields...
        userRepository.persist(entity);
        return Response.ok().build();
    }

    @DELETE
    @Path("{id}")
    @Transactional
    @RolesAllowed({"admin"})
    public Response deleteUser(@PathParam("id") UUID id) {
        User entity = userRepository.findById(id);
        if (entity == null) {
            throw new WebApplicationException("User not found", 404);
        }
        userRepository.delete(entity);
        return Response.noContent().build();
    }
}
