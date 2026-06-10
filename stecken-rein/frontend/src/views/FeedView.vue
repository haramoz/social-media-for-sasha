<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { useAuthStore } from '../stores/auth'
import api from '../services/api'

type Post = {
  id: number
  authorId: number
  authorName: string
  text: string
  createdAt: string
}

const authStore = useAuthStore()

const posts = ref<Post[]>([])
const newPostText = ref('')
const message = ref('')

const loadPosts = async () => {
  const response = await api.get('/posts')
  posts.value = response.data
}

const createPost = async () => {
  if (!newPostText.value.trim()) {
    message.value = 'Please write something first.'
    return
  }

  await api.post('/posts', {
  text: newPostText.value,
  })

  newPostText.value = ''
  message.value = ''
  await loadPosts()
}

onMounted(loadPosts)
</script>

<template>
  <div>
    <h1>Neighborhood Feed</h1>

    <p>Welcome, {{ authStore.currentUser?.firstName }}.</p>

    <textarea
      v-model="newPostText"
      placeholder="Share something with your neighbors..."
      rows="4"
      style="width: 100%; max-width: 600px"
    />

    <br /><br />

    <button @click="createPost">Post</button>

    <p>{{ message }}</p>

    <hr />

    <div v-for="post in posts" :key="post.id">
      <strong>{{ post.authorName }}</strong>
      <p>{{ post.text }}</p>
      <small>{{ new Date(post.createdAt).toLocaleString() }}</small>
      <hr />
    </div>
  </div>
</template>