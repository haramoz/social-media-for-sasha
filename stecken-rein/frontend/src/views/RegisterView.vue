<script setup lang="ts">
import { ref } from 'vue'
import api from '../services/api'

const firstName = ref('')
const lastName = ref('')
const email = ref('')
const password = ref('')

const message = ref('')

const register = async () => {
  try {
    const response = await api.post('/auth/register', {
      firstName: firstName.value,
      lastName: lastName.value,
      email: email.value,
      password: password.value,
    })

    message.value = response.data
  } catch (error) {
    message.value = 'Registration failed'
  }
}
</script>

<template>
  <div>
    <h1>Register</h1>

    <input v-model="firstName" placeholder="First Name" />
    <br /><br />

    <input v-model="lastName" placeholder="Last Name" />
    <br /><br />

    <input v-model="email" placeholder="Email" />
    <br /><br />

    <input
      v-model="password"
      placeholder="Password"
      type="password"
    />
    <br /><br />

    <button @click="register">
      Register
    </button>

    <p>{{ message }}</p>
  </div>
</template>