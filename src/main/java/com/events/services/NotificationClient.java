package com.events.services;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.core.MediaType;

@Path("/notify")
@RegisterRestClient(configKey = "notification-api")
public interface NotificationClient {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    void sendNotification(NotificationData notificationData);
}
