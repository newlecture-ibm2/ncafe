package com.new_cafe.app.backend.entity;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Menu {
    private Long id;
    private String korName;
    private String engName;
    private String description;
    private Integer price;
    private Long categoryId;
    private Boolean isAvailable;
    private Integer sortOrder; // Added field
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Category category;
    private List<MenuImage> images; // Added field

}
