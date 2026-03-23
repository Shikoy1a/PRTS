<script setup lang="ts">
import { computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessageBox } from 'element-plus'
import {
  Compass,
  Location,
  ForkSpoon,
  EditPen,
  Setting,
  User,
  SwitchButton,
  Star,
} from '@element-plus/icons-vue'
import { useAuthStore } from '../stores/auth'

const route = useRoute()
const router = useRouter()
const auth = useAuthStore()

const active = computed(() => route.path)

async function logout() {
  await ElMessageBox.confirm('确认退出登录？', '提示', { type: 'warning' })
  auth.clear()
  router.push('/home')
}
</script>

<template>
  <div class="shell">
    <aside class="sidebar glass">
      <div class="brand">
        <div class="logo" />
        <div class="name">
          <div class="title">Travel System</div>
          <div class="sub muted">路线 · 推荐 · 日记 · 美食</div>
        </div>
      </div>

      <el-menu :default-active="active" router class="menu" background-color="transparent">
        <el-menu-item index="/home">
          <el-icon><Compass /></el-icon>
          <span>推荐景区</span>
        </el-menu-item>
        <el-menu-item index="/route">
          <el-icon><Location /></el-icon>
          <span>路线规划</span>
        </el-menu-item>
        <el-menu-item index="/facility">
          <el-icon><Setting /></el-icon>
          <span>设施查询</span>
        </el-menu-item>
        <el-menu-item index="/food">
          <el-icon><ForkSpoon /></el-icon>
          <span>美食推荐</span>
        </el-menu-item>
        <el-menu-item index="/diary">
          <el-icon><EditPen /></el-icon>
          <span>旅游日记</span>
        </el-menu-item>
        <el-menu-item v-if="auth.user?.role?.toUpperCase() === 'ADMIN'" index="/admin">
          <el-icon><Star /></el-icon>
          <span>管理员</span>
        </el-menu-item>
      </el-menu>

      <div class="side-footer muted">
        <div>后端端口：8080</div>
        <div>前端端口：5173</div>
      </div>
    </aside>

    <main class="main">
      <header class="topbar glass">
        <div class="left">
          <div class="crumb">{{ route.meta.title || route.path }}</div>
        </div>
        <div class="right">
          <template v-if="auth.isAuthed">
            <el-button text @click="$router.push('/profile')">
              <el-icon><User /></el-icon>
              <span style="margin-left: 6px">{{ auth.user?.username }}</span>
            </el-button>
            <el-button text @click="logout">
              <el-icon><SwitchButton /></el-icon>
              <span style="margin-left: 6px">退出</span>
            </el-button>
          </template>
          <template v-else>
            <el-button type="primary" @click="$router.push('/login')">登录</el-button>
            <el-button @click="$router.push('/register')">注册</el-button>
          </template>
        </div>
      </header>

      <div class="content">
        <router-view />
      </div>
    </main>
  </div>
</template>

<style scoped>
.shell {
  display: grid;
  grid-template-columns: 290px 1fr;
  gap: 14px;
  padding: 14px;
  min-height: 100vh;
  box-sizing: border-box;
}

.sidebar {
  padding: 14px 12px;
  display: flex;
  flex-direction: column;
  min-height: calc(100vh - 28px);
}

.brand {
  display: flex;
  gap: 10px;
  align-items: center;
  padding: 10px 10px 14px;
}

.logo {
  width: 42px;
  height: 42px;
  border-radius: 14px;
  background:
    radial-gradient(18px 18px at 30% 30%, rgba(255, 255, 255, 0.35), transparent 65%),
    linear-gradient(135deg, rgba(124, 58, 237, 0.95), rgba(34, 211, 238, 0.9));
  box-shadow: 0 18px 30px rgba(0, 0, 0, 0.35);
}

.title {
  font-weight: 700;
  letter-spacing: 0.4px;
}

.sub {
  font-size: 12px;
}

.menu {
  border-right: none;
  flex: 1;
}

.side-footer {
  padding: 10px 10px 4px;
  font-size: 12px;
}

.main {
  display: flex;
  flex-direction: column;
  gap: 14px;
}

.topbar {
  padding: 10px 14px;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.crumb {
  font-weight: 600;
}

.content {
  flex: 1;
  min-height: 0;
}

@media (max-width: 980px) {
  .shell {
    grid-template-columns: 1fr;
  }
  .sidebar {
    min-height: auto;
  }
}
</style>

