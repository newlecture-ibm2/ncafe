package com.new_cafe.app.backend.menu.adapter.out.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MenuJpaRepository extends JpaRepository<MenuEntity, Long> {

    @Query("SELECT m FROM UserMenu m LEFT JOIN FETCH m.category WHERE m.id = :id AND m.isAvailable = true")
    Optional<MenuEntity> findAvailableById(@Param("id") Long id);

    @Query("SELECT m FROM UserMenu m LEFT JOIN FETCH m.category WHERE m.isAvailable = true")
    List<MenuEntity> findAllAvailable();

    @Query("SELECT m FROM UserMenu m LEFT JOIN FETCH m.category WHERE m.isAvailable = true AND m.categoryId = :categoryId")
    List<MenuEntity> findAllAvailableByCategoryId(@Param("categoryId") Long categoryId);

    @Query("SELECT m FROM UserMenu m LEFT JOIN FETCH m.category WHERE m.isAvailable = true AND (m.korName LIKE %:query% OR m.engName LIKE %:query%)")
    List<MenuEntity> findAllAvailableByQuery(@Param("query") String query);

    @Query("SELECT m FROM UserMenu m LEFT JOIN FETCH m.category WHERE m.isAvailable = true AND m.categoryId = :categoryId AND (m.korName LIKE %:query% OR m.engName LIKE %:query%)")
    List<MenuEntity> findAllAvailableByCategoryIdAndQuery(@Param("categoryId") Long categoryId, @Param("query") String query);
}
