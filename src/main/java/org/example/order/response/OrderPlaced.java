package org.example.order.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class OrderPlaced {

    private List<Ingredient> ingredients;

    @SerializedName("_id")
    private String id;
    private Owner owner;
    private String status;
    private String name;
    private String createdAt;
    private String updatedAt;
    private Integer number;
    private Integer Price;

    public OrderPlaced() {
    }

    public OrderPlaced(List<Ingredient> ingredients, String id, Owner owner, String status, String name, String createdAt, String updatedAt, Integer number, Integer price) {
        this.ingredients = ingredients;
        this.id = id;
        this.owner = owner;
        this.status = status;
        this.name = name;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.number = number;
        Price = price;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public List<String> getListOfIngredientsIds() {
        List<String> ids = new ArrayList<>();
        ingredients.forEach(ingredient -> ids.add(ingredient.getId()));

        return ids;
    }

    public String getId() {
        return id;
    }

    public void set_id(String id) {
        this.id = id;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Integer getPrice() {
        return Price;
    }

    public void setPrice(Integer price) {
        Price = price;
    }
}
