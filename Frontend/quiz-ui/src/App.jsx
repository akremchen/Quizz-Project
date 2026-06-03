import { useState } from 'react'
import { BookOpenCheck, History, Home, Plus } from 'lucide-react'
import Dashboard from './pages/Dashboard.jsx'
import CreateEditQuiz from './pages/CreateEditQuiz.jsx'
import PlayQuiz from './pages/PlayQuiz.jsx'
import Attempts from './pages/Attempts.jsx'
import Toast from './components/Toast.jsx'

export default function App() {
  const [route, setRoute] = useState({ page: 'dashboard', id: null })
  const [toast, setToast] = useState(null)

  const navigate = (page, id = null) => setRoute({ page, id })

  return (
      <div className="app-shell">
        <aside className="sidebar">
          <div className="brand"><BookOpenCheck /><span>Quiz Platform</span></div>
          <nav>
            <button className={route.page === 'dashboard' ? 'active' : ''} onClick={() => navigate('dashboard')}><Home size={18} /> Dashboard</button>
            <button className={route.page === 'create' ? 'active' : ''} onClick={() => navigate('create')}><Plus size={18} /> Create Quiz</button>
            <button className={route.page === 'attempts' ? 'active' : ''} onClick={() => navigate('attempts')}><History size={18} /> Attempts</button>
          </nav>
          <div className="sidebar-note">API: <code>/api/quizzes</code></div>
        </aside>

        <main className="content">
          {route.page === 'dashboard' && <Dashboard navigate={navigate} setToast={setToast} />}
          {route.page === 'create' && <CreateEditQuiz mode="create" navigate={navigate} setToast={setToast} />}
          {route.page === 'edit' && <CreateEditQuiz mode="edit" quizId={route.id} navigate={navigate} setToast={setToast} />}
          {route.page === 'play' && <PlayQuiz quizId={route.id} navigate={navigate} setToast={setToast} />}
          {route.page === 'attempts' && <Attempts setToast={setToast} />}
        </main>

        <Toast message={toast?.message} type={toast?.type} onClose={() => setToast(null)} />
      </div>
  )
}
