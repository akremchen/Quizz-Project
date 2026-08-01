import { useEffect, useState } from 'react'
import QuizForm from '../components/QuizForm.jsx'
import { getApiError, quizApi } from '../api/quizApi.js'
import { emptyQuiz, quizToForm, validateQuizForm } from '../utils/quizForm.js'
import { useAuth } from '../context/AuthContext.jsx'

export default function CreateEditQuiz({ mode, quizId, navigate, setToast }) {
    const { isAdmin } = useAuth()
    const [form, setForm] = useState(() => ({ ...emptyQuiz }))
    const [loading, setLoading] = useState(false)

    useEffect(() => {
        if (mode !== 'edit') return
        setLoading(true)
        quizApi.getById(quizId).then((quiz) => setForm(quizToForm(quiz))).catch((error) => setToast({ type: 'error', message: getApiError(error) })).finally(() => setLoading(false))
    }, [mode, quizId, setToast])

    const submit = async (event) => {
        event.preventDefault()
        const validationError = validateQuizForm(form)
        if (validationError) return setToast({ type: 'error', message: validationError })
        const payload = {
            title: form.title.trim(), description: form.description.trim(), category: form.category.trim(),
            premium: isAdmin ? Boolean(form.premium) : false,
            unlockPoints: isAdmin && form.premium ? Number(form.unlockPoints) : null,
            questions: form.questions.map((question) => ({ question: question.question.trim(), options: question.options.map((option) => ({ answer: option.answer.trim(), correct: option.correct })) })),
        }
        try {
            setLoading(true)
            if (mode === 'edit') await quizApi.update(quizId, payload)
            else await quizApi.create(payload)
            setToast({ type: 'success', message: mode === 'edit' ? 'Quiz updated' : 'Quiz created as a draft' })
            navigate('dashboard')
        } catch (error) { setToast({ type: 'error', message: getApiError(error) }) } finally { setLoading(false) }
    }

    return <section className="panel"><div className="page-heading"><div><span className="eyebrow">{mode === 'edit' ? 'Edit quiz' : 'Create quiz'}</span><h1>{mode === 'edit' ? 'Update your quiz' : 'Build a new challenge'}</h1><p>The signed-in account will be recorded as the owner.</p></div><button className="btn" onClick={() => navigate('dashboard')}>Back</button></div><QuizForm form={form} setForm={setForm} mode={mode} onSubmit={submit} loading={loading} isAdmin={isAdmin} /></section>
}
