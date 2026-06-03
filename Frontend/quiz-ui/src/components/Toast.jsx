import { quizApi } from "../api/quizApi.js";

export default function Toast({ message, type = 'info', onClose }) {
    if (!message) return null
    return (
        <div className={`toast toast-${type}`}>
            <span>{message}</span>
            <button onClick={onClose} aria-label="Close message">×</button>
        </div>
    )
}
