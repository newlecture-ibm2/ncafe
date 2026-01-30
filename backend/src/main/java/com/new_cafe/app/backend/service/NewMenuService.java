package com.new_cafe.app.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.new_cafe.app.backend.entity.Menu;
import com.new_cafe.app.backend.repository.MenuRepository;

@Service
public class NewMenuService implements MenuService {

    private MenuRepository menuRepository;

    public NewMenuService(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    @Override
    public List<Menu> getAll() {
        return menuRepository.findAll();
    }

    @Override
    public List<Menu> getAll(Integer categoryId) {
        return menuRepository.findAllByCategoryId(categoryId);
    }

}
