package com.new_cafe.app.backend.admin.menu.adapter.in.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.new_cafe.app.backend.admin.menu.application.dto.MenuListResponse;
import com.new_cafe.app.backend.admin.menu.application.dto.MenuResponse;
import com.new_cafe.app.backend.admin.menu.application.port.in.ManageMenuUseCase;
import com.new_cafe.app.backend.admin.menu.application.port.in.ManageMenuUseCase.CreateMenuCommand;
import com.new_cafe.app.backend.admin.menu.application.port.in.ManageMenuUseCase.UpdateMenuCommand;

@RestController("adminMenuController")
@RequestMapping("/api/admin/menus")
public class MenuController {

    private final ManageMenuUseCase manageMenuUseCase;

    public MenuController(ManageMenuUseCase manageMenuUseCase) {
        this.manageMenuUseCase = manageMenuUseCase;
    }

    @PostMapping
    public ResponseEntity<MenuResponse> createMenu(@RequestBody CreateMenuCommand command) {
        MenuResponse response = manageMenuUseCase.createMenu(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<MenuListResponse> getMenus(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) Long categoryId) {
        MenuListResponse response = manageMenuUseCase.getMenus(query, categoryId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MenuResponse> getMenu(@PathVariable Long id) {
        MenuResponse response = manageMenuUseCase.getMenu(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MenuResponse> updateMenu(
            @PathVariable Long id,
            @RequestBody UpdateMenuCommand command) {
        UpdateMenuCommand commandWithId = new UpdateMenuCommand(
                id,
                command.korName(),
                command.engName(),
                command.description(),
                command.price(),
                command.categoryId(),
                command.isAvailable()
        );
        MenuResponse response = manageMenuUseCase.updateMenu(commandWithId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMenu(@PathVariable Long id) {
        manageMenuUseCase.deleteMenu(id);
        return ResponseEntity.noContent().build();
    }
}
