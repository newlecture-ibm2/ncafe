'use client';

import { useState, useCallback } from 'react';
import styles from './page.module.css';
import CategoryFilter from './_components/CategoryFilter';
import SearchBar from './_components/SearchBar';
import MenuGrid from './_components/MenuGrid';

export default function MenuPage() {
    const [selectedCategory, setSelectedCategory] = useState<number | undefined>(undefined);
    const [searchQuery, setSearchQuery] = useState('');

    const handleSearch = useCallback((query: string) => {
        setSearchQuery(query);
    }, []);

    return (
        <main>
            {/* Hero Banner */}
            <section className={styles.hero}>
                <div className={styles.heroOverlay} />
                <div className={styles.heroContent}>
                    <span className={styles.heroLabel}>NCafe Menu</span>
                    <h1 className={styles.heroTitle}>
                        정성스럽게 준비한
                        <br />
                        <span className={styles.heroHighlight}>특별한 메뉴</span>
                    </h1>
                    <p className={styles.heroDescription}>
                        바리스타가 엄선한 원두로 내린 커피부터 수제 디저트까지,
                        <br />
                        당신의 취향에 맞는 메뉴를 찾아보세요.
                    </p>
                </div>
            </section>

            {/* Content */}
            <section className={styles.content}>
                <div className={styles.toolbar}>
                    <CategoryFilter
                        selectedCategory={selectedCategory}
                        onSelect={setSelectedCategory}
                    />
                    <SearchBar onSearch={handleSearch} />
                </div>

                <MenuGrid
                    selectedCategory={selectedCategory}
                    searchQuery={searchQuery}
                />
            </section>
        </main>
    );
}
