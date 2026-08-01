import { Edit3, Play, Send, Trash2 } from 'lucide-react'
import { quizApi } from "../api/quizApi.js";

export default function QuizCard({ quiz, onView, onEdit, onDelete, onPublish }) {
    return (
        <article className="quiz-card">
            <div className="card-topline">
                <span className="pill">{quiz.category}</span>
                <span className={quiz.published ? 'status published' : 'status draft'}>
          {quiz.published ? 'Published' : 'Draft'}
        </span>
            </div>

            <h3>{quiz.title}</h3>
            <p>{quiz.description || 'No description provided.'}</p>

            <div className="card-meta">
                <span>{quiz.questions?.length || 0} questions</span>
                <span>Owner #{quiz.ownerId}</span>
            </div>

            <div className="card-actions">
                <button className="btn primary" onClick={() => onView(quiz.id)} disabled={!quiz.published}>
                    <Play size={16} /> Play
                </button>
                <button className="btn" onClick={() => onEdit(quiz.id)}>
                    <Edit3 size={16} /> Edit
                </button>
                {!quiz.published && (
                    <button className="btn success" onClick={() => onPublish(quiz.id)}>
                        <Send size={16} /> Publish
                    </button>
                )}
                <button className="btn danger" onClick={() => onDelete(quiz)}>
                    <Trash2 size={16} /> Delete
                </button>
            </div>
        </article>
    )
}
