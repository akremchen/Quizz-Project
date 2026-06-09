import { useEffect, useMemo, useState } from 'react'
import { CheckCircle2 } from 'lucide-react'
import { getApiError, quizApi } from '../api/quizApi.js'

export default function PlayQuiz({ quizId, navigate, setToast }) {
    const [quiz, setQuiz] = useState(null)
    const [userId, setUserId] = useState(1)
    const [answers, setAnswers] = useState({})
    const [result, setResult] = useState(null)
    const [loading, setLoading] = useState(true)

    useEffect(() => {
        const loadQuiz = async () => {
            try {
                setLoading(true)
                const data = await quizApi.getById(quizId)
                setQuiz(data)
            } catch (error) {
                setToast({ type: 'error', message: getApiError(error) })
            } finally {
                setLoading(false)
            }
        }

        loadQuiz()
    }, [quizId, setToast])

    const progress = useMemo(() => {
        if (!quiz?.questions?.length) return 0
        return Math.round((Object.keys(answers).length / quiz.questions.length) * 100)
    }, [answers, quiz])

    const submit = async () => {
        if (!quiz.questions.every((q) => answers[q.id])) {
            setToast({ type: 'error', message: 'Answer every question before submitting' })
            return
        }

        try {
            const payload = {
                userId: Number(userId),
                answers: quiz.questions.map((q) => ({
                    questionId: q.id,
                    selectedOptionId: answers[q.id],
                })),
            }

            const data = await quizApi.submit(quiz.id, payload)

            setResult(data)
            setToast({ type: 'success', message: 'Quiz submitted' })
        } catch (error) {
            setToast({ type: 'error', message: getApiError(error) })
        }
    }

    if (loading) return <div className="empty">Loading quiz...</div>
    if (!quiz) return <div className="empty">Quiz not found.</div>

    return (
        <section className="panel">
            <div className="page-heading">
                <div>
                    <span className="eyebrow">Play quiz</span>
                    <h1>{quiz.title}</h1>
                    <p>{quiz.description}</p>
                </div>

                <button className="btn" onClick={() => navigate('dashboard')}>
                    Back
                </button>
            </div>

            {!quiz.published && (
                <div className="warning">
                    This quiz is not published. The backend will reject submissions.
                </div>
            )}

            <div className="play-header">
                <label>
                    User ID
                    <input
                        type="number"
                        min="1"
                        value={userId}
                        onChange={(e) => setUserId(e.target.value)}
                    />
                </label>

                <div className="progress">
                    <span style={{ width: `${progress}%` }} />
                </div>

                <strong>{progress}% complete</strong>
            </div>

            {quiz.questions.map((question, index) => (
                <div className="question-player" key={question.id}>
                    <h3>
                        {index + 1}. {question.question}
                    </h3>

                    <div className="answer-grid">
                        {question.options.map((option) => (
                            <button
                                type="button"
                                key={option.id}
                                className={answers[question.id] === option.id ? 'answer selected' : 'answer'}
                                onClick={() =>
                                    setAnswers((prev) => ({
                                        ...prev,
                                        [question.id]: option.id,
                                    }))
                                }
                            >
                                {option.answer}
                            </button>
                        ))}
                    </div>
                </div>
            ))}

            {!result ? (
                <button className="btn primary large" onClick={submit}>
                    <CheckCircle2 size={18} /> Submit quiz
                </button>
            ) : (
                <div className="result-card">
                    <h2>Result</h2>
                    <strong>
                        {result.correctAnswers} / {result.totalQuestions}
                    </strong>
                    <p>Score: {result.score}</p>
                    <p>User #{result.userId}</p>
                </div>
            )}
        </section>
    )
}