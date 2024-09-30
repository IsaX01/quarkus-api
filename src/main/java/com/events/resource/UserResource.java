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

    @Inject
    RoleRepository roleRepository;

    @GET
    // @RolesAllowed({"admin"})
    public List<User> getAllUsers() {
        return userRepository.listAll();
    }

    @GET
    @Path("{id}")
    // @RolesAllowed({"admin", "event_manager"})
    public User getUser(@PathParam("id") UUID id) {
        return userRepository.findById(id);
    }

    @POST
    @Transactional
    // @RolesAllowed({"admin"})
    public Response createUser(User user) {
        Role role = roleRepository.findById(user.getRoleId());
        if (role == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("RoleId invalid").build();
        }
    
        user.setUsername(user.getUsername());
        user.setEmail(user.getEmail());
        user.setFirstName(user.getFirstName());
        user.setLastName(user.getLastName());
        user.setRole(role);
    
        user.setPasswordHash(PasswordUtils.hashPassword(user.getPasswordHash()));
        userRepository.persist(user);
    
        return Response.status(Response.Status.CREATED).entity(user).build();
    }

    @PUT
    @Path("{id}")
    @Transactional
    // @RolesAllowed({"admin"})
    public Response updateUser(@PathParam("id") UUID id, User user) {
        User entity = userRepository.findById(id);
        if (entity == null) {
            throw new WebApplicationException("User not found", 404);
        }
        Long roleId;
        try {
            roleId = Long.parseLong(user.getRoleId().toString());
        } catch (NumberFormatException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("RoleId must be a number").build();
        }

        Role role = roleRepository.findById(roleId);
        if (role == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("RoleId invalid").build();
        }
        entity.setUsername(user.getUsername());
        entity.setEmail(user.getEmail());
        entity.setFirstName(user.getFirstName());
        entity.setLastName(user.getLastName());
        entity.setRole(role);

        userRepository.persist(entity);
        return Response.ok().entity(entity).build();
    }

    @DELETE
    @Path("{id}")
    @Transactional
    // @RolesAllowed({"admin"})
    public Response deleteUser(@PathParam("id") UUID id) {
        User entity = userRepository.findById(id);
        if (entity == null) {
            throw new WebApplicationException("User not found", 404);
        }
        userRepository.delete(entity);
        return Response.noContent().build();
    }
}
