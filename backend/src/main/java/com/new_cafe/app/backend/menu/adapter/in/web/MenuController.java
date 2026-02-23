package com.new_cafe.app.backend.menu.adapter.in.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.new_cafe.app.backend.menu.application.dto.MenuListResponse;
import com.new_cafe.app.backend.menu.application.dto.MenuResponse;
import com.new_cafe.app.backend.menu.application.port.in.GetMenuUseCase;

@RestController
@RequestMapping("/api/menus")
public class MenuController {

    private final GetMenuUseCase getMenuUseCase;

    public MenuController(GetMenuUseCase getMenuUseCase) {
        this.getMenuUseCase = getMenuUseCase;
    }

    @GetMapping
    public ResponseEntity<MenuListResponse> getMenus(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String query) {
        MenuListResponse response = getMenuUseCase.getMenus(categoryId, query);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MenuResponse> getMenu(@PathVariable Long id) {
        MenuResponse response = getMenuUseCase.getMenu(id);
        return ResponseEntity.ok(response);
    }
}
