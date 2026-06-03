import { useEffect, useState } from 'react'
import QuizForm from '../components/QuizForm.jsx'
import { getApiError, quizApi } from '../api/quizApi.js'
import { emptyQuiz, quizToForm, validateQuizForm } from '../utils/quizForm.js'

export default function CreateEditQuiz({ mode, quizId, navigate, setToast }) {
    const [form, setForm] = useState(emptyQuiz)
    const [loading, setLoading] = useState(false)

    useEffect(() => {
        if (mode !== 'edit') return
        const loadQuiz = async () => {
            try {
                setLoading(true)
                const quiz = await quizApi.getById(quizId)
                setForm(quizToForm(quiz))
            } catch (error) {
                setToast({ type: 'error', message: getApiError(error) })
            } finally {
                setLoading(false)
            }
        }
        loadQuiz()
    }, [mode, quizId, setToast])

    const submit = async (event) => {
        event.preventDefault()
        const validationError = validateQuizForm(form)
        if (validationError) {
            setToast({ type: 'error', message: validationError })
            return
        }

        const payload = {
            title: form.title.trim(),
            description: form.description.trim(),
            category: form.category.trim(),
            questions: form.questions.map((q) => ({
                question: q.question.trim(),
                options: q.options.map((o) => ({ answer: o.answer.trim(), correct: o.correct })),
            })),
        }

        try {
            setLoading(true)
            if (mode === 'edit') {
                await quizApi.update(quizId, form.ownerId, payload)
                setToast({ type: 'success', message: 'Quiz updated' })
            } else {
                await quizApi.create({ ...payload, ownerId: form.ownerId })
                setToast({ type: 'success', message: 'Quiz created' })
            }
            navigate('dashboard')
        } catch (error) {
            setToast({ type: 'error', message: getApiError(error) })
        } finally {
            setLoading(false)
        }
    }

    return (
        <section className="panel">
            <div className="page-heading">
                <div>
                    <span className="eyebrow">{mode === 'edit' ? 'Edit quiz' : 'Create quiz'}</span>
                    <h1>{mode === 'edit' ? 'Update your quiz' : 'Create a new quiz'}</h1>
                </div>
                <button className="btn" onClick={() => navigate('dashboard')}>Back</button>
            </div>
            <QuizForm form={form} setForm={setForm} mode={mode} onSubmit={submit} loading={loading} />
        </section>
    )
}
