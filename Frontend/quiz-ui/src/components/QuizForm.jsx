import { Plus, Save, Trash2 } from 'lucide-react'
import { quizApi } from "../api/quizApi.js";


export default function QuizForm({ form, setForm, mode = 'create', onSubmit, loading }) {
    const updateField = (field, value) => setForm((prev) => ({ ...prev, [field]: value }))

    const updateQuestion = (questionIndex, field, value) => {
        setForm((prev) => ({
            ...prev,
            questions: prev.questions.map((q, i) => (i === questionIndex ? { ...q, [field]: value } : q)),
        }))
    }

    const updateOption = (questionIndex, optionIndex, field, value) => {
        setForm((prev) => ({
            ...prev,
            questions: prev.questions.map((q, i) => {
                if (i !== questionIndex) return q
                return {
                    ...q,
                    options: q.options.map((o, j) => (j === optionIndex ? { ...o, [field]: value } : o)),
                }
            }),
        }))
    }

    const markCorrect = (questionIndex, optionIndex) => {
        setForm((prev) => ({
            ...prev,
            questions: prev.questions.map((q, i) => {
                if (i !== questionIndex) return q
                return { ...q, options: q.options.map((o, j) => ({ ...o, correct: j === optionIndex })) }
            }),
        }))
    }

    const addQuestion = () => {
        setForm((prev) => ({
            ...prev,
            questions: [...prev.questions, { question: '', options: [{ answer: '', correct: true }, { answer: '', correct: false }] }],
        }))
    }

    const removeQuestion = (questionIndex) => {
        setForm((prev) => ({ ...prev, questions: prev.questions.filter((_, i) => i !== questionIndex) }))
    }

    const addOption = (questionIndex) => {
        setForm((prev) => ({
            ...prev,
            questions: prev.questions.map((q, i) =>
                i === questionIndex ? { ...q, options: [...q.options, { answer: '', correct: false }] } : q,
            ),
        }))
    }

    const removeOption = (questionIndex, optionIndex) => {
        setForm((prev) => ({
            ...prev,
            questions: prev.questions.map((q, i) => {
                if (i !== questionIndex) return q
                const nextOptions = q.options.filter((_, j) => j !== optionIndex)
                if (!nextOptions.some((o) => o.correct) && nextOptions[0]) nextOptions[0].correct = true
                return { ...q, options: nextOptions }
            }),
        }))
    }

    return (
        <form className="quiz-form" onSubmit={onSubmit}>
            <div className="form-grid">
                <label>
                    Owner ID
                    <input type="number" min="1" value={form.ownerId} onChange={(e) => updateField('ownerId', Number(e.target.value))} />
                </label>
                <label>
                    Category
                    <input value={form.category} onChange={(e) => updateField('category', e.target.value)} placeholder="Java, Spring, Math..." />
                </label>
            </div>

            <label>
                Title
                <input value={form.title} onChange={(e) => updateField('title', e.target.value)} placeholder="Quiz title" />
            </label>

            <label>
                Description
                <textarea value={form.description} onChange={(e) => updateField('description', e.target.value)} placeholder="Short description" />
            </label>

            <div className="section-title">
                <h2>Questions</h2>
                <button type="button" className="btn" onClick={addQuestion}><Plus size={16} /> Add question</button>
            </div>

            {form.questions.map((question, questionIndex) => (
                <div className="question-editor" key={questionIndex}>
                    <div className="question-header">
                        <h3>Question {questionIndex + 1}</h3>
                        {form.questions.length > 1 && (
                            <button type="button" className="icon-btn danger-text" onClick={() => removeQuestion(questionIndex)}>
                                <Trash2 size={18} />
                            </button>
                        )}
                    </div>

                    <input value={question.question} onChange={(e) => updateQuestion(questionIndex, 'question', e.target.value)} placeholder="Write your question" />

                    <div className="options-list">
                        {question.options.map((option, optionIndex) => (
                            <div className="option-row" key={optionIndex}>
                                <input
                                    type="radio"
                                    checked={option.correct}
                                    onChange={() => markCorrect(questionIndex, optionIndex)}
                                    title="Correct answer"
                                />
                                <input value={option.answer} onChange={(e) => updateOption(questionIndex, optionIndex, 'answer', e.target.value)} placeholder={`Option ${optionIndex + 1}`} />
                                {question.options.length > 2 && (
                                    <button type="button" className="icon-btn" onClick={() => removeOption(questionIndex, optionIndex)}>×</button>
                                )}
                            </div>
                        ))}
                    </div>

                    <button type="button" className="btn ghost" onClick={() => addOption(questionIndex)}><Plus size={16} /> Add option</button>
                </div>
            ))}

            <button className="btn primary large" disabled={loading}>
                <Save size={18} /> {loading ? 'Saving...' : mode === 'edit' ? 'Update quiz' : 'Create quiz'}
            </button>
        </form>
    )
}
