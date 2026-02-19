package com.new_cafe.app.backend.menu.application.port.out;

import java.util.List;
import com.new_cafe.app.backend.menu.domain.Menu; // New Domain Menu

public interface LoadMenuPort {
    List<Menu> findAllByCategoryAndSearchQuery(Integer categoryId, String searchQuery);
    Menu findById(Long id);
}
