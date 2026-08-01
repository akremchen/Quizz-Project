import { useEffect, useMemo, useState } from 'react'
import { CheckCircle2 } from 'lucide-react'
import { getApiError, quizApi } from '../api/quizApi.js'
import { useAuth } from '../context/AuthContext.jsx'

export default function PlayQuiz({ quizId, navigate, setToast }) {
    const { user } = useAuth()
    const [quiz, setQuiz] = useState(null)
    const [answers, setAnswers] = useState({})
    const [result, setResult] = useState(null)
    const [loading, setLoading] = useState(true)

    useEffect(() => { quizApi.getById(quizId).then(setQuiz).catch((error) => setToast({ type: 'error', message: getApiError(error) })).finally(() => setLoading(false)) }, [quizId, setToast])
    const progress = useMemo(() => !quiz?.questions?.length ? 0 : Math.round((Object.keys(answers).length / quiz.questions.length) * 100), [answers, quiz])

    const submit = async () => {
        if (!quiz.questions.every((question) => answers[question.id])) return setToast({ type: 'error', message: 'Answer every question before submitting' })
        try {
            const payload = { userId: Number(user.id), answers: quiz.questions.map((question) => ({ questionId: question.id, selectedOptionId: answers[question.id] })) }
            setResult(await quizApi.submit(quiz.id, payload))
            setToast({ type: 'success', message: 'Quiz submitted. Your achievements will update shortly.' })
        } catch (error) { setToast({ type: 'error', message: getApiError(error) }) }
    }

    if (loading) return <div className="empty">Loading quiz...</div>
    if (!quiz) return <div className="empty">Quiz not found.</div>
    return <section className="panel"><div className="page-heading"><div><span className="eyebrow">Play quiz</span><h1>{quiz.title}</h1><p>{quiz.description}</p></div><button className="btn" onClick={() => navigate('dashboard')}>Back</button></div>{!quiz.published && <div className="warning">This quiz is still a draft.</div>}<div className="play-header"><span>Playing as <strong>{user.username}</strong></span><div className="progress"><span style={{ width: `${progress}%` }} /></div><strong>{progress}% complete</strong></div>{quiz.questions.map((question, index) => <div className="question-player" key={question.id}><h3>{index + 1}. {question.question}</h3><div className="answer-grid">{question.options.map((option) => <button type="button" key={option.id} className={answers[question.id] === option.id ? 'answer selected' : 'answer'} onClick={() => setAnswers((current) => ({ ...current, [question.id]: option.id }))}>{option.answer}</button>)}</div></div>)}{!result ? <button className="btn primary large" onClick={submit}><CheckCircle2 size={18} /> Submit quiz</button> : <div className="result-card"><h2>Result</h2><strong>{result.correctAnswers} / {result.totalQuestions}</strong><p>Score: {result.score}</p><p>Points and badge notifications may arrive in a moment.</p></div>}</section>
}
