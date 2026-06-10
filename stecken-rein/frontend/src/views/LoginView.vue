<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '../stores/auth'

const router = useRouter()
const authStore = useAuthStore()

const email = ref('')
const password = ref('')
const message = ref('')

const login = async () => {
  try {
    await authStore.login(email.value, password.value)
    await router.push('/feed')
    } catch (error: any) {
    console.error(error)
    message.value =
      error.response?.data?.message ||
      error.response?.data ||
      'Login failed'
  }
}
</script>

<template>
  <div>
    <h1>Login</h1>

    <input v-model="email" placeholder="Email" />
    <br /><br />

    <input v-model="password" placeholder="Password" type="password" />
    <br /><br />

    <button @click="login">Login</button>

    <p>{{ message }}</p>
  </div>
</template>