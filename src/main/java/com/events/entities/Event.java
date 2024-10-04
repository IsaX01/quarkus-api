package com.events.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

@Entity
@Table(name = "events")
public class Event extends PanacheEntityBase{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = true)
    private String description;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "start_time")
    private LocalTime startTime;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "end_time")
    private LocalTime endTime;

    @Column(name = "capacity")
    private Integer capacity;

    @Column(name = "event_type")
    private String eventType;

    @Column(name = "status")
    private String status = "scheduled";

    @ManyToOne
    @JoinColumn(name = "created_by")
    private User createdBy;

    @ManyToOne
    @JoinColumn(name = "place_id")
    private Places place;

    private long placeId;

    private UUID userId;
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    public UUID getUserId() {
        return this.userId;
    }

    public long getPlaceId(){
        return this.placeId;
    }
 
    public void setPlaceId (long placeId){
        this.placeId = placeId;
    }

    public void setPlace (Places place){
        this.place = place;
    }

    public void setCreatedBy(User createdBy){
        this.createdBy = createdBy;
    }

    public String getName(){
        return this.name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getDescription(){
        return this.description;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public LocalTime getStartTime(){
        return this.startTime;
    }

    public void setStartTime(LocalTime startTime){
        this.startTime = startTime;
    }

    public LocalTime getEndTime(){
        return this.endTime;
    }

    public void setEndTime(LocalTime endTime){
        this.endTime = endTime;
    }

    
    public LocalDate getEndDate(){
        return this.endDate;
    }

    public void setEndDate(LocalDate endDate){
        this.endDate = endDate;
    }

        
    public LocalDate getStartDate(){
        return this.startDate;
    }

    public void setStartDate(LocalDate startDate){
        this.startDate = startDate;
    }

    public Integer getCapacity(){
        return this.capacity;
    }

    public void setCapacity(Integer capacity){
        this.capacity = capacity;
    }

    public String getEventType(){
        return this.eventType;
    }

    public void setEventType(String eventType){
        this.eventType = eventType;
    }


}
