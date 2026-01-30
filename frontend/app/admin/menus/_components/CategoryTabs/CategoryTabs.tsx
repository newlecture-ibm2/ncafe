'use client';

import { MenuCategory, Menu } from '@/types';
import styles from './CategoryTabs.module.css';
import { useEffect, useState } from 'react';

interface CategoryTabsProps {
    onSelectCategory: (categoryId: string | null) => void;
}


export default function CategoryTabs({
    onSelectCategory,
}: CategoryTabsProps) {

    const [categories, setCategories] = useState<MenuCategory[]>([]);
    const [menus, setMenus] = useState<Menu[]>([]);
    const [selectedCategory, setSelectedCategory] = useState<string | null>(null);

    useEffect(() => {
        const fetchCategories = async () => {
            try {
                const response = await fetch('http://localhost:8080/admin/categories');
                if (!response.ok) {
                    throw new Error('Failed to fetch categories');
                }
                const data = await response.json();
                setCategories(data);
            } catch (error) {
                console.error('Error fetching categories:', error);
            }
        };

        fetchCategories();
    }, []);

    // 카테고리별 메뉴 개수 계산
    const getMenuCount = (categoryId: string | null) => {
        if (!categoryId) return menus.length;
        return menus.filter(menu => menu.category.id === categoryId).length;
    };

    return (
        <div className={styles.tabs}>
            {/* 전체 탭 */}
            <button
                className={`${styles.tab} ${selectedCategory === null ? styles.tabActive : ''}`}
                onClick={() => onSelectCategory(null)}
            >
                <span className={styles.tabIcon}>📋</span>
                전체
                <span className={styles.tabCount}>{getMenuCount(null)}</span>
            </button>

            {/* 카테고리별 탭 */}
            {categories.map((category) => (
                <button
                    key={category.id}
                    className={`${styles.tab} ${selectedCategory === category.id ? styles.tabActive : ''}`}
                    onClick={() => onSelectCategory(category.id)}
                >
                    <span className={styles.tabIcon}>{category.icon}</span>
                    {category.name}
                    <span className={styles.tabCount}>{getMenuCount(category.id)}</span>
                </button>
            ))}
        </div>
    );
}
