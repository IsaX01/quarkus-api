package com.events.resource;

import com.events.entities.Inventory;
import com.events.entities.InventoryCategory;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.time.LocalDateTime;
import java.util.List;

@Path("/api/inventories")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class InventoryResource {

    // Inventory CRUD Operations

    @GET
    public List<Inventory> getAllInventories() {
        return Inventory.listAll();
    }

    @GET
    @Path("/{id}")
    public Inventory getInventoryById(@PathParam("id") Long id) {
        Inventory inventory = Inventory.findById(id);
        if (inventory == null) {
            throw new WebApplicationException("Inventory not found", Response.Status.NOT_FOUND);
        }
        return inventory;
    }

    @POST
    @Transactional
    public Response createInventory(Inventory inventory) {
        if (inventory.getCategoryId() != null) {
            InventoryCategory category = InventoryCategory.findById(inventory.getCategoryId());
            if (category == null) {
                throw new WebApplicationException("Category not found", Response.Status.BAD_REQUEST);
            }
            inventory.setCategory(category);
        }
        inventory.persist();
        return Response.status(Response.Status.CREATED).entity(inventory).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Inventory updateInventory(@PathParam("id") Long id, Inventory inventory) {
        Inventory entity = Inventory.findById(id);
        if (entity == null) {
            throw new WebApplicationException("Inventory not found", Response.Status.NOT_FOUND);
        }

        entity.setName(inventory.getName());
        entity.setDescription(inventory.getDescription());
        entity.setStockQuantity(inventory.getStockQuantity());
        entity.setMaintenanceStatus(inventory.getMaintenanceStatus());
        entity.setIsAvailable(inventory.getIsAvailable());

        if (inventory.getCategoryId() != null) {
            InventoryCategory category = InventoryCategory.findById(inventory.getCategoryId());
            if (category == null) {
                throw new WebApplicationException("Category not found", Response.Status.BAD_REQUEST);
            }
            entity.setCategory(category);
        } else {
            entity.setCategory(null);
        }

        entity.setUpdatedAt(LocalDateTime.now());
        return entity;
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response deleteInventory(@PathParam("id") Long id) {
        Inventory entity = Inventory.findById(id);
        if (entity == null) {
            throw new WebApplicationException("Inventory not found", Response.Status.NOT_FOUND);
        }
        entity.delete();
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    // InventoryCategory CRUD Operations

    @GET
    @Path("/categories")
    public List<InventoryCategory> getAllCategories() {
        return InventoryCategory.listAll();
    }

    @GET
    @Path("/categories/{id}")
    public InventoryCategory getCategoryById(@PathParam("id") Long id) {
        InventoryCategory category = InventoryCategory.findById(id);
        if (category == null) {
            throw new WebApplicationException("Category not found", Response.Status.NOT_FOUND);
        }
        return category;
    }

    @POST
    @Path("/categories")
    @Transactional
    public Response createCategory(InventoryCategory category) {
        category.persist();
        return Response.status(Response.Status.CREATED).entity(category).build();
    }

    @PUT
    @Path("/categories/{id}")
    @Transactional
    public InventoryCategory updateCategory(@PathParam("id") Long id, InventoryCategory category) {
        InventoryCategory entity = InventoryCategory.findById(id);
        if (entity == null) {
            throw new WebApplicationException("Category not found", Response.Status.NOT_FOUND);
        }

        entity.setCategoryName(category.getCategoryName());
        entity.setUpdatedAt(LocalDateTime.now());
        return entity;
    }

    @DELETE
    @Path("/categories/{id}")
    @Transactional
    public Response deleteCategory(@PathParam("id") Long id) {
        InventoryCategory entity = InventoryCategory.findById(id);
        if (entity == null) {
            throw new WebApplicationException("Category not found", Response.Status.NOT_FOUND);
        }
        entity.delete();
        return Response.status(Response.Status.NO_CONTENT).build();
    }
}