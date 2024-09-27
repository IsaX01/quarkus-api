package com.events.resource;

import jakarta.annotation.security.RolesAllowed;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import java.util.List;

import com.events.entities.Places;
import com.events.entities.Review;
import com.events.entities.User;

@Path("/reviews")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ReviewResource {

    @POST
    @Path("/{placeId}")
    @RolesAllowed({"user", "admin"})
    @Transactional
    public Response addReview(@PathParam("placeId") Long placeId, Review review, @Context SecurityContext securityContext) {
    
        Places place = Places.findById(placeId);
        if (place == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        String username = securityContext.getUserPrincipal().getName();
        User user = User.find("username", username).firstResult(); // Asumiendo que tienes una entidad User y este método

        if (user == null) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        review.setPlace(place);
        review.setUser(user);
        review.setOpinion(review.getOpinion());
        review.setStars(review.getStars());
        review.persist();

        return Response.status(Response.Status.CREATED).entity(review).build();
    }

    @GET
    @Path("/{placeId}")
    public Response getReviews(@PathParam("placeId") Long placeId) {
        Places place = Places.findById(placeId);
        if (place == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        List<Review> reviews = Review.list("place", place);
        return Response.ok(reviews).build();
    }

    @GET
    @Path("/{placeId}/average-rating")
    public Response getAverageRating(@PathParam("placeId") Long placeId) {
        Places place = Places.findById(placeId);
        if (place == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        double averageRating = place.getAverageRating();
        long reviewCount = place.getReviewCount();
        return Response.ok(new RatingResponse(averageRating, reviewCount)).build();
    }

    public static class RatingResponse {
        public double averageRating;
        public long reviewCount;

        public RatingResponse(double averageRating, long reviewCount) {
            this.averageRating = averageRating;
            this.reviewCount = reviewCount;
        }

        public double getAverageRating() {
            return averageRating;
        }
    
        public long getReviewCount() {
            return reviewCount;
        }
    }
}
