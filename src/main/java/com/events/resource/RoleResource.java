package com.events.resource;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import com.events.entities.Role;


@Path("/api/roles")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RoleResource {
    @POST
    @Transactional
     public Response createRole(Role role) {
        role.persist();
        return Response.status(Response.Status.CREATED).entity(role).build();
    }
}
