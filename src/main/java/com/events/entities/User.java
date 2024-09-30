package com.events.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

import com.events.services.RoleDeserializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

@Entity
@Table(name = "users")
public class User extends PanacheEntityBase { 

    @Id
    @GeneratedValue
    private UUID id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    @JsonProperty("password")
    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    private Long roleId;

    public UUID getId(){
        return this.id;
     }
 
     public void setId(UUID id){
         this.id = id;
     }

    public String getUsername(){
       return this.username;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public String getEmail(){
        return this.email;
    }

    
    public void setEmail(String email){
        this.email = email;
    }

    public String getPasswordHash(){
        return this.passwordHash;
    }
    
    public void setPasswordHash(String passwordHash){
        this.passwordHash = passwordHash;
    }

    public Role getRole() {
        return this.role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
    
    public String getRoleName() {
        return getRole().getRoleName();
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Boolean getIsActive(){
        return this.isActive;
    }

    public void setIsActive(Boolean isActive){
        this.isActive = isActive;
    }
}
