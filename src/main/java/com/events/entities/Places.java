package com.events.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;


@Entity
@Table(name = "places")
public class Places extends PanacheEntityBase{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "place_name", nullable = false)
    private String placeName;

    @Column(nullable = true)
    private String description;

    @Column(nullable = true)
    private String about;

    @Column(name = "location")
    private String location;

    @Column(nullable = true)
    private String image;

    @Column(nullable = true)
    private String image_path;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    @OneToMany(mappedBy = "place", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonIgnore
    private List<Review> reviews;

    public void setUpdatedAt(LocalDateTime updateAt){
        this.updatedAt = updateAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    @JsonIgnore
    public double getAverageRating() {
        return reviews.stream()
        .mapToInt(Review::getStars)
        .average()
        .orElse(0.0); 
    }

    @JsonIgnore
    public long getReviewCount() {
        return reviews.size(); 
    }

    public String getImage() {
        return image;
    }
    
    public void setImage(String image) {
        this.image = image;
    }

    public String getLocation() {
        return location;
    }
    
    public void setLocation(String location) {
        this.location = location;
    }

}
