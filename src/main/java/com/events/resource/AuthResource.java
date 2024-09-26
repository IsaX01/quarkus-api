package com.events.resource;

import io.smallrye.jwt.build.Jwt;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.time.Duration;
import java.util.HashSet;
import java.util.Set;
import com.events.repositories.*;
import com.events.entities.*;
import com.events.services.*;

@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthResource {

    @Inject
    UserRepository userRepository;

    @POST
    @Path("/login")
    public Response login(Credentials credentials) {
        User user = userRepository.findByUsername(credentials.username);

        if (user != null && verifyPassword(credentials.password, user.getPasswordHash())) {
            String token = Jwt.issuer("https://X-Events.com")
                    .upn(user.getUsername())
                    .groups(getRoles(user))
                    .expiresIn(Duration.ofHours(24))
                    .sign();
            return Response.ok(new TokenResponse(token)).build();
        } else {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }

    private boolean verifyPassword(String password, String passwordHash) {
        // Implement password verification logic (e.g., using bcrypt)
        return PasswordUtils.verifyPassword(password, passwordHash);
    }

    private Set<String> getRoles(User user) {
        Set<String> roles = new HashSet<>();
        roles.add(user.getRole().getRoleName());
        return roles;
    }

    // DTO Classes
    public static class Credentials {
        public String username;
        public String password;
    }

    public static class TokenResponse {
        public String token;
        public TokenResponse(String token) {
            this.token = token;
        }
    }
}
