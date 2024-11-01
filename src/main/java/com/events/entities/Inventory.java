package com.events.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

@Entity
@Table(name = "inventory")
public class Inventory extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = true)
    private String description;

    @Column(name = "stock_quantity", nullable = false)
    private Integer stockQuantity;

    @Column(name = "maintenance_status")
    private String maintenanceStatus;

    @Column(name = "is_available")
    private Boolean isAvailable;

    private Long categoryId;

    @ManyToOne 
    @JoinColumn(name = "category_id")
    private InventoryCategory category;

    @Column(name ="created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    public void setUpdatedAt(LocalDateTime updateAt){
        this.updatedAt = updateAt;
    }

    public Long getId(){
        return this.id;
    }

    public void setId(Long id){
        this.id = id;
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

    public Integer getStockQuantity(){
        return this.stockQuantity;
    }

    public void setStockQuantity(Integer stockQuantity){
        this.stockQuantity = stockQuantity;
    }

    public String getMaintenanceStatus(){
        return this.maintenanceStatus;
    }

    public void setMaintenanceStatus(String maintenanceStatus){
        this.maintenanceStatus = maintenanceStatus;
    }
    
    public Boolean getIsAvailable(){
        return this.isAvailable;
    }

    public void setIsAvailable(Boolean isAvailable){
        this.isAvailable = isAvailable;
    }

    public InventoryCategory getCategory() {
        return this.category;
    }

    public void setCategory(InventoryCategory category) {
        this.category = category;
    }
    
    public String getCategoryName() {
        return getCategory().getCategoryName();
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }
    
}
