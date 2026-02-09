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
import com.new_cafe.app.backend.dto.MenuImageResponse;
import com.new_cafe.app.backend.dto.MenuImageListResponse;
import com.new_cafe.app.backend.entity.Category;
import com.new_cafe.app.backend.entity.Menu;
import com.new_cafe.app.backend.entity.MenuImage;
import com.new_cafe.app.backend.repository.CategoryRepository;
import com.new_cafe.app.backend.repository.MenuImageRepository;
import com.new_cafe.app.backend.repository.MenuRepository;

@Service
public class NewMenuService implements MenuService {

    private final MenuRepository menuRepository;
    private final CategoryRepository categoryRepository;
    private final MenuImageRepository menuImageRepository;

    public NewMenuService(MenuRepository menuRepository,
            CategoryRepository categoryRepository,
            MenuImageRepository menuImageRepository) {
        this.menuRepository = menuRepository;
        this.categoryRepository = categoryRepository;
        this.menuImageRepository = menuImageRepository;
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

        List<Menu> menus = menuRepository.findAllByCategoryAndSearchQuery(categoryId, searchQuery);

        List<MenuResponse> menuResponses = menus
                .stream()
                .map(menu -> {
                    // 카테고리 이름 조회 (findById 사용)
                    Category category = categoryRepository.findById(menu.getCategoryId());
                    String categoryName = (category != null) ? category.getName() : "미지정";

                    // 이미지 조회 (기본 이미지 방어)
                    List<MenuImage> images = menuImageRepository.findAllByMenuId(menu.getId());

                    // images의 개수가 0인 경우는 blank.png 이미지를 사용하도록 하고
                    String imageSrc = "blank.png";
                    // images의 개수가 1개 이상인 경우는 srcUrl을 사용하도록 한다
                    if (images.size() > 0) {
                        imageSrc = images.get(0).getUrl();
                    }

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
                })
                .toList();

        return MenuListResponse.builder()
                .menus(menuResponses)
                .total(menuResponses.size())
                .build();
    }

    @Override
    public MenuDetailResponse getMenu(Long id) {
        // 메뉴 정보 조회
        Menu menu = menuRepository.findById(id);
        if (menu == null) {
            return null;
        }

        // 카테고리 이름 조회 (findById 사용)
        Category category = categoryRepository.findById(menu.getCategoryId());
        String categoryName = (category != null) ? category.getName() : "미지정";

        // 이미지 조회
        List<MenuImage> images = menuImageRepository.findAllByMenuId(id);
        List<MenuImageResponse> imageResponses = images.stream()
                .map(image -> MenuImageResponse.builder()
                        .id(image.getId())
                        .url(image.getUrl())
                        .sortOrder(image.getSortOrder())
                        .build())
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
    public MenuUpdateResponse updateMenu(MenuUpdateRequest request) {
        throw new UnsupportedOperationException("Unimplemented method 'updateMenu'");
    }

    @Override
    public void deleteMenu(Long id) {
        throw new UnsupportedOperationException("Unimplemented method 'deleteMenu'");
    }

    @Override
    public MenuImageListResponse getMenuImages(Long menuId) {
        // 메뉴 정보 조회 (메뉴 이름을 알기 위함)
        Menu menu = menuRepository.findById(menuId);
        String menuName = (menu != null && menu.getKorName() != null) ? menu.getKorName() : "메뉴";

        // 메뉴 이미지 조회
        List<MenuImage> images = menuImageRepository.findAllByMenuId(menuId);

        // MenuImageResponse로 변환
        List<MenuImageResponse> imageResponses = images.stream()
                .map(image -> {
                    return MenuImageResponse.builder()
                            .id(image.getId())
                            .url(image.getUrl())
                            .sortOrder(image.getSortOrder())
                            .altText(menuName)
                            .build();
                })
                .toList();

        return MenuImageListResponse.builder()
                .images(imageResponses)
                .build();
    }

}
