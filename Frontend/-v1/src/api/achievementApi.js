import axios from 'axios'

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api'

const client = axios.create({
    baseURL: API_BASE_URL,
    headers: { 'Content-Type': 'application/json' },
})

export const achievementApi = {
    ping: () => client.get('/achievement/ping').then((res) => res.data),

    quizCompleted: (payload) =>
        client.post('/achievement/quiz-completed', payload).then((res) => res.data),

    getPoints: (userId) =>
        client.get(`/achievement/${userId}/points`).then((res) => res.data),

    getBadges: (userId) =>
        client.get(`/achievement/${userId}/badges`).then((res) => res.data),
}

export function getAchievementApiError(error) {
    return error?.response?.data?.message || error?.response?.data?.error || error?.message || 'Something went wrong'
}