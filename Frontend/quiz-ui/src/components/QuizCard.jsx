import { Coins, Edit3, LockKeyhole, Play, Send, Trash2 } from 'lucide-react'

export default function QuizCard({ quiz, canManage, onView, onEdit, onDelete, onPublish, onUnlock }) {
    return (
        <article className="quiz-card">
            <div className="card-topline">
                <span className="pill">{quiz.category}</span>
                <span className={quiz.published ? 'status published' : 'status draft'}>
          {quiz.published ? 'Published' : 'Draft'}
                </span>
                {quiz.premium && <span className="status premium"><Coins size={13} /> {quiz.unlockPoints} points</span>}
            </div>

            <h3>{quiz.title}</h3>
            <p>{quiz.description || 'No description provided.'}</p>

            <div className="card-meta">
                <span>{quiz.questions?.length || 0} questions</span>
                <span>{quiz.premium ? 'Premium challenge' : 'Free to play'}</span>
            </div>

            <div className="card-actions">
                {quiz.premium && !quiz.unlocked && !canManage ? (
                    <button className="btn premium-btn" onClick={() => onUnlock(quiz)} disabled={!quiz.published}>
                        <LockKeyhole size={16} /> Unlock
                    </button>
                ) : (
                    <button className="btn primary" onClick={() => onView(quiz.id)} disabled={!quiz.published}>
                        <Play size={16} /> Play
                    </button>
                )}
                {canManage && <button className="btn" onClick={() => onEdit(quiz.id)}><Edit3 size={16} /> Edit</button>}
                {canManage && !quiz.published && (
                    <button className="btn success" onClick={() => onPublish(quiz.id)}>
                        <Send size={16} /> Publish
                    </button>
                )}
                {canManage && <button className="btn danger" onClick={() => onDelete(quiz)}>
                    <Trash2 size={16} /> Delete
                </button>}
            </div>
        </article>
    )
}
