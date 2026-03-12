<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { api } from '@/api/client'

interface News {
  id: number
  title: string
  content: string
  source: string
  publishTime: string
}

const newsList = ref<News[]>([])
const loading = ref(false)

async function fetchNews() {
  loading.value = true
  try {
    const res = await api.get<{ success: boolean; data: News[] }>('/news/list')
    newsList.value = res.data || []
  } finally {
    loading.value = false
  }
}

function refresh() {
  api.post('/news/refresh').then(() => fetchNews())
}

onMounted(fetchNews)
</script>

<template>
  <div class="news-view">
    <div class="header">
      <h1>资讯</h1>
      <button class="btn-primary" :disabled="loading" @click="refresh">刷新</button>
    </div>
    <div v-if="loading" class="loading">加载中...</div>
    <ul v-else class="news-list">
      <li v-for="n in newsList" :key="n.id" class="news-item">
        <h4>{{ n.title }}</h4>
        <p>{{ n.content }}</p>
        <span class="meta">{{ n.source }} · {{ n.publishTime }}</span>
      </li>
      <li v-if="!loading && newsList.length === 0" class="empty">暂无资讯，点击刷新拉取</li>
    </ul>
  </div>
</template>

<style scoped>
.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1.5rem;
}
.btn-primary {
  background: var(--color-primary);
  color: var(--color-bg);
  border: none;
  padding: 0.5rem 1rem;
  border-radius: var(--radius);
  cursor: pointer;
}
.btn-primary:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}
.news-list {
  list-style: none;
  padding: 0;
  margin: 0;
}
.news-item {
  background: var(--color-surface);
  border: 1px solid var(--color-border);
  border-radius: var(--radius);
  padding: 1rem;
  margin-bottom: 0.75rem;
}
.news-item h4 { margin: 0 0 0.5rem; }
.news-item p {
  color: var(--color-text-muted);
  font-size: 0.9rem;
  margin: 0 0 0.5rem;
}
.meta {
  font-size: 0.8rem;
  color: var(--color-text-muted);
  opacity: 0.8;
}
.empty { color: var(--color-text-muted); text-align: center; padding: 2rem; }
.loading { color: var(--color-text-muted); padding: 2rem; }
</style>
