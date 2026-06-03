import { useState } from 'react'
import { Award, RefreshCw, Star, Trophy } from 'lucide-react'
import { achievementApi, getAchievementApiError } from '../api/achievementApi'

export default function Achievements() {
    const [userId, setUserId] = useState(1)
    const [points, setPoints] = useState(0)
    const [badges, setBadges] = useState([])
    const [loading, setLoading] = useState(false)
    const [error, setError] = useState('')

    async function loadAchievements() {
        try {
            setLoading(true)
            setError('')

            const [pointsData, badgesData] = await Promise.all([
                achievementApi.getPoints(userId),
                achievementApi.getBadges(userId),
            ])

            setPoints(pointsData.points || 0)
            setBadges(badgesData.badges || [])
        } catch (err) {
            setError(getAchievementApiError(err))
        } finally {
            setLoading(false)
        }
    }

    return (
        <div className="page">
            <div className="hero">
                <div>
                    <p className="eyebrow">Achievement Service</p>
                    <h1>Points and badges</h1>
                    <p>Track user progress after quiz submissions.</p>
                </div>
            </div>

            <div className="toolbar">
                <input
                    type="number"
                    min="1"
                    value={userId}
                    onChange={(e) => setUserId(e.target.value)}
                    placeholder="User ID"
                />

                <button className="btn primary" onClick={loadAchievements} disabled={loading}>
                    <RefreshCw size={18} />
                    {loading ? 'Loading...' : 'Load'}
                </button>
            </div>

            {error && <div className="error-box">{error}</div>}

            <div className="stats-grid">
                <div className="stat-card">
                    <Star size={30} />
                    <span>Total Points</span>
                    <strong>{points}</strong>
                </div>

                <div className="stat-card">
                    <Trophy size={30} />
                    <span>Total Badges</span>
                    <strong>{badges.length}</strong>
                </div>
            </div>

            <div className="badge-grid">
                {badges.length === 0 ? (
                    <div className="empty-state">No badges earned yet.</div>
                ) : (
                    badges.map((badge) => (
                        <div className="badge-card" key={badge}>
                            <Award size={22} />
                            <span>{badge}</span>
                        </div>
                    ))
                )}
            </div>
        </div>
    )
}