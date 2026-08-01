import { useCallback, useEffect, useMemo, useState } from 'react'
import { Plus, RefreshCcw, Search, Sparkles } from 'lucide-react'
import QuizCard from '../components/QuizCard.jsx'
import { getApiError, quizApi } from '../api/quizApi.js'
import { achievementApi } from '../api/achievementApi.js'
import { useAuth } from '../context/AuthContext.jsx'

export default function Dashboard({ navigate, setToast }) {
    const { user, isAdmin } = useAuth()
    const [quizzes, setQuizzes] = useState([])
    const [points, setPoints] = useState(0)
    const [loading, setLoading] = useState(true)
    const [search, setSearch] = useState('')
    const [category, setCategory] = useState('')

    const loadQuizzes = useCallback(async () => {
        try {
            setLoading(true)
            const data = category.trim() ? await quizApi.getByCategory(category.trim()) : await quizApi.getAll()
            setQuizzes(Array.isArray(data) ? data : data?.content || [])
        } catch (error) {
            setToast({ type: 'error', message: getApiError(error) })
        } finally {
            setLoading(false)
        }
    }, [category, setToast])

    useEffect(() => { loadQuizzes() }, []) // eslint-disable-line react-hooks/exhaustive-deps
    useEffect(() => {
        achievementApi.getPoints(user.id).then((data) => setPoints(Number(data?.points ?? data ?? 0))).catch(() => {})
    }, [user.id])

    const filtered = useMemo(() => {
        const query = search.toLowerCase()
        return quizzes.filter((quiz) => [quiz.title, quiz.description, quiz.category].some((value) => value?.toLowerCase().includes(query)))
    }, [quizzes, search])

    const canManage = (quiz) => isAdmin || Number(quiz.ownerId) === Number(user.id)

    const publishQuiz = async (id) => {
        try {
            await quizApi.publish(id)
            setToast({ type: 'success', message: 'Quiz published. Interested users will be notified.' })
            loadQuizzes()
        } catch (error) { setToast({ type: 'error', message: getApiError(error) }) }
    }

    const deleteQuiz = async (quiz) => {
        if (!window.confirm(`Delete “${quiz.title}” permanently?`)) return
        try {
            await quizApi.delete(quiz.id)
            setToast({ type: 'success', message: 'Quiz deleted' })
            loadQuizzes()
        } catch (error) { setToast({ type: 'error', message: getApiError(error) }) }
    }

    const unlockQuiz = async (quiz) => {
        if (!window.confirm(`Unlock “${quiz.title}” for ${quiz.unlockPoints} points?`)) return
        try {
            await quizApi.unlock(quiz.id)
            setPoints((current) => Math.max(0, current - Number(quiz.unlockPoints || 0)))
            setQuizzes((current) => current.map((item) => item.id === quiz.id ? { ...item, unlocked: true } : item))
            setToast({ type: 'success', message: 'Premium quiz unlocked successfully' })
        } catch (error) { setToast({ type: 'error', message: getApiError(error) }) }
    }

    return (
        <>
            <section className="hero dashboard-hero">
                <div><span className="eyebrow">Welcome back, {user.username}</span><h1>Find your next challenge.</h1><p>Play community quizzes, collect points, and unlock premium challenges.</p></div>
                <div className="hero-actions"><div className="points-chip"><Sparkles size={18} /><span>{points} points</span></div><button className="btn primary large" onClick={() => navigate('create')}><Plus size={18} /> New quiz</button></div>
            </section>
            <section className="toolbar">
                <div className="searchbox"><Search size={18} /><input value={search} onChange={(e) => setSearch(e.target.value)} placeholder="Search title, description, or category" /></div>
                <input value={category} onChange={(e) => setCategory(e.target.value)} placeholder="Category filter" />
                <button className="btn" onClick={loadQuizzes}><RefreshCcw size={16} /> Load</button>
            </section>
            {loading ? <div className="empty">Loading quizzes...</div> : filtered.length === 0 ? <div className="empty">No quizzes found.</div> : (
                <section className="quiz-grid">{filtered.map((quiz) => <QuizCard key={quiz.id} quiz={quiz} canManage={canManage(quiz)} onView={(id) => navigate('play', id)} onEdit={(id) => navigate('edit', id)} onPublish={publishQuiz} onDelete={deleteQuiz} onUnlock={unlockQuiz} />)}</section>
            )}
        </>
    )
}
