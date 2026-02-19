package com.new_cafe.app.backend.menu.application.port.in;

import com.new_cafe.app.backend.dto.MenuCreateRequest;
import com.new_cafe.app.backend.dto.MenuCreateResponse;
import com.new_cafe.app.backend.dto.MenuUpdateRequest;
import com.new_cafe.app.backend.dto.MenuUpdateResponse;

public interface ManageMenuUseCase {
    MenuCreateResponse createMenu(MenuCreateRequest request);
    MenuUpdateResponse updateMenu(MenuUpdateRequest request);
    void deleteMenu(Long id);
}
