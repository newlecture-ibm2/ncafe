package com.new_cafe.app.backend.menu.application.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.new_cafe.app.backend.dto.*;
import com.new_cafe.app.backend.entity.Category;
import com.new_cafe.app.backend.entity.MenuImage;
import com.new_cafe.app.backend.menu.application.port.in.GetMenuUseCase; // New Interface
import com.new_cafe.app.backend.menu.application.port.in.ManageMenuUseCase; // New Interface
import com.new_cafe.app.backend.menu.application.port.out.LoadMenuPort;
import com.new_cafe.app.backend.menu.application.port.out.SaveMenuPort;
import com.new_cafe.app.backend.menu.domain.Menu;
import com.new_cafe.app.backend.repository.CategoryRepository;
import com.new_cafe.app.backend.repository.MenuImageRepository;

@Service
public class MenuService implements GetMenuUseCase, ManageMenuUseCase { // Implements split interfaces

    private final LoadMenuPort loadMenuPort;
    private final SaveMenuPort saveMenuPort;
    private final CategoryRepository categoryRepository;
    private final MenuImageRepository menuImageRepository;

    public MenuService(LoadMenuPort loadMenuPort, SaveMenuPort saveMenuPort,
                       CategoryRepository categoryRepository, MenuImageRepository menuImageRepository) {
        this.loadMenuPort = loadMenuPort;
        this.saveMenuPort = saveMenuPort;
        this.categoryRepository = categoryRepository;
        this.menuImageRepository = menuImageRepository;
    }

    // --- GetMenuUseCase Implementation ---
    @Override
    public MenuListResponse getMenus(MenuListRequest request) {
        Integer categoryId = request.getCategoryId();
        String searchQuery = request.getSearchQuery();

        List<Menu> menus = loadMenuPort.findAllByCategoryAndSearchQuery(categoryId, searchQuery);

        List<MenuResponse> menuResponses = menus.stream()
                .map(this::mapToMenuResponse)
                .toList();

        return MenuListResponse.builder()
                .menus(menuResponses)
                .total(menuResponses.size())
                .build();
    }

    @Override
    public MenuDetailResponse getMenu(Long id) {
        Menu menu = loadMenuPort.findById(id);
        if (menu == null) return null;

        Category category = categoryRepository.findById(menu.getCategoryId());
        String categoryName = (category != null) ? category.getName() : "미지정";

        List<MenuImage> images = menuImageRepository.findAllByMenuId(id);
        List<MenuImageResponse> imageResponses = images.stream()
                .map(image -> mapToMenuImageResponse(image, menu.getKorName()))
                .toList();

        return MenuDetailResponse.builder()
                .id(menu.getId())
                .korName(menu.getKorName())
                .engName(menu.getEngName())
                .categoryName(categoryName)
                .price(menu.getPrice() != null ? menu.getPrice().toString() : "0")
                .isAvailable(menu.getIsAvailable() != null ? menu.getIsAvailable() : true)
                .createdAt(menu.getCreatedAt())
                .description(menu.getDescription())
                .images(imageResponses)
                .build();
    }

    @Override
    public MenuImageListResponse getMenuImages(Long id) {
        Menu menu = loadMenuPort.findById(id);
        String menuName = (menu != null && menu.getKorName() != null) ? menu.getKorName() : "메뉴";

        List<MenuImage> images = menuImageRepository.findAllByMenuId(id);
        List<MenuImageResponse> imageResponses = images.stream()
                .map(image -> mapToMenuImageResponse(image, menuName))
                .toList();
        
        return MenuImageListResponse.builder().images(imageResponses).build();
    }

    // --- ManageMenuUseCase Implementation ---
    @Override
    public MenuCreateResponse createMenu(MenuCreateRequest request) {
        // TODO: Implement logic + call saveMenuPort.save()
        throw new UnsupportedOperationException("Unimplemented method 'createMenu'");
    }

    @Override
    public MenuUpdateResponse updateMenu(MenuUpdateRequest request) {
        // TODO: Implement logic + call saveMenuPort.save()
        throw new UnsupportedOperationException("Unimplemented method 'updateMenu'");
    }

    @Override
    public void deleteMenu(Long id) {
        // TODO: Implement logic + call saveMenuPort.delete()
        throw new UnsupportedOperationException("Unimplemented method 'deleteMenu'");
    }

    // --- Helper Methods ---
    private MenuResponse mapToMenuResponse(Menu menu) {
        Category category = categoryRepository.findById(menu.getCategoryId());
        String categoryName = (category != null) ? category.getName() : "미지정";

        List<MenuImage> images = menuImageRepository.findAllByMenuId(menu.getId());
        String imageSrc = (images != null && !images.isEmpty()) ? images.get(0).getUrl() : "blank.png";

        return MenuResponse.builder()
                .id(menu.getId())
                .korName(menu.getKorName())
                .engName(menu.getEngName())
                .description(menu.getDescription())
                .price(menu.getPrice())
                .categoryName(categoryName)
                .imageSrc(imageSrc)
                .isAvailable(menu.getIsAvailable() != null ? menu.getIsAvailable() : true)
                .isSoldOut(false)
                .sortOrder(menu.getSortOrder() != null ? menu.getSortOrder() : 1)
                .createdAt(menu.getCreatedAt())
                .updatedAt(menu.getUpdatedAt())
                .build();
    }

    private MenuImageResponse mapToMenuImageResponse(MenuImage image, String altText) {
        return MenuImageResponse.builder()
                .id(image.getId())
                .menuId(image.getMenuId())
                .url(image.getUrl())
                .sortOrder(image.getSortOrder())
                .altText(altText)
                .createdAt(image.getCreatedAt())
                .build();
    }
}
