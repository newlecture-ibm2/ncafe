package com.new_cafe.app.backend.admin.menu.application.dto;

import java.time.LocalDateTime;

import com.new_cafe.app.backend.admin.menu.domain.Menu;

public record MenuResponse(
        Long id,
        String korName,
        String engName,
        String description,
        Integer price,
        Long categoryId,
        Boolean isAvailable,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static MenuResponse from(Menu menu) {
        return new MenuResponse(
                menu.getId(),
                menu.getKorName(),
                menu.getEngName(),
                menu.getDescription(),
                menu.getPrice(),
                menu.getCategoryId(),
                menu.getIsAvailable(),
                menu.getCreatedAt(),
                menu.getUpdatedAt()
        );
    }
}
