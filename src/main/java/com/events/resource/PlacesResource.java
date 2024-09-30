package com.events.resource;

import com.events.entities.Places;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import jakarta.ws.rs.Path;

import org.jboss.resteasy.reactive.MultipartForm;
import org.jboss.resteasy.reactive.RestForm;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.List;

@Path("/api/places")
@Produces(MediaType.APPLICATION_JSON)
public class PlacesResource {

    // Get all places
    @GET
    public List<Places> getAllPlaces() {
        return Places.listAll();
    }

    // Get a place by ID
    @GET
    @Path("/{id}")
    public Places getPlaceById(@PathParam("id") Long id) {
        Places place = Places.findById(id);
        if (place == null) {
            throw new WebApplicationException("Place not found", Response.Status.NOT_FOUND);
        }
        return place;
    }

    // Create a new place with image upload
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Transactional
    public Response createPlace(@MultipartForm PlaceFormData formData, @Context UriInfo uriInfo) {

        // Validate form data
        if (formData.placeName == null || formData.placeName.isEmpty()) {
            throw new WebApplicationException("Place name is required", Response.Status.BAD_REQUEST);
        }

        Places place = new Places();
        place.setPlaceName(formData.placeName);
        place.setDescription(formData.description);
        place.setAbout(formData.about);
        place.setLocation(formData.location);

        // Handle image upload
        if (formData.image != null && formData.imageFileName != null) {
            try {
                String imagePath = saveImage(formData.image, formData.imageFileName);
                place.setImage(imagePath);
            } catch (IOException e) {
                throw new WebApplicationException("Image upload failed: " + e.getMessage(), Response.Status.INTERNAL_SERVER_ERROR);
            }
        } else {
            place.setImage(null); // Or set a default image path
        }

        place.persist();

        // UriBuilder builder = uriInfo.getAbsolutePathBuilder().path(Long.toString(place.getId()));
        return  Response.status(Response.Status.CREATED).entity(place).build();
    }

    // Update an existing place with optional image upload
    @PUT
    @Path("/{id}")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Transactional
    public Places updatePlace(@PathParam("id") Long id, @MultipartForm PlaceFormData formData) {
        Places place = Places.findById(id);
        if (place == null) {
            throw new WebApplicationException("Place not found", Response.Status.NOT_FOUND);
        }

        place.setPlaceName(formData.placeName);
        place.setDescription(formData.description);
        place.setAbout(formData.about);
        place.setLocation(formData.location);

        // Handle image upload if a new image is provided
        if (formData.image != null && formData.imageFileName != null) {
            try {
                String imagePath = saveImage(formData.image, formData.imageFileName);
                place.setImage(imagePath);
            } catch (IOException e) {
                throw new WebApplicationException("Image upload failed: " + e.getMessage(), Response.Status.INTERNAL_SERVER_ERROR);
            }
        }

        place.setUpdatedAt(LocalDateTime.now());
        place.persist();

        return place;
    }

    // Delete a place by ID
    @DELETE
    @Path("/{id}")
    @Transactional
    public Response deletePlace(@PathParam("id") Long id) {
        Places place = Places.findById(id);
        if (place == null) {
            throw new WebApplicationException("Place not found", Response.Status.NOT_FOUND);
        }
        place.delete();
        return Response.noContent().build();
    }

    // Helper method to save uploaded image
    private String saveImage(InputStream uploadedInputStream, String fileName) throws IOException {
        String uploadDir = "uploads/images"; // Define your upload directory
        java.nio.file.Path uploadPath = java.nio.file.Paths.get(uploadDir);

        // Create directories if they don't exist
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Sanitize file name and construct file path
        String sanitizedFileName = Paths.get(fileName).getFileName().toString();
        java.nio.file.Path filePath = uploadPath.resolve(sanitizedFileName);

        // Save the file to the server
        Files.copy(uploadedInputStream, filePath, StandardCopyOption.REPLACE_EXISTING);

        // Return the relative file path to store in the database
        return filePath.toString();
    }

    // Inner class to handle multipart form data
    public static class PlaceFormData {

        @RestForm
        public String placeName;

        @RestForm
        public String description;

        @RestForm
        public String about;

        @RestForm
        public String location;

        @RestForm("image")
        public InputStream image;

        @RestForm("imageFileName")
        public String imageFileName;
    }
}
