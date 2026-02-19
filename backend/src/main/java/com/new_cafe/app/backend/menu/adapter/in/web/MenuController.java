package com.new_cafe.app.backend.menu.adapter.in.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.new_cafe.app.backend.dto.MenuDetailResponse;
import com.new_cafe.app.backend.dto.MenuImageListResponse;
import com.new_cafe.app.backend.dto.MenuListRequest;
import com.new_cafe.app.backend.dto.MenuListResponse;
import com.new_cafe.app.backend.menu.application.port.in.GetMenuUseCase;

@RestController
@RequestMapping("/api/menus") // 사용자용 URL (관리자 접두어 없음)
public class MenuController {

    private final GetMenuUseCase getMenuUseCase;

    public MenuController(GetMenuUseCase getMenuUseCase) {
        this.getMenuUseCase = getMenuUseCase;
    }

    // 목록 조회
    @GetMapping
    public MenuListResponse getMenus(MenuListRequest request) {
        return getMenuUseCase.getMenus(request);
    }

    // 상세 조회
    @GetMapping("/{id}")
    public MenuDetailResponse getMenu(@PathVariable Long id) {
        return getMenuUseCase.getMenu(id);
    }

    // 메뉴 이미지 목록 조회
    @GetMapping("/{id}/menu-images")
    public MenuImageListResponse getMenuImages(@PathVariable Long id) {
        return getMenuUseCase.getMenuImages(id);
    }
}
