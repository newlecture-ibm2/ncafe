package com.new_cafe.app.backend.menu.application.port.out;

import com.new_cafe.app.backend.menu.domain.Menu; // New Domain Menu

public interface SaveMenuPort {
    Menu save(Menu menu);
    void delete(Long id);
}
