package com.new_cafe.app.backend.admin.menu.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.new_cafe.app.backend.admin.menu.application.dto.MenuListResponse;
import com.new_cafe.app.backend.admin.menu.application.dto.MenuResponse;
import com.new_cafe.app.backend.admin.menu.application.port.in.ManageMenuUseCase;
import com.new_cafe.app.backend.admin.menu.application.port.out.MenuPort;
import com.new_cafe.app.backend.admin.menu.domain.Menu;

@Service
@Transactional
public class MenuService implements ManageMenuUseCase {

    private final MenuPort menuPort;

    public MenuService(MenuPort menuPort) {
        this.menuPort = menuPort;
    }

    @Override
    public MenuResponse createMenu(CreateMenuCommand command) {
        Menu menu = Menu.create(
                command.korName(),
                command.engName(),
                command.description(),
                command.price(),
                command.categoryId()
        );
        Menu saved = menuPort.save(menu);
        return MenuResponse.from(saved);
    }

    @Override
    public MenuResponse updateMenu(UpdateMenuCommand command) {
        Menu menu = menuPort.findById(command.id())
                .orElseThrow(() -> new IllegalArgumentException("Menu not found: " + command.id()));

        menu.update(
                command.korName(),
                command.engName(),
                command.description(),
                command.price(),
                command.categoryId(),
                command.isAvailable()
        );

        Menu saved = menuPort.save(menu);
        return MenuResponse.from(saved);
    }

    @Override
    public void deleteMenu(Long id) {
        menuPort.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Menu not found: " + id));
        menuPort.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public MenuResponse getMenu(Long id) {
        Menu menu = menuPort.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Menu not found: " + id));
        return MenuResponse.from(menu);
    }

    @Override
    @Transactional(readOnly = true)
    public MenuListResponse getMenus(String query, Long categoryId) {
        var menus = menuPort.findAll(query, categoryId);
        return MenuListResponse.from(menus);
    }
}
