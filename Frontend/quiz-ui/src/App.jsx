import { useCallback, useEffect, useState } from 'react'
import { Award, Bell, BookOpenCheck, Heart, History, Home, LogOut, Plus, ShieldCheck } from 'lucide-react'
import Dashboard from './pages/Dashboard.jsx'
import CreateEditQuiz from './pages/CreateEditQuiz.jsx'
import PlayQuiz from './pages/PlayQuiz.jsx'
import Attempts from './pages/Attempts.jsx'
import Achievements from './pages/Achievements.jsx'
import Favorites from './pages/Favorites.jsx'
import Notifications from './pages/Notifications.jsx'
import AuthPage from './pages/AuthPage.jsx'
import Toast from './components/Toast.jsx'
import { useAuth } from './context/AuthContext.jsx'
import { notificationApi } from './api/notificationApi.js'
import { UserRound } from 'lucide-react'
import Profile from './pages/Profile.jsx'

const navItems = [
    { page: 'dashboard', label: 'Discover', icon: Home },
    { page: 'create', label: 'Create Quiz', icon: Plus },
    { page: 'attempts', label: 'My Attempts', icon: History },
    { page: 'achievements', label: 'Achievements', icon: Award },
    { page: 'favorites', label: 'Favorites', icon: Heart },
    { page: 'notifications', label: 'Notifications', icon: Bell },
    { page: 'profile', label: 'Profile', icon: UserRound },
]

export default function App() {
    const { user, isAuthenticated, isAdmin, logout } = useAuth()
    const [route, setRoute] = useState({ page: 'dashboard', id: null })
    const [toast, setToast] = useState(null)
    const [unread, setUnread] = useState(0)
    const navigate = (page, id = null) => { setRoute({ page, id }); window.scrollTo({ top: 0, behavior: 'smooth' }) }
    const updateUnread = useCallback((value) => setUnread(value), [])

    useEffect(() => {
        if (!user?.id) return
        notificationApi.getUnread(user.id).then((items) => setUnread(items.length)).catch(() => {})
    }, [user?.id])

    if (!isAuthenticated) return <AuthPage />

    return (
        <div className="app-shell">
            <aside className="sidebar">
                <div className="brand"><span className="brand-icon"><BookOpenCheck /></span><div><span>Quiz Platform</span><small>Challenge yourself</small></div></div>
                <nav>{navItems.map(({ page, label, icon: Icon }) => <button key={page} className={route.page === page ? 'active' : ''} onClick={() => navigate(page)}><Icon size={18} /> {label}{page === 'notifications' && unread > 0 && <span className="nav-count">{unread > 99 ? '99+' : unread}</span>}</button>)}</nav>
                <div className="account-card"><div className="avatar">{(user.username || user.email || 'U').slice(0, 1).toUpperCase()}</div><div><strong>{user.username || 'Quiz player'}</strong><span>{user.email}</span><small>{isAdmin && <ShieldCheck size={12} />} {user.role || 'USER'}</small></div></div>
                <button className="logout-button" onClick={logout}><LogOut size={17} /> Sign out</button>
            </aside>
            <main className="content">
                {route.page === 'dashboard' && <Dashboard navigate={navigate} setToast={setToast} />}
                {route.page === 'create' && <CreateEditQuiz mode="create" navigate={navigate} setToast={setToast} />}
                {route.page === 'edit' && <CreateEditQuiz mode="edit" quizId={route.id} navigate={navigate} setToast={setToast} />}
                {route.page === 'play' && <PlayQuiz quizId={route.id} navigate={navigate} setToast={setToast} />}
                {route.page === 'attempts' && <Attempts setToast={setToast} />}
                {route.page === 'achievements' && <Achievements />}
                {route.page === 'favorites' && <Favorites setToast={setToast} />}
                {route.page === 'notifications' && <Notifications setToast={setToast} onUnreadChange={updateUnread} />}
                {route.page === 'profile' && (
                    <Profile setToast={setToast} />
                )}
            </main>
            <Toast message={toast?.message} type={toast?.type} onClose={() => setToast(null)} />
        </div>
    )
}
