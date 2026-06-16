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

    <div v-for="post in posts" :key="post.id" class="feed-card">
      <strong>{{ post.authorName }}</strong>
      <p>{{ post.text }}</p>
      <small>{{ new Date(post.createdAt).toLocaleString() }}</small>
      <hr />
    </div>
  </div>
</template>

<style scoped>
.feed-card {
  background: white;
  padding: 16px;
  border-radius: 16px;
  margin-bottom: 14px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
}

textarea {
  width: 100%;
  box-sizing: border-box;
  padding: 12px;
  border-radius: 12px;
  border: 1px solid #ccc;
  font-size: 16px;
}

button {
  width: 100%;
  margin-top: 10px;
  padding: 12px;
  border-radius: 12px;
  border: none;
  background: #2563eb;
  color: white;
  font-weight: bold;
}

.post-meta {
  font-size: 13px;
  color: #666;
}
</style>