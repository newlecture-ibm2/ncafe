import { ListChecks } from 'lucide-react';
import styles from './MenuOptions.module.css';

export default function MenuOptions() {
    // 부모로부터 받던 props를 끊고 컴포넌트 내부에서 자체적으로 데이터를 관리하거나 
    // 정적인 스타일 구조를 유지합니다.
    const options = [
        {
            id: '1',
            name: '기본 옵션 (예시)',
            type: 'radio',
            required: true,
            items: [
                { id: 'i1', name: '옵션 항목 1', priceDelta: 0 },
                { id: 'i2', name: '옵션 항목 2', priceDelta: 500 },
            ]
        }
    ];

    return (
        <section className={styles.card}>
            <h2 className={styles.sectionTitle}>
                <ListChecks size={20} />
                옵션 ({options.length})
            </h2>

            {options.map((option) => (
                <div key={option.id} className={styles.optionGroup}>
                    <div className={styles.optionGroupHeader}>
                        <span>{option.name}</span>
                        <div className={styles.badges}>
                            <span className={styles.optionType}>
                                {option.type === 'radio' ? '단일 선택' : '다중 선택'}
                            </span>
                            {option.required && (
                                <span className={`${styles.optionBadge} ${styles.requiredBadge}`}>필수</span>
                            )}
                        </div>
                    </div>

                    <div className={styles.optionItems}>
                        {option.items.map((item) => (
                            <div key={item.id} className={styles.optionItem}>
                                <span>{item.name}</span>
                                <span className={styles.optionPrice}>
                                    {item.priceDelta > 0 ? `+${item.priceDelta.toLocaleString()}원` : '무료'}
                                </span>
                            </div>
                        ))}
                    </div>
                </div>
            ))}
        </section>
    );
}
