import { useState, useEffect } from 'react';

// 백엔드 & 프론트엔드 통합 인터페이스
export interface MenuImageResponse {
    id: number;
    menuId: number;
    srcUrl: string;
    sortOrder: number;
    // 프론트엔드에서 추가로 사용하는 속성 (optional 처리하거나, 데이터 로드 시 반드시 채워줌)
    isPrimary: boolean;
    createdAt?: string; // 백엔드에서 오지만 뷰에서는 안쓰일 수도 있음
}

interface MenuImageListResponse {
    images: MenuImageResponse[];
}

export function useMenuImages(menuId: string | number) {
    const [images, setImages] = useState<MenuImageResponse[]>([]);
    const [loading, setLoading] = useState<boolean>(true);
    const [error, setError] = useState<unknown>(null);

    useEffect(() => {
        if (!menuId) return;

        const fetchImages = async () => {
            try {
                setLoading(true);
                const response = await fetch(`/api/admin/menus/${menuId}/menu-images`);
                if (!response.ok) {
                    throw new Error('Failed to fetch menu images');
                }
                const data: MenuImageListResponse = await response.json();

                // 데이터 처리: isPrimary 등 파생 속성 계산
                // 백엔드에서 isPrimary가 안 오더라도, 여기서 계산해서 MenuImageResponse 타입을 만족시킴
                const processedImages: MenuImageResponse[] = data.images.map((img) => ({
                    ...img,
                    isPrimary: img.sortOrder === 1
                }));

                // 정렬 (sortOrder 기준 오름차순)
                processedImages.sort((a, b) => a.sortOrder - b.sortOrder);

                setImages(processedImages);
            } catch (err) {
                console.error("Error loading menu images:", err);
                setError(err);
            } finally {
                setLoading(false);
            }
        };

        fetchImages();
    }, [menuId]);

    return { images, loading, error, setImages };
}
