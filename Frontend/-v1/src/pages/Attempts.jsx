import { useState } from 'react'
import { getApiError, quizApi } from '../api/quizApi.js'

export default function Attempts({ setToast }) {
    const [userId, setUserId] = useState(1)
    const [attempts, setAttempts] = useState([])
    const [loading, setLoading] = useState(false)

    const loadAttempts = async () => {
        try {
            setLoading(true)
            const data = await quizApi.attemptsByUser(userId)
            setAttempts(data)
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
                    <span className="eyebrow">Attempt history</span>
                    <h1>User quiz attempts</h1>
                </div>
            </div>
            <div className="toolbar">
                <input type="number" min="1" value={userId} onChange={(e) => setUserId(e.target.value)} />
                <button className="btn primary" onClick={loadAttempts}>{loading ? 'Loading...' : 'Load attempts'}</button>
            </div>
            <div className="table-card">
                <table>
                    <thead><tr><th>Quiz</th><th>Score</th><th>Correct</th><th>Total</th><th>Submitted</th></tr></thead>
                    <tbody>
                    {attempts.map((attempt) => (
                        <tr key={attempt.id}>
                            <td>#{attempt.quizId}</td>
                            <td>{attempt.score}</td>
                            <td>{attempt.correctAnswers}</td>
                            <td>{attempt.totalQuestions}</td>
                            <td>{attempt.submittedAt ? new Date(attempt.submittedAt).toLocaleString() : '-'}</td>
                        </tr>
                    ))}
                    </tbody>
                </table>
                {!attempts.length && <div className="empty small">No attempts loaded.</div>}
            </div>
        </section>
    )
}
