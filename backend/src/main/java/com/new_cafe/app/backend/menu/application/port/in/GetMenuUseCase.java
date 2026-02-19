package com.new_cafe.app.backend.menu.application.port.in;

import com.new_cafe.app.backend.dto.MenuDetailResponse;
import com.new_cafe.app.backend.dto.MenuImageListResponse;
import com.new_cafe.app.backend.dto.MenuListRequest;
import com.new_cafe.app.backend.dto.MenuListResponse;

public interface GetMenuUseCase {
    MenuListResponse getMenus(MenuListRequest request);
    MenuDetailResponse getMenu(Long id);
    MenuImageListResponse getMenuImages(Long id);
}
