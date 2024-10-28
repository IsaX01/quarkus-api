package com.events.resource;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
// import jakarta.resource.spi.work.SecurityContext;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import java.util.List;
import java.security.Principal;
import java.time.format.DateTimeFormatter;

import com.events.entities.Event;
import com.events.entities.User;
import com.events.entities.Places;
import com.events.repositories.EventRepository;
import com.events.repositories.PlacesRepository;
import com.events.repositories.UserRepository;
import com.events.services.NotificationClient;
import com.events.services.NotificationData;
import org.jboss.logging.Logger;
import io.vertx.core.json.JsonObject;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@Path("/api/events")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EventResource {

    @Inject
    EventRepository eventRepository;

    @Inject
    UserRepository userRepository;

    @Inject
    PlacesRepository placesRepository;

    @Inject
    @RestClient
    NotificationClient notificationClient;

    private static final Logger LOG = Logger.getLogger(EventResource.class);
    @GET
    // @RolesAllowed({"admin", "event_manager"})
    public List<Event> getAllEvents() {
        return eventRepository.listAll();
    }

    @GET
    @Path("{id}")
    // @RolesAllowed({"admin", "event_manager", "user"})
    public Event getEvent(@PathParam("id") Long id) {
        Event event = eventRepository.findById(id);
        if (event == null) {
            throw new WebApplicationException("Event not found", 404);
        }
        return event;
    }

    @POST 
    @Transactional
    // @RolesAllowed({"admin", "event_manager", "user"})
    public Response createEvent(Event event, @Context SecurityContext securityContext) {
        // Principal principal = securityContext.getUserPrincipal();
        User user = userRepository.findById(event.getUserId());
        event.setCreatedBy(user);
        Places place = placesRepository.findById(event.getPlaceId());
        event.setPlace(place);
        event.setPlaceId(event.getPlaceId());
        
        NotificationData notificationData = new NotificationData();
        notificationData.setName(event.getName());
        notificationData.setDescription(event.getDescription());
        notificationData.setPlace(place.getPlaceName());
        notificationData.setDate(event.getStartDate().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
        notificationData.setStartDate(event.getStartDate().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
        notificationData.setEndDate(event.getEndDate().format( DateTimeFormatter.ofPattern("yyyyMMdd")));
        notificationData.setDeviceToken(user.getDeviceToken()); 
        LOG.infof("Token %s", user.getDeviceToken());
        try {
            notificationClient.sendNotification(notificationData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        eventRepository.persist(event);
        return Response.status(Response.Status.CREATED).entity(event).build();
    }

    @PUT
    @Path("{id}")
    @Transactional
    // @RolesAllowed({"admin", "event_manager", "user"})
    public Response updateEvent(@PathParam("id") Long id, Event event) {
        Event entity = eventRepository.findById(id);
        if (entity == null) {
            throw new WebApplicationException("Event not found", 404);
        }
        entity.setName(event.getName());
        entity.setDescription(event.getDescription());
        // Update other fields...
        eventRepository.persist(entity);
        return Response.ok(entity).build();
    }

    @DELETE
    @Path("{id}")
    @Transactional
    // @RolesAllowed({"admin", "event_manager", "user"})
    public Response deleteEvent(@PathParam("id") Long id) {
        Event entity = eventRepository.findById(id);
        if (entity == null) {
            throw new WebApplicationException("Event not found", 404);
        }
        eventRepository.delete(entity);
        return Response.noContent().build();
    }
}
