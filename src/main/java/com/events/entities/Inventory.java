package com.events.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

@Entity
@Table(name = "inventory")
public class Inventory extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "serial_number")
    private String serialNumber;

    @Column(name = "stock_quantity")
    private Integer stockQuantity;

    @Column(name = "maintenance_status")
    private String maintenanceStatus;

    @Column(name = "is_available")
    private Boolean isAvailable;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private ProductCategory category;

    @Column(name = "location")
    private String location;

    @Column(name ="created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();
}