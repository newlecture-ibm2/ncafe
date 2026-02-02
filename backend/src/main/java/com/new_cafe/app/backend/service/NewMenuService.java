package com.new_cafe.app.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.new_cafe.app.backend.dto.MenuCreateRequest;
import com.new_cafe.app.backend.dto.MenuCreateResponse;
import com.new_cafe.app.backend.dto.MenuDetailResponse;
import com.new_cafe.app.backend.dto.MenuListRequest;
import com.new_cafe.app.backend.dto.MenuListResponse;
import com.new_cafe.app.backend.dto.MenuUpdateRequest;
import com.new_cafe.app.backend.dto.MenuUpdateResponse;
import com.new_cafe.app.backend.dto.MenuResponse;
import com.new_cafe.app.backend.entity.Menu;
import com.new_cafe.app.backend.repository.MenuRepository;

@Service
public class NewMenuService implements MenuService {

    private MenuRepository menuRepository;

    public NewMenuService(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    @Override
    public MenuCreateResponse createMenu(MenuCreateRequest request) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createMenu'");
    }

    @Override
    public MenuListResponse getMenus(MenuListRequest request) {

        Integer categoryId = request.getCategoryId();
        String searchQuery = request.getSearchQuery();

        // Menu <----> MenuResponse ----> [] ----> MenuListResponse
        List<Menu> menus = menuRepository.findAllByCategoryAndSearchQuery(categoryId, searchQuery);

        List<MenuResponse> menuResponses = menus
                .stream()
                .map(menu -> MenuResponse
                        .builder()
                        .id(menu.getId())
                        .korName(menu.getKorName())
                        .engName(menu.getEngName())
                        .description(menu.getDescription())
                        .price(menu.getPrice())
                        .categoryName("커피")
                        .imageSrc("/images/coffee.jpg")
                        .isAvailable(true)
                        .isSoldOut(false)
                        .sortOrder(1)
                        .createdAt(menu.getCreatedAt())
                        .updatedAt(menu.getUpdatedAt())
                        .build())
                .toList();

        return MenuListResponse
                .builder()
                .menus(menuResponses)
                .total(100)
                .build();
    }

    @Override
    public MenuDetailResponse getMenu(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getMenu'");
    }

    @Override
    public MenuUpdateResponse updateMenu(MenuUpdateRequest request) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateMenu'");
    }

    @Override
    public void deleteMenu(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteMenu'");
    }

}
