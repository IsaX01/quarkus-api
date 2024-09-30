package com.events.resource;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;

@Path("/images")
public class ImageResource {

    @GET
    @Path("/{filename}")
    @Produces({"image/jpeg", "image/png", "image/gif"})
    public Response getImage(@PathParam("filename") String filename) {
        String uploadDir = "uploads/images";
        java.nio.file.Path filePath = Paths.get(uploadDir).resolve(filename).normalize();

        // Security check
        if (!filePath.startsWith(Paths.get(uploadDir).toAbsolutePath())) {
            throw new WebApplicationException("Invalid file path", Response.Status.BAD_REQUEST);
        }

        File file = filePath.toFile();
        if (!file.exists()) {
            throw new WebApplicationException("Image not found", Response.Status.NOT_FOUND);
        }

        String mimeType;
        try {
            mimeType = Files.probeContentType(filePath);
        } catch (IOException e) {
            mimeType = "application/octet-stream";
        }

        return Response.ok(file)
                .type(mimeType)
                .build();
    }
}
