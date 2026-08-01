export const emptyQuiz = {
    ownerId: 1,
    title: '',
    description: '',
    category: '',
    questions: [
        {
            question: '',
            options: [
                { answer: '', correct: true },
                { answer: '', correct: false },
            ],
        },
    ],
}

export function quizToForm(quiz) {
    return {
        ownerId: quiz.ownerId || 1,
        title: quiz.title || '',
        description: quiz.description || '',
        category: quiz.category || '',
        questions: (quiz.questions || []).map((q) => ({
            question: q.question || '',
            options: (q.options || []).map((o, index) => ({
                answer: o.answer || '',
                correct: index === 0,
            })),
        })),
    }
}

export function validateQuizForm(form) {
    if (!form.ownerId) return 'Owner ID is required'
    if (!form.title.trim()) return 'Title is required'
    if (!form.category.trim()) return 'Category is required'
    if (!form.questions.length) return 'At least one question is required'

    for (const [qIndex, question] of form.questions.entries()) {
        if (!question.question.trim()) return `Question ${qIndex + 1} is empty`
        if (question.options.length < 2) return `Question ${qIndex + 1} needs at least 2 options`
        if (!question.options.some((o) => o.correct)) return `Question ${qIndex + 1} needs a correct answer`

        for (const [oIndex, option] of question.options.entries()) {
            if (!option.answer.trim()) return `Option ${oIndex + 1} in question ${qIndex + 1} is empty`
        }
    }

    return null
}
