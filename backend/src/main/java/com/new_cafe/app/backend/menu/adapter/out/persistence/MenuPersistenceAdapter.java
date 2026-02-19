package com.new_cafe.app.backend.menu.adapter.out.persistence;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.stereotype.Component;

import com.new_cafe.app.backend.menu.application.port.out.LoadMenuPort;
import com.new_cafe.app.backend.menu.application.port.out.SaveMenuPort;
import com.new_cafe.app.backend.menu.domain.Menu;

@Component
public class MenuPersistenceAdapter implements LoadMenuPort, SaveMenuPort {

    private final DataSource dataSource;

    public MenuPersistenceAdapter(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List<Menu> findAllByCategoryAndSearchQuery(Integer categoryId, String searchQuery) {
        List<Menu> list = new ArrayList<>();
        // Note: Using 1=1 to easily append AND clauses
        String sql = "SELECT * FROM menus WHERE 1=1";

        if (categoryId != null)
            sql += " AND category_id=" + categoryId;

        if (searchQuery != null && !searchQuery.isEmpty())
            sql += " AND kor_name LIKE '%" + searchQuery + "%'";

        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                list.add(mapRowToMenu(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
            // TODO: Throw custom exception or handle better
        }

        return list;
    }

    @Override
    public Menu findById(Long id) {
        String sql = "SELECT * FROM menus WHERE id=" + id;

        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return mapRowToMenu(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Menu save(Menu menu) {
        // TODO: Implement Insert/Update logic if needed
        return menu;
    }

    @Override
    public void delete(Long id) {
        // TODO: Implement Delete logic
    }

    // Helper method to map ResultSet to Domain Menu
    private Menu mapRowToMenu(ResultSet rs) throws SQLException {
        return Menu.builder()
                .id(rs.getLong("id"))
                .korName(rs.getString("kor_name"))
                .engName(rs.getString("eng_name"))
                .description(rs.getString("description"))
                .price(rs.getInt("price"))
                .categoryId(rs.getLong("category_id"))
                .isAvailable(rs.getBoolean("is_available"))
                // sortOrder is hardcoded to 1 as per original code, might need column check
                .sortOrder(1) 
                .createdAt(getLocalDateTime(rs, "created_at"))
                .updatedAt(getLocalDateTime(rs, "updated_at"))
                // Associated entities (Category, Images) are typically loaded separately or lazily
                // For now, leaving them null as the Service layer seems to handle filling them
                .category(null)
                .images(null)
                .build();
    }

    private LocalDateTime getLocalDateTime(ResultSet rs, String columnLabel) throws SQLException {
        java.sql.Timestamp ts = rs.getTimestamp(columnLabel);
        return (ts != null) ? ts.toLocalDateTime() : null;
    }
}
