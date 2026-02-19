package com.new_cafe.app.backend.menu.adapter.in.web;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.new_cafe.app.backend.menu.application.port.in.ManageMenuUseCase;
import com.new_cafe.app.backend.menu.domain.Menu; // DTO 사용 권장하지만 일단 유지

@RestController
@RequestMapping("/api/admin/menus") // 관리자용 URL
public class AdminMenuController {

    private final ManageMenuUseCase manageMenuUseCase;

    public AdminMenuController(ManageMenuUseCase manageMenuUseCase) {
        this.manageMenuUseCase = manageMenuUseCase;
    }

    // 메뉴 생성
    @PostMapping
    public String newMenu(Menu menu) {
        // TODO: Map to DTO -> manageMenuUseCase.createMenu(request)
        return "newMenu";
    }

    // 메뉴 수정
    @PutMapping("/{id}")
    public String editMenu(Menu menu) {
        // TODO: Map to DTO -> manageMenuUseCase.updateMenu(request)
        return "editMenu";
    }

    // 메뉴 삭제
    @DeleteMapping("/{id}")
    public String deleteMenu(@PathVariable Long id) {
        manageMenuUseCase.deleteMenu(id);
        return "deleteMenu";
    }
}
