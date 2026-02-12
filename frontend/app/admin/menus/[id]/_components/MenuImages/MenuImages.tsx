'use client';

import { useState, useEffect } from 'react';
import Image from 'next/image';
import { ImageIcon } from 'lucide-react';
import styles from './MenuImages.module.css';
import { useMenuImages, MenuImageResponse } from './useMenuImages';


export default function MenuImages({ menuId }: { menuId: number }) {

    const { images, loading } = useMenuImages(menuId);
    const [selectedImageId, setSelectedImageId] = useState<number | null>(null);

    // 선택된 이미지가 없으면 첫 번째 이미지를 기본으로 사용
    const selectedImage = selectedImageId
        ? images.find(img => img.id === selectedImageId)
        : images[0];

    // 첫 이미지가 로드될 때 selectedImageId를 설정할 수도 있지만, 
    // 위 로직(images[0])으로 처리하면 이미지가 변경될 때 자연스럽게 첫 번째가 나옴.
    // 다만 클릭 시 명시적으로 변경.

    if (loading) {
        return <div className={styles.loading}>이미지 로딩 중...</div>;
    }

    if (!images || images.length === 0) {
        return (
            <section className={styles.card}>
                <h2 className={styles.sectionTitle}>
                    <ImageIcon size={20} />
                    이미지
                </h2>
                <div className={styles.emptyState}>
                    <ImageIcon size={48} />
                    <span>이미지가 없습니다</span>
                </div>
            </section>
        );
    }

    return (
        <section className={styles.card}>
            <h2 className={styles.sectionTitle}>
                <ImageIcon size={20} />
                이미지
            </h2>

            <div className={styles.primaryImageWrapper}>
                {selectedImage && (
                    <Image
                        src={`/api/v1/${selectedImage.srcUrl}`}
                        alt="상세 이미지"
                        fill
                        className={styles.primaryImage}
                        priority
                        sizes="(max-width: 768px) 100vw, 50vw"
                    />
                )}
                {selectedImage?.isPrimary && (
                    <span className={styles.primaryBadge}>대표 이미지</span>
                )}
            </div>

            <div className={styles.thumbnailGrid}>
                {images.map((image) => (
                    <button
                        key={image.id}
                        className={styles.thumbnailWrapper}
                        style={{
                            borderColor: selectedImage?.id === image.id ? 'var(--color-primary-500)' : undefined
                        }}
                        onClick={() => setSelectedImageId(image.id)}
                    >
                        <Image
                            src={`/images/${image.srcUrl}`}
                            alt="썸네일"
                            fill
                            className={styles.thumbnailImage}
                            sizes="100px"
                        />
                    </button>
                ))}
            </div>
        </section>
    );
}
