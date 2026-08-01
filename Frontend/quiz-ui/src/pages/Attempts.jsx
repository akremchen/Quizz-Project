import { useCallback, useEffect, useState } from 'react'
import { RefreshCw } from 'lucide-react'
import { getApiError, quizApi } from '../api/quizApi.js'
import { useAuth } from '../context/AuthContext.jsx'

export default function Attempts({ setToast }) {
    const { user } = useAuth()
    const [attempts, setAttempts] = useState([])
    const [loading, setLoading] = useState(false)
    const loadAttempts = useCallback(async () => {
        try { setLoading(true); setAttempts(await quizApi.attemptsByUser(user.id)) }
        catch (error) { setToast({ type: 'error', message: getApiError(error) }) }
        finally { setLoading(false) }
    }, [user.id, setToast])
    useEffect(() => { loadAttempts() }, [loadAttempts])
    return <section className="panel"><div className="page-heading"><div><span className="eyebrow">Attempt history</span><h1>Your quiz attempts</h1><p>Every submitted quiz for {user.username}.</p></div><button className="btn" onClick={loadAttempts}><RefreshCw size={16} /> Refresh</button></div><div className="table-card table-scroll"><table><thead><tr><th>Quiz</th><th>Score</th><th>Correct</th><th>Total</th><th>Submitted</th></tr></thead><tbody>{attempts.map((attempt) => <tr key={attempt.id}><td>#{attempt.quizId}</td><td>{attempt.score}</td><td>{attempt.correctAnswers}</td><td>{attempt.totalQuestions}</td><td>{attempt.submittedAt ? new Date(attempt.submittedAt).toLocaleString() : '-'}</td></tr>)}</tbody></table>{!loading && !attempts.length && <div className="empty small">No attempts yet. Play your first quiz.</div>}{loading && <div className="empty small">Loading attempts...</div>}</div></section>
}
