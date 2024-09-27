package com.events.resource;

import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import org.jboss.resteasy.reactive.multipart.FileUpload;

import com.events.entities.Places;
import com.events.entities.PlacesImages;
import com.events.helpers.PlaceFormData;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@jakarta.ws.rs.Path("/places")
@Produces(MediaType.APPLICATION_JSON)
public class PlacesResource {

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Transactional
    public Response createPlace(PlaceFormData formData) {
        // Crear el lugar
        Places place = new Places();
        place.setPlaceName(formData.placeName);
        place.setDescription(formData.description);
        place.setAbout(formData.about);

        // Persistir el lugar
        place.persist();

        // Manejar las imágenes
        List<PlacesImages> imageEntities = new ArrayList<>();
        if (formData.images != null) {
            for (FileUpload file : formData.images) {
                String fileName = file.fileName();

                // Guardar la imagen en el sistema de archivos
                File uploadDir = new File("images/");
                if (!uploadDir.exists()) {
                    uploadDir.mkdirs();
                }
                File targetFile = new File(uploadDir, fileName);
                try {
                    Path source = file.uploadedFile(); // Path del archivo subido
                    Path target = targetFile.toPath(); // Path del archivo de destino
                
                    Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException e) {
                    e.printStackTrace();
                    return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error al guardar la imagen").build();
                }

                // Crear la entidad Image y asociarla al lugar
                PlacesImages image = new PlacesImages();
                image.setFileName(fileName);
                image.setPlace(place);
                image.setUploadTime(LocalDateTime.now());
                image.persist();

                imageEntities.add(image);
            }
        }

        place.setImages(imageEntities);

        return Response.status(Response.Status.CREATED).entity(place).build();
    }
}