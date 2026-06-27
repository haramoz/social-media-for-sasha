import { createRouter, createWebHistory } from 'vue-router'

import HomeView from '../views/HomeView.vue'
import LoginView from '../views/LoginView.vue'
import RegisterView from '../views/RegisterView.vue'
import FeedView from '../views/FeedView.vue'
import AdminView from '../views/AdminView.vue'
import EventsView from '../views/EventsView.vue'
import ChatListView from '../views/ChatListView.vue'
import ChatView from '../views/ChatView.vue'

import { useAuthStore } from '../stores/auth'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    { path: '/', component: HomeView },
    { path: '/login', component: LoginView },
    { path: '/register', component: RegisterView },
    {
      path: '/feed',
      component: FeedView,
      meta: { requiresAuth: true },
    },
    {
      path: '/admin',
      component: AdminView,
      meta: { requiresAuth: true },
    },
    {
      path: '/events',
      component: EventsView,
      meta: { requiresAuth: true },
    },
    {
      path: '/chat',
      component: ChatListView,
      meta: { requiresAuth: true },
    },
    {
      path: '/chat/:neighborId',
      component: ChatView,
      meta: { requiresAuth: true },
    }
  ],
})

router.beforeEach((to) => {
  const authStore = useAuthStore()

  if (to.meta.requiresAuth && !authStore.isLoggedIn) {
    return '/login'
  }
})

export default router