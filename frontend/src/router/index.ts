import { createRouter, createWebHistory } from 'vue-router'
import { useAuthStore } from '../stores/auth'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/',
      redirect: '/home',
    },
    {
      path: '/login',
      component: () => import('../views/auth/LoginView.vue'),
      meta: { public: true },
    },
    {
      path: '/register',
      component: () => import('../views/auth/RegisterView.vue'),
      meta: { public: true },
    },
    {
      path: '/',
      component: () => import('../layouts/AppLayout.vue'),
      children: [
        { path: 'home', component: () => import('../views/HomeView.vue') },
        { path: 'scenic/:id', component: () => import('../views/scenic/ScenicDetailView.vue') },
        { path: 'route', component: () => import('../views/route/RoutePlannerView.vue') },
        { path: 'facility', component: () => import('../views/facility/FacilityView.vue') },
        { path: 'food', component: () => import('../views/food/FoodView.vue') },
        { path: 'food/:id', component: () => import('../views/food/FoodDetailView.vue') },
        { path: 'diary', component: () => import('../views/diary/DiaryListView.vue') },
        { path: 'diary/new', component: () => import('../views/diary/DiaryEditorView.vue'), meta: { requiresAuth: true } },
        { path: 'diary/:id', component: () => import('../views/diary/DiaryDetailView.vue') },
        { path: 'diary/:id/edit', component: () => import('../views/diary/DiaryEditorView.vue'), meta: { requiresAuth: true } },
        { path: 'admin', component: () => import('../views/admin/AdminView.vue'), meta: { requiresAuth: true, role: 'ADMIN' } },
        { path: 'profile', component: () => import('../views/profile/ProfileView.vue'), meta: { requiresAuth: true } },
      ],
    },
    {
      path: '/:pathMatch(.*)*',
      component: () => import('../views/NotFoundView.vue'),
      meta: { public: true },
    },
  ],
})

router.beforeEach((to) => {
  const auth = useAuthStore()
  if (to.meta.public) return true

  if (to.meta.requiresAuth && !auth.isAuthed) {
    return { path: '/login', query: { redirect: to.fullPath } }
  }

  const requiredRole = (to.meta.role as string | undefined) ?? undefined
  if (requiredRole && auth.user?.role?.toUpperCase() !== requiredRole.toUpperCase()) {
    return { path: '/home' }
  }
  return true
})

export default router

