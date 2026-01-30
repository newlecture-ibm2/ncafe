package com.new_cafe.app.backend.entity;

import java.time.LocalDateTime;

public class Menu {
    private Long id;
    private String korName;
    private String engName;
    private String description;
    private String price;
    private Long categoryId;
    private Boolean isAvailable;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Category category;
    // private List<MenuImage> images;

    public Menu() {
    }

    public Menu(Long id, String korName, String engName, String description, String price, Long categoryId,
            Boolean isAvailable, LocalDateTime createdAt, LocalDateTime updatedAt, Category category) {
        this.id = id;
        this.korName = korName;
        this.engName = engName;
        this.description = description;
        this.price = price;
        this.categoryId = categoryId;
        this.isAvailable = isAvailable;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.category = category;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKorName() {
        return korName;
    }

    public void setKorName(String korName) {
        this.korName = korName;
    }

    public String getEngName() {
        return engName;
    }

    public void setEngName(String engName) {
        this.engName = engName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Boolean getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(Boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String toString() {
        return "Menu [id=" + id + ", korName=" + korName + ", engName=" + engName + ", description=" + description
                + ", price=" + price + ", categoryId=" + categoryId + ", isAvailable=" + isAvailable + ", createdAt="
                + createdAt + ", updatedAt=" + updatedAt + "]";
    }

}
