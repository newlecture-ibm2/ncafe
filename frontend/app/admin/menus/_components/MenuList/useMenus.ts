import { useState, useEffect } from 'react';
import { Menu } from '@/types';
import { mockCategories } from '@/mocks/menuData';


export interface MenuResponse {
    id: number,
    korName: string,
    engName: string,
    description: string,
    price: number,
    image: string
}

export function useMenus(selectedCategory: number | undefined, searchQuery: string | undefined) {
    const [menus, setMenus] = useState<MenuResponse[]>([]);

    useEffect(() => {
        console.log("=============================");
        console.log(selectedCategory);
        console.log(searchQuery);
        console.log("=============================");

        const fetchMenus = async () => {

            const url = new URL('http://localhost:8080/admin/menus');

            const params = url.searchParams;
            if (selectedCategory) {
                params.set('cid', selectedCategory.toString());
            }
            if (searchQuery) {
                params.set('searchQuery', searchQuery);
            }

            try {
                const response = await fetch(url.toString());
                if (!response.ok) {
                    throw new Error('Failed to fetch menus');
                }
                const data = await response.json();
                console.log("-------------------------------------------------")
                console.log(data);
                console.log("-------------------------------------------------")
                setMenus(data.menus);

                // 백엔드 데이터를 프론트엔드 Menu 타입으로 변환
                // const mappedMenus: MenuResponse[] = data.map((item: any) => ({
                //     id: String(item.id),
                //     korName: item.korName,
                //     engName: item.engName,
                //     description: item.description,
                //     price: parseInt(item.price) || 0,
                //     // 카테고리 정보가 없으므로 임시로 첫 번째 카테고리 할당
                //     category: mockCategories[0],
                //     images: item.image ? [{
                //         id: `img-${item.id}`,
                //         url: item.image,
                //         isPrimary: true,
                //         sortOrder: 0
                //     }] : [],
                //     isAvailable: true,
                //     isSoldOut: false,
                //     sortOrder: item.id, // 임시 정렬 순서
                //     options: [],
                //     createdAt: new Date(),
                //     updatedAt: new Date(),
                // }));

                // setMenus(data);
            } catch (error) {
                console.error('Error fetching menus:', error);
            }
        };

        fetchMenus();
    }, [selectedCategory, searchQuery]);

    return { menus, setMenus };
}
