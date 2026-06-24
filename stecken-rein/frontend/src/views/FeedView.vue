<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { useAuthStore } from '../stores/auth'
import api from '../services/api'

type Post = {
  id: number
  authorId: number
  authorName: string
  text: string
  imagePath: string | null
  createdAt: string
}

const authStore = useAuthStore()

const posts = ref<Post[]>([])
const newPostText = ref('')
const message = ref('')

const selectedImage = ref<File | null>(null)

const onImageSelected = (event: Event) => {
  const input = event.target as HTMLInputElement
  selectedImage.value = input.files?.[0] ?? null
}

const loadPosts = async () => {
  const response = await api.get('/posts')
  posts.value = response.data
}

const createPost = async () => {
  if (!newPostText.value.trim() && !selectedImage.value) {
    message.value = 'Please write something or choose a photo.'
    return
  }

  const formData = new FormData()
  formData.append('text', newPostText.value)

  if (selectedImage.value) {
    formData.append('image', selectedImage.value)
  }

  await api.post('/posts/with-image', formData, {
    headers: {
      'Content-Type': 'multipart/form-data',
    },
  })

  newPostText.value = ''
  selectedImage.value = null
  message.value = ''

  await loadPosts()
}

const deletePost = async (id: number) => {

  if (!confirm("Delete this post?"))
    return

  await api.delete(`/posts/${id}`)

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

    <input type="file" accept="image/*" @change="onImageSelected" />

    <br /><br />

    <button @click="createPost">Post</button>

    <p>{{ message }}</p>

    <hr />

    <div v-for="post in posts" :key="post.id" class="feed-card">
      <strong>{{ post.authorName }}</strong>
      <div v-if="post.imagePath" class="post-image-wrapper">
        <img
          :src="`http://localhost:8090${post.imagePath}`"
          class="post-image"
        />
      </div>
      <p>{{ post.text }}</p>
      <small>{{ new Date(post.createdAt).toLocaleString() }}</small>

      <div class="post-actions">
        <button
          v-if="post.authorId === authStore.currentUser?.id"
          class="delete-button"
          @click="deletePost(post.id)"
        >
          🗑 Delete
        </button>
      </div>
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

.post-image-wrapper {
  width: 100%;
  aspect-ratio: 1 / 1;
  border-radius: 14px;
  overflow: hidden;
  background: #f3f4f6;
  margin-top: 12px;
}

.post-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
  display: block;
}

.post-actions{
    display:flex;
    justify-content:flex-end;
    margin-top:12px;
}

.delete-button{

    background:#dc2626;
    color:white;

    width:auto;

    padding:8px 18px;

    border-radius:10px;
}
</style>