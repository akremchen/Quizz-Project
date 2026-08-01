import { useState } from 'react'
import { BookOpenCheck, LogIn, UserPlus } from 'lucide-react'
import { useAuth } from '../context/AuthContext.jsx'
import { getApiError } from '../api/client.js'

export default function AuthPage() {
    const [mode, setMode] = useState('login')
    const [form, setForm] = useState({ username: '', email: '', password: '' })
    const [loading, setLoading] = useState(false)
    const [error, setError] = useState('')
    const { login, register } = useAuth()

    const set = (field, value) => setForm((current) => ({ ...current, [field]: value }))

    const submit = async (event) => {
        event.preventDefault()
        setError('')
        setLoading(true)
        try {
            if (mode === 'register') {
                await register({ username: form.username.trim(), email: form.email.trim(), password: form.password })
            }
            await login({ email: form.email.trim(), password: form.password })
        } catch (err) {
            setError(getApiError(err))
        } finally {
            setLoading(false)
        }
    }

    return (
        <main className="auth-page">
            <section className="auth-intro">
                <div className="brand-mark"><BookOpenCheck size={30} /></div>
                <span className="eyebrow">Quiz Platform</span>
                <h1>Learn, compete, and unlock your next challenge.</h1>
                <p>Create quizzes, earn points and badges, follow your favorite categories, and never miss a new challenge.</p>
                <div className="auth-feature-row"><span>Live notifications</span><span>Premium quizzes</span><span>Achievements</span></div>
            </section>
            <section className="auth-card">
                <div className="auth-tabs">
                    <button className={mode === 'login' ? 'active' : ''} onClick={() => setMode('login')}>Sign in</button>
                    <button className={mode === 'register' ? 'active' : ''} onClick={() => setMode('register')}>Create account</button>
                </div>
                <form onSubmit={submit}>
                    <h2>{mode === 'login' ? 'Welcome back' : 'Join the platform'}</h2>
                    <p>{mode === 'login' ? 'Use your account to continue.' : 'Start building your quiz profile.'}</p>
                    {mode === 'register' && <label>Username<input value={form.username} onChange={(e) => set('username', e.target.value)} required minLength="3" /></label>}
                    <label>Email<input type="email" value={form.email} onChange={(e) => set('email', e.target.value)} required /></label>
                    <label>Password<input type="password" value={form.password} onChange={(e) => set('password', e.target.value)} required minLength="6" /></label>
                    {error && <div className="error-box">{error}</div>}
                    <button className="btn primary large full" disabled={loading}>
                        {mode === 'login' ? <LogIn size={18} /> : <UserPlus size={18} />}
                        {loading ? 'Please wait...' : mode === 'login' ? 'Sign in' : 'Create account'}
                    </button>
                </form>
            </section>
        </main>
    )
}
