import { useCallback, useEffect, useState } from 'react'
import { Heart, Plus, Trash2 } from 'lucide-react'
import { userApi } from '../api/userApi.js'
import { getApiError } from '../api/client.js'

export default function Favorites({ setToast }) {
    const [categories, setCategories] = useState([])
    const [category, setCategory] = useState('')
    const [loading, setLoading] = useState(true)
    const load = useCallback(async () => { try { setLoading(true); setCategories(await userApi.getFavoriteCategories()) } catch (error) { setToast({ type: 'error', message: getApiError(error) }) } finally { setLoading(false) } }, [setToast])
    useEffect(() => { load() }, [load])
    const add = async (event) => { event.preventDefault(); const value = category.trim(); if (!value) return; try { await userApi.addFavoriteCategory(value); setCategory(''); setToast({ type: 'success', message: `${value} added to favorites` }); load() } catch (error) { setToast({ type: 'error', message: getApiError(error) }) } }
    const remove = async (value) => { try { await userApi.removeFavoriteCategory(value); setCategories((current) => current.filter((item) => item !== value)); setToast({ type: 'success', message: `${value} removed` }) } catch (error) { setToast({ type: 'error', message: getApiError(error) }) } }
    return <section className="panel"><div className="page-heading"><div><span className="eyebrow">Personalized discovery</span><h1>Favorite categories</h1><p>You will receive a notification whenever a new quiz is published in one of these categories.</p></div><Heart size={32} className="page-icon" /></div><form className="inline-form" onSubmit={add}><input value={category} onChange={(e) => setCategory(e.target.value)} placeholder="Java, Spring, Math..." /><button className="btn primary"><Plus size={16} /> Add category</button></form>{loading ? <div className="empty">Loading favorites...</div> : <div className="favorite-grid">{categories.map((value) => <div className="favorite-card" key={value}><Heart size={18} fill="currentColor" /><strong>{value}</strong><button className="icon-btn" onClick={() => remove(value)} aria-label={`Remove ${value}`}><Trash2 size={16} /></button></div>)}{!categories.length && <div className="empty">No favorite categories yet.</div>}</div>}</section>
}
