package com.new_cafe.app.backend.menu.domain;

import java.time.LocalDateTime;
import java.util.List;
import com.new_cafe.app.backend.entity.Category;     // 기존 엔티티 참조 유지
import com.new_cafe.app.backend.entity.MenuImage;    // 기존 엔티티 참조 유지

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
    private Integer sortOrder;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // 연관 관계 (나중에는 이것도 분리하는 것이 좋음/Category ID만 참조 등)
    private Category category;
    private List<MenuImage> images;
}
