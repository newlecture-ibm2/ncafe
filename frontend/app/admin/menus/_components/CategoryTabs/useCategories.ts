import { useState, useEffect } from 'react';

export interface CategoryResponseDto {
    id: number;
    name: string;
    icon: string;
    sortOrder: number;
    menuCount: number;
}

export interface CategoryListResponseDto {
    categories: CategoryResponseDto[];
    totalCount: number;
}

// http://localhost:8080/api/v1/admin/categories


export function useCategories() {
    const [categories, setCategories] = useState<CategoryResponseDto[]>([]);

    useEffect(() => {
        const fetchCategories = async () => {
            try {
                const baseUrl = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8080';
                const response = await fetch(`${baseUrl}/api/v1/admin/categories`);
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

    return { categories };
}
