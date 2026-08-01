import { useCallback, useEffect, useState } from 'react'
import { Bell, Check, RefreshCw } from 'lucide-react'
import { notificationApi } from '../api/notificationApi.js'
import { getApiError } from '../api/client.js'
import { useAuth } from '../context/AuthContext.jsx'

export default function Notifications({ setToast, onUnreadChange }) {
    const { user } = useAuth()
    const [notifications, setNotifications] = useState([])
    const [loading, setLoading] = useState(true)
    const load = useCallback(async () => { try { setLoading(true); const data = await notificationApi.getAll(user.id); setNotifications(data); onUnreadChange(data.filter((item) => !item.read && !item.isRead).length) } catch (error) { setToast({ type: 'error', message: getApiError(error) }) } finally { setLoading(false) } }, [user.id, onUnreadChange, setToast])
    useEffect(() => { load() }, [load])
    const markRead = async (notification) => { try { await notificationApi.markRead(notification.id); setNotifications((current) => current.map((item) => item.id === notification.id ? { ...item, read: true, isRead: true } : item)); onUnreadChange((current) => Math.max(0, current - 1)) } catch (error) { setToast({ type: 'error', message: getApiError(error) }) } }
    return <section className="panel"><div className="page-heading"><div><span className="eyebrow">Activity center</span><h1>Notifications</h1><p>Quiz publications, earned points, and new badges appear here.</p></div><button className="btn" onClick={load}><RefreshCw size={16} /> Refresh</button></div>{loading ? <div className="empty">Loading notifications...</div> : <div className="notification-list">{notifications.map((notification) => { const isRead = notification.read || notification.isRead; return <article className={`notification-card ${isRead ? '' : 'unread'}`} key={notification.id}><span className="notification-icon"><Bell size={18} /></span><div><div className="notification-heading"><h3>{notification.title || notification.type?.replaceAll('_', ' ') || 'Notification'}</h3>{!isRead && <span className="unread-dot" />}</div><p>{notification.message}</p><small>{notification.createdAt ? new Date(notification.createdAt).toLocaleString() : ''}</small></div>{!isRead && <button className="btn compact" onClick={() => markRead(notification)}><Check size={15} /> Mark read</button>}</article> })}{!notifications.length && <div className="empty">You have no notifications yet.</div>}</div>}</section>
}
