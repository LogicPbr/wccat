import { createRouter, createWebHistory } from 'vue-router'
import type { RouteRecordRaw } from 'vue-router'
import AppLayout from '@/layouts/AppLayout.vue'

const routes: RouteRecordRaw[] = [
  {
    path: '/',
    component: AppLayout,
    children: [
      { path: '', redirect: '/dashboard' },
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('@/views/DashboardView.vue'),
        meta: { title: '首页' }
      },
      {
        path: 'news',
        name: 'News',
        component: () => import('@/views/news/NewsView.vue'),
        meta: { title: '资讯' }
      },
      {
        path: 'strategy',
        name: 'Strategy',
        component: () => import('@/views/strategy/StrategyView.vue'),
        meta: { title: '策略' }
      },
      {
        path: 'backtest',
        name: 'Backtest',
        component: () => import('@/views/backtest/BacktestView.vue'),
        meta: { title: '回测' }
      },
      {
        path: 'broker',
        name: 'Broker',
        component: () => import('@/views/broker/BrokerView.vue'),
        meta: { title: '券商账户' }
      },
      {
        path: 'watchlist',
        name: 'Watchlist',
        component: () => import('@/views/watchlist/WatchlistView.vue'),
        meta: { title: '自选股' }
      },
      {
        path: 'alerts',
        name: 'Alerts',
        component: () => import('@/views/alerts/AlertsView.vue'),
        meta: { title: '报警规则' }
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes
})

router.afterEach((to) => {
  document.title = to.meta.title ? `${to.meta.title} - WCCAT` : 'WCCAT'
})

export default router
