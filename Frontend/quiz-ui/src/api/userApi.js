import { client } from './client.js'

const asList = (data) => Array.isArray(data) ? data : data?.favoriteCategories || data?.categories || []

export const userApi = {
    getFavoriteCategories: () => client.get('/users/me/favorite-categories').then((res) => asList(res.data)),
    addFavoriteCategory: (category) => client.post('/users/me/favorite-categories', { category }).then((res) => res.data),
    removeFavoriteCategory: (category) => client.delete(`/users/me/favorite-categories/${encodeURIComponent(category)}`).then((res) => res.data),
}
