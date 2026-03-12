<script setup lang="ts">
import { RouterLink, RouterView } from 'vue-router'
</script>

<template>
  <div class="layout">
    <aside class="sidebar">
      <div class="logo">WCCAT</div>
      <nav class="nav">
        <RouterLink to="/dashboard" active-class="active">首页</RouterLink>
        <RouterLink to="/news" active-class="active">资讯</RouterLink>
        <RouterLink to="/strategy" active-class="active">策略</RouterLink>
        <RouterLink to="/backtest" active-class="active">回测</RouterLink>
        <RouterLink to="/broker" active-class="active">券商账户</RouterLink>
        <RouterLink to="/watchlist" active-class="active">自选股</RouterLink>
        <RouterLink to="/alerts" active-class="active">报警规则</RouterLink>
      </nav>
    </aside>
    <main class="main">
      <RouterView />
    </main>
  </div>
</template>

<style scoped>
.layout {
  display: flex;
  min-height: 100vh;
}

.sidebar {
  width: 240px;
  background: var(--color-surface);
  border-right: 1px solid var(--color-border);
  padding: 1.5rem 0;
}

.logo {
  font-size: 1.5rem;
  font-weight: 700;
  color: var(--color-primary);
  padding: 0 1.5rem;
  margin-bottom: 2rem;
}

.nav {
  display: flex;
  flex-direction: column;
  gap: 0.25rem;
}

.nav a {
  padding: 0.75rem 1.5rem;
  color: var(--color-text-muted);
  transition: all 0.15s;
}

.nav a:hover {
  color: var(--color-text);
  background: var(--color-surface-elevated);
  text-decoration: none;
}

.nav a.active {
  color: var(--color-primary);
  background: rgba(0, 212, 170, 0.1);
  border-right: 3px solid var(--color-primary);
}

.main {
  flex: 1;
  padding: 2rem;
  overflow: auto;
}
</style>
