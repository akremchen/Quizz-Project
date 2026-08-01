import { useEffect, useMemo, useState } from 'react'
import { Plus, RefreshCcw, Search } from 'lucide-react'
import QuizCard from '../components/QuizCard.jsx'
import { getApiError, quizApi } from '../api/quizApi.js'

export default function Dashboard({ navigate, setToast }) {
    const [quizzes, setQuizzes] = useState([])
    const [loading, setLoading] = useState(true)
    const [search, setSearch] = useState('')
    const [category, setCategory] = useState('')

    const loadQuizzes = async () => {
        try {
            setLoading(true)
            const data = category.trim() ? await quizApi.getByCategory(category.trim()) : await quizApi.getAll()
            setQuizzes(data)
        } catch (error) {
            setToast({ type: 'error', message: getApiError(error) })
        } finally {
            setLoading(false)
        }
    }

    useEffect(() => { loadQuizzes() }, [])

    const filtered = useMemo(() => {
        const q = search.toLowerCase()
        return quizzes.filter((quiz) =>
            quiz.title?.toLowerCase().includes(q) ||
            quiz.description?.toLowerCase().includes(q) ||
            quiz.category?.toLowerCase().includes(q),
        )
    }, [quizzes, search])

    const publishQuiz = async (id) => {
        try {
            await quizApi.publish(id)
            setToast({ type: 'success', message: 'Quiz published successfully' })
            loadQuizzes()
        } catch (error) {
            setToast({ type: 'error', message: getApiError(error) })
        }
    }

    const deleteQuiz = async (quiz) => {
        const ownerId = window.prompt(`Owner ID required to delete "${quiz.title}"`, quiz.ownerId || '')
        if (!ownerId) return
        if (!window.confirm('Delete this quiz permanently?')) return

        try {
            await quizApi.delete(quiz.id, ownerId)
            setToast({ type: 'success', message: 'Quiz deleted' })
            loadQuizzes()
        } catch (error) {
            setToast({ type: 'error', message: getApiError(error) })
        }
    }

    return (
        <>
            <section className="hero">
                <div>
                    <span className="eyebrow">Quiz Service</span>
                    <h1>Build, publish, and play quizzes.</h1>
                </div>
                <button className="btn primary large" onClick={() => navigate('create')}><Plus size={18} /> New quiz</button>
            </section>

            <section className="toolbar">
                <div className="searchbox"><Search size={18} /><input value={search} onChange={(e) => setSearch(e.target.value)} placeholder="Search quizzes" /></div>
                <input value={category} onChange={(e) => setCategory(e.target.value)} placeholder="Filter by category API" />
                <button className="btn" onClick={loadQuizzes}><RefreshCcw size={16} /> Load</button>
            </section>

            {loading ? <div className="empty">Loading quizzes...</div> : filtered.length === 0 ? (
                <div className="empty">No quizzes found. Create one to get started.</div>
            ) : (
                <section className="quiz-grid">
                    {filtered.map((quiz) => (
                        <QuizCard
                            key={quiz.id}
                            quiz={quiz}
                            onView={(id) => navigate('play', id)}
                            onEdit={(id) => navigate('edit', id)}
                            onPublish={publishQuiz}
                            onDelete={deleteQuiz}
                        />
                    ))}
                </section>
            )}
        </>
    )
}
