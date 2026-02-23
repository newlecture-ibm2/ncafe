package com.new_cafe.app.backend.admin.menu.application.port.in;

import com.new_cafe.app.backend.admin.menu.application.dto.MenuListResponse;
import com.new_cafe.app.backend.admin.menu.application.dto.MenuResponse;

public interface ManageMenuUseCase {
    MenuResponse createMenu(CreateMenuCommand command);
    MenuResponse updateMenu(UpdateMenuCommand command);
    void deleteMenu(Long id);
    MenuResponse getMenu(Long id);
    MenuListResponse getMenus(String query, Long categoryId);

    record CreateMenuCommand(
            String korName,
            String engName,
            String description,
            Integer price,
            Long categoryId
    ) {}

    record UpdateMenuCommand(
            Long id,
            String korName,
            String engName,
            String description,
            Integer price,
            Long categoryId,
            Boolean isAvailable
    ) {}
}
