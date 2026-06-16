<script setup lang="ts">
import { useRouter } from 'vue-router'
import { useAuthStore } from './stores/auth'

const router = useRouter()
const authStore = useAuthStore()

const logout = async () => {
  authStore.logout()
  await router.push('/login')
}
</script>

<template>
  <div class="app-shell">
    <header class="top-bar">
      <h1>Stecken-Rein</h1>
    </header>

    <main class="content">
      <router-view />
    </main>

    <nav class="bottom-nav">
      <router-link to="/">Home</router-link>

      <router-link v-if="authStore.isLoggedIn" to="/feed">
        Feed
      </router-link>

      <router-link
        v-if="authStore.currentUser?.role === 'ADMIN'"
        to="/admin"
      >
        Admin
      </router-link>

      <router-link v-if="!authStore.isLoggedIn" to="/login">
        Login
      </router-link>

      <router-link v-if="!authStore.isLoggedIn" to="/register">
        Register
      </router-link>

      <button v-if="authStore.isLoggedIn" @click="logout">
        Logout
      </button>
    </nav>
  </div>
</template>

<style scoped>
.app-shell {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  background: #f4f5f7;
}

.top-bar {
  position: sticky;
  top: 0;
  z-index: 10;
  background: white;
  padding: 14px 16px;
  border-bottom: 1px solid #ddd;
}

.top-bar h1 {
  margin: 0;
  font-size: 20px;
}

.content {
  flex: 1;
  padding: 16px;
  padding-bottom: 80px;
}

.bottom-nav {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  display: flex;
  gap: 8px;
  justify-content: space-around;
  align-items: center;
  background: white;
  border-top: 1px solid #ddd;
  padding: 10px;
}

.bottom-nav a,
.bottom-nav button {
  border: none;
  background: none;
  font-size: 14px;
  text-decoration: none;
  color: #333;
}

.bottom-nav a.router-link-active {
  font-weight: bold;
  color: #2563eb;
}
</style>