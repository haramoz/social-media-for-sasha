<script setup lang="ts">
import { useAuthStore } from './stores/auth'

const authStore = useAuthStore()
</script>

<template>
  <nav>
    <router-link to="/">Home</router-link>
    |
    <router-link v-if="authStore.isLoggedIn" to="/feed">Feed</router-link>
    <span v-if="authStore.isLoggedIn"> | </span>

    <router-link v-if="!authStore.isLoggedIn" to="/login">Login</router-link>
    <span v-if="!authStore.isLoggedIn"> | </span>

    <router-link v-if="!authStore.isLoggedIn" to="/register">Register</router-link>

    <router-link v-if="authStore.currentUser?.role === 'ADMIN'" to="/admin">Admin</router-link>

    <button v-if="authStore.isLoggedIn" @click="authStore.logout()">
      Logout
    </button>
  </nav>

  <hr />

  <router-view />
</template>