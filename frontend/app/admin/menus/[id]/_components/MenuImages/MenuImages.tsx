'use client';

import { useState } from 'react';
import Image from 'next/image';
import { useParams } from 'next/navigation';
import { ImageIcon } from 'lucide-react';
import styles from './MenuImages.module.css';
import { useMenuImages, MenuImage } from './useMenuImages';
import { useMenuDetail } from '../MenuDetailInfo/useMenuDetail';

export default function MenuImages() {
    const params = useParams();
    const id = params.id as string;
    const { images, loading, error } = useMenuImages(id);
    const { menuDetail } = useMenuDetail(id); // 메뉴 이름(korName)을 가져오기 위해 추가
    const [selectedImage, setSelectedImage] = useState<MenuImage | null>(null);

    const menuName = menuDetail?.korName || '메뉴';

    // 기본 이미지 설정 (데이터 로드 후 첫 번째 이미지)
    const displayImage = selectedImage || (images.length > 0 ? images[0] : null);

    const baseUrl = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8080';

    if (loading) return <div className={styles.loading}>이미지 로딩 중...</div>;
    // 에러 발생 시에도 빈 상태를 보여줄 수 있으므로 null 반환보다는 UI 유지

    return (
        <section className={styles.card}>
            <h2 className={styles.sectionTitle}>
                <ImageIcon size={20} />
                이미지
            </h2>

            <div className={styles.primaryImageWrapper}>
                {displayImage ? (
                    <Image
                        src={displayImage.url.startsWith('http') ? displayImage.url : `${baseUrl}/${displayImage.url}`}
                        alt={displayImage.altText || `${menuName} 대표 이미지`}
                        fill
                        className={styles.primaryImage}
                    />
                ) : (
                    <div className={styles.emptyState}>
                        <ImageIcon size={48} color="#9ca3af" />
                        <span>이미지 영역</span>
                    </div>
                )}
            </div>

            <div className={styles.thumbnailGrid}>
                {images.map((image) => (
                    <button
                        key={image.id}
                        className={`${styles.thumbnailWrapper} ${displayImage?.id === image.id ? styles.activeThumbnail : ''}`}
                        onClick={() => setSelectedImage(image)}
                    >
                        <Image
                            src={image.url.startsWith('http') ? image.url : `${baseUrl}/${image.url}`}
                            alt={image.altText || `${menuName} 이미지`}
                            fill
                            className={styles.thumbnailImage}
                        />
                    </button>
                ))}
            </div>
        </section>
    );
}
