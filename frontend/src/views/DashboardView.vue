<script setup lang="ts">
import { ref } from 'vue'
</script>

<template>
  <div class="dashboard">
    <h1>欢迎使用 WCCAT</h1>
    <p class="subtitle">AI 化投资辅助平台</p>
    <div class="cards">
      <div class="card">
        <h3>资讯</h3>
        <p>拉取财经资讯，把握市场脉搏</p>
      </div>
      <div class="card">
        <h3>策略</h3>
        <p>验证交易策略，量化投资逻辑</p>
      </div>
      <div class="card">
        <h3>回测</h3>
        <p>自定义回测，验证历史收益</p>
      </div>
      <div class="card">
        <h3>券商</h3>
        <p>平安、华宝账户，持仓一目了然</p>
      </div>
      <div class="card">
        <h3>自选股</h3>
        <p>同步同花顺自选，统一管理</p>
      </div>
      <div class="card">
        <h3>报警</h3>
        <p>买卖提醒，不错过关键时机</p>
      </div>
    </div>
  </div>
</template>

<style scoped>
.dashboard h1 {
  font-size: 2rem;
  margin-bottom: 0.5rem;
}
.subtitle {
  color: var(--color-text-muted);
  margin-bottom: 2rem;
}
.cards {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 1.5rem;
}
.card {
  background: var(--color-surface);
  border: 1px solid var(--color-border);
  border-radius: var(--radius);
  padding: 1.5rem;
}
.card h3 {
  color: var(--color-primary);
  margin: 0 0 0.5rem;
}
.card p {
  color: var(--color-text-muted);
  margin: 0;
  font-size: 0.9rem;
}
</style>
