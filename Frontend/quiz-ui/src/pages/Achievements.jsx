import { useCallback, useEffect, useState } from 'react'
import { Award, RefreshCw, Sparkles, Star, Trophy } from 'lucide-react'
import { achievementApi, getAchievementApiError } from '../api/achievementApi.js'
import { useAuth } from '../context/AuthContext.jsx'

export default function Achievements() {
    const { user } = useAuth()
    const [points, setPoints] = useState(0)
    const [badges, setBadges] = useState([])
    const [loading, setLoading] = useState(false)
    const [error, setError] = useState('')
    const loadAchievements = useCallback(async () => {
        try {
            setLoading(true); setError('')
            const [pointsData, badgesData] = await Promise.all([achievementApi.getPoints(user.id), achievementApi.getBadges(user.id)])
            setPoints(Number(pointsData?.points ?? pointsData ?? 0))
            setBadges(Array.isArray(badgesData) ? badgesData : badgesData?.badges || [])
        } catch (err) { setError(getAchievementApiError(err)) } finally { setLoading(false) }
    }, [user.id])
    useEffect(() => { loadAchievements() }, [loadAchievements])
    return <div className="page"><div className="hero"><div><p className="eyebrow">Your progress</p><h1>Points and badges</h1><p>Points are awarded automatically after quiz submissions and can unlock premium quizzes.</p></div><button className="btn" onClick={loadAchievements} disabled={loading}><RefreshCw size={18} /> {loading ? 'Loading...' : 'Refresh'}</button></div>{error && <div className="error-box">{error}</div>}<div className="stats-grid"><div className="stat-card accent-stat"><Star size={30} /><span>Available Points</span><strong>{points}</strong></div><div className="stat-card"><Trophy size={30} /><span>Badges Earned</span><strong>{badges.length}</strong></div></div><div className="section-title"><div><span className="eyebrow">Collection</span><h2>Your badges</h2></div><Sparkles /></div><div className="badge-grid">{badges.length === 0 ? <div className="empty">No badges earned yet.</div> : badges.map((badge, index) => { const label = typeof badge === 'string' ? badge : badge.name || badge.badgeName || badge.type || `Badge ${index + 1}`; return <div className="badge-card" key={badge.id || label}><span className="badge-icon"><Award size={22} /></span><div><strong>{label}</strong>{badge.earnedAt && <small>{new Date(badge.earnedAt).toLocaleDateString()}</small>}</div></div> })}</div></div>
}
