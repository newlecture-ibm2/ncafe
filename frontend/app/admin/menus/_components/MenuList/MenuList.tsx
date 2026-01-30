import {
    DndContext,
    closestCenter,
    KeyboardSensor,
    PointerSensor,
    useSensor,
    useSensors,
    DragEndEvent,
} from '@dnd-kit/core';
import {
    arrayMove,
    SortableContext,
    sortableKeyboardCoordinates,
    rectSortingStrategy,
} from '@dnd-kit/sortable';
import { useState, useEffect, useMemo } from 'react';
import { Menu } from '@/types';
import { mockCategories } from '@/mocks/menuData'; // To get default category
import MenuCard from '../MenuCard';
import Pagination from '@/app/_components/Pagination';
import styles from './MenuList.module.css';

interface MenuListProps {
    selectedCategory: string | null;
    searchQuery: string;
}

const ITEMS_PER_PAGE = 12;

export default function MenuList({ selectedCategory, searchQuery }: MenuListProps) {
    const [menus, setMenus] = useState<Menu[]>([]);
    const [currentPage, setCurrentPage] = useState(1);

    // 데이터 로드
    useEffect(() => {
        const fetchMenus = async () => {
            try {
                const response = await fetch('http://localhost:8080/admin/menus');
                if (!response.ok) {
                    throw new Error('Failed to fetch menus');
                }
                const data = await response.json();

                // 백엔드 데이터를 프론트엔드 Menu 타입으로 변환
                const mappedMenus: Menu[] = data.map((item: any) => ({
                    id: String(item.id),
                    korName: item.korName,
                    engName: item.engName,
                    description: item.description,
                    price: parseInt(item.price) || 0,
                    // 카테고리 정보가 없으므로 임시로 첫 번째 카테고리 할당
                    category: mockCategories[0],
                    images: item.image ? [{
                        id: `img-${item.id}`,
                        url: item.image,
                        isPrimary: true,
                        sortOrder: 0
                    }] : [],
                    isAvailable: true,
                    isSoldOut: false,
                    sortOrder: item.id, // 임시 정렬 순서
                    options: [],
                    createdAt: new Date(),
                    updatedAt: new Date(),
                }));

                setMenus(mappedMenus);
            } catch (error) {
                console.error('Error fetching menus:', error);
            }
        };

        fetchMenus();
    }, []);

    // 필터링된 메뉴 목록
    const filteredMenus = useMemo(() => {
        let result = menus;

        // 카테고리 필터
        if (selectedCategory) {
            result = result.filter(menu => menu.category.id === selectedCategory);
        }

        // 검색 필터
        if (searchQuery.trim()) {
            const query = searchQuery.toLowerCase();
            result = result.filter(
                menu =>
                    menu.korName.toLowerCase().includes(query) ||
                    menu.engName.toLowerCase().includes(query) ||
                    menu.description.toLowerCase().includes(query)
            );
        }

        return result;
    }, [menus, selectedCategory, searchQuery]);

    // 필터 변경 시 페이지 초기화
    useEffect(() => {
        setCurrentPage(1);
    }, [selectedCategory, searchQuery]);

    // 페이징 처리된 메뉴 목록
    const paginatedMenus = useMemo(() => {
        const startIndex = (currentPage - 1) * ITEMS_PER_PAGE;
        return filteredMenus.slice(startIndex, startIndex + ITEMS_PER_PAGE);
    }, [filteredMenus, currentPage]);

    const totalPages = Math.ceil(filteredMenus.length / ITEMS_PER_PAGE);

    // 품절 토글
    const handleToggleSoldOut = (id: string, isSoldOut: boolean) => {
        setMenus(prev =>
            prev.map(menu =>
                menu.id === id ? { ...menu, isSoldOut } : menu
            )
        );
    };

    // 삭제
    const handleDelete = (id: string) => {
        if (window.confirm('정말 이 메뉴를 삭제하시겠습니까?')) {
            setMenus(prev => prev.filter(menu => menu.id !== id));
        }
    };

    // 센서 설정
    const sensors = useSensors(
        useSensor(PointerSensor, {
            activationConstraint: {
                distance: 8,
            },
        }),
        useSensor(KeyboardSensor, {
            coordinateGetter: sortableKeyboardCoordinates,
        })
    );

    // 드래그 종료 핸들러
    const handleDragEnd = (event: DragEndEvent) => {
        const { active, over } = event;

        if (over && active.id !== over.id) {
            setMenus((items) => {
                const oldIndex = items.findIndex((item) => item.id === active.id);
                const newIndex = items.findIndex((item) => item.id === over.id);

                return arrayMove(items, oldIndex, newIndex);
            });
        }
    };

    if (menus.length === 0) {
        // 데이터가 아직 없거나 로딩 중일 때 표시할 내용
        // 여기서는 기존 'empty' UI를 재활용하되, 로딩 상태 구분은 하지 않음 (기존 로직 유지)
        return (
            <div className={styles.empty}>
                <div className={styles.emptyIcon}>📋</div>
                <h3 className={styles.emptyTitle}>메뉴를 불러오는 중이거나 없습니다.</h3>
                <p className={styles.emptyDescription}>
                    잠시만 기다려주세요...
                </p>
            </div>
        );
    }

    // 필터링 결과가 없을 때 보여줄 UI는? (기존에는 length===0 check가 전체 메뉴 기준이었음)
    // List should render even if empty? 
    // If filtered result is empty but menus has items, simple text?

    if (filteredMenus.length === 0) {
        return (
            <div className={styles.empty}>
                <div className={styles.emptyIcon}>🔍</div>
                <h3 className={styles.emptyTitle}>검색 결과가 없습니다</h3>
            </div>
        );
    }

    return (
        <div>
            <DndContext
                sensors={sensors}
                collisionDetection={closestCenter}
                onDragEnd={handleDragEnd}
            >
                <SortableContext
                    items={paginatedMenus.map(m => m.id)}
                    strategy={rectSortingStrategy}
                >
                    <div className={styles.grid}>
                        {paginatedMenus.map((menu) => (
                            <MenuCard
                                key={menu.id}
                                menu={menu}
                                onToggleSoldOut={handleToggleSoldOut}
                                onDelete={handleDelete}
                            />
                        ))}
                    </div>
                </SortableContext>
            </DndContext>

            {/* 페이징 컨트롤 */}
            <div className={styles.paginationWrapper}>
                <Pagination
                    currentPage={currentPage}
                    totalPages={totalPages}
                    onPageChange={setCurrentPage}
                />
            </div>
        </div>
    );
}
