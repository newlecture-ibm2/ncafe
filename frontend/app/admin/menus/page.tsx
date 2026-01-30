'use client';


import { useState } from 'react';
import CategoryTabs from './_components/CategoryTabs';
import MenuList from './_components/MenuList';
import MenusPageHeader from './_components/MenusPageHeader';
import { mockCategories } from '@/mocks/menuData';

export default function MenusPage() {
    // 상태
    const [selectedCategory, setSelectedCategory] = useState<string | null>(null);
    const [searchQuery, setSearchQuery] = useState('');

    const handleSelectCategory = (category: string | null) => {
        setSelectedCategory(category);
    };

    return (
        <main>
            {/* 페이지 헤더 */}
            <MenusPageHeader
                totalCount={0} // 데이터가 하위 컴포넌트로 이동하여 임시로 0 표시
                soldOutCount={0}
                searchQuery={searchQuery}
                onSearchChange={setSearchQuery}
            />

            {/* 카테고리 탭 */}
            <CategoryTabs
                onSelectCategory={handleSelectCategory}
            />

            {/* 메뉴 그리드 (데이터 로딩 및 관리는 내부에서 수행) */}
            <MenuList
                selectedCategory={selectedCategory}
                searchQuery={searchQuery}
            />
        </main>
    );
}
