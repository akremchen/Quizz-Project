# Quiz Platform UI

React/Vite frontend for the Quiz Platform microservices project.

## Integrated features

- JWT registration, login, persistent sessions, logout, and automatic expiry handling
- Role-aware quiz ownership and administration controls
- Quiz discovery, category filtering, creation, editing, deletion, publishing, play, and submission
- Admin-only premium quiz settings and points-based premium unlocks
- Personal attempt history, achievement points, and badges
- Favorite category management
- Quiz publication, points, and badge notifications with unread status
- Responsive desktop and mobile layout

## Run locally

The backend gateway is expected at `http://localhost:8080/api`.

```bash
npm install
npm run dev
```

Override the API endpoint in `.env` when necessary:

```env
VITE_API_BASE_URL=http://localhost:8080/api
```

## Verification

```bash
npm run lint
npm run build
```
