<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { api } from '@/api/client'

const strategies = ref<any[]>([])
const loading = ref(false)

onMounted(async () => {
  loading.value = true
  try {
    const res = await api.get('/strategies')
    strategies.value = res.data || []
  } finally {
    loading.value = false
  }
})
</script>

<template>
  <div class="strategy-view">
    <h1>策略</h1>
    <p class="desc">创建并验证交易策略规则</p>
    <div v-if="loading">加载中...</div>
    <div v-else class="empty">暂无策略，后端 API 已就绪，可扩展创建表单</div>
  </div>
</template>

<style scoped>
.desc { color: var(--color-text-muted); margin-bottom: 1.5rem; }
.empty { color: var(--color-text-muted); padding: 2rem; }
</style>
