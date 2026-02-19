// app/login/page.tsx
import LoginForm from './LoginForm';
import styles from './login.module.css';

export default function LoginPage() {
    return (
        <main className={styles.container}>
            <LoginForm />
        </main>
    );
}
