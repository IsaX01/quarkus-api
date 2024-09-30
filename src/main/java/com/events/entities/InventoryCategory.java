package com.events.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

@Entity
@Table(name = "inventory_category")
public class InventoryCategory extends PanacheEntityBase {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String category;

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

     public String getCategory(){
        return this.category;
     }
 
     public void setCategory(String category){
         this.category = category;
     }

    public InventoryCategory() {
    }

    public String getCategoryName() {
        return category;
    }

    public void setCategoryName(String category) {
        this.category = category;
    }
}
