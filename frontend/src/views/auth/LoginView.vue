<script setup lang="ts">
import { reactive, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { apiLogin } from '../../lib/api'
import { useAuthStore } from '../../stores/auth'

const route = useRoute()
const router = useRouter()
const auth = useAuthStore()

const loading = ref(false)
const form = reactive({
  username: '',
  password: '',
})

async function submit() {
  loading.value = true
  try {
    const data = await apiLogin({ username: form.username, password: form.password })
    auth.setAuth(data.token, data.user)
    ElMessage.success('登录成功')
    const redirect = (route.query.redirect as string) || '/home'
    router.replace(redirect)
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <div class="auth">
    <div class="panel glass">
      <div class="hero">
        <div class="h1">欢迎回来</div>
        <div class="muted">登录后可使用个性化推荐、评分、写日记等功能。</div>
      </div>

      <el-form label-position="top" class="form" @submit.prevent="submit">
        <el-form-item label="用户名">
          <el-input v-model="form.username" placeholder="请输入用户名" />
        </el-form-item>
        <el-form-item label="密码">
          <el-input v-model="form.password" type="password" show-password placeholder="请输入密码" />
        </el-form-item>

        <el-button type="primary" size="large" :loading="loading" style="width: 100%" @click="submit">
          登录
        </el-button>
        <div class="muted links">
          没有账号？
          <a @click.prevent="$router.push('/register')">去注册</a>
        </div>
      </el-form>
    </div>
  </div>
</template>

<style scoped>
.auth {
  min-height: 100vh;
  display: grid;
  place-items: center;
  padding: 18px;
  box-sizing: border-box;
}
.panel {
  width: min(980px, 100%);
  display: grid;
  grid-template-columns: 1.2fr 1fr;
  gap: 18px;
  padding: 18px;
}
.hero {
  padding: 18px;
  border-radius: 16px;
  background:
    radial-gradient(380px 240px at 20% 20%, rgba(124, 58, 237, 0.32), transparent 65%),
    radial-gradient(380px 240px at 80% 40%, rgba(34, 211, 238, 0.22), transparent 65%),
    linear-gradient(180deg, rgba(255, 255, 255, 0.08), rgba(255, 255, 255, 0.03));
  border: 1px solid rgba(255, 255, 255, 0.12);
}
.h1 {
  font-size: 34px;
  font-weight: 800;
  margin-bottom: 8px;
}
.form {
  padding: 18px;
}
.links {
  margin-top: 10px;
  font-size: 13px;
}
.links a {
  color: rgba(34, 211, 238, 0.95);
  cursor: pointer;
  text-decoration: none;
}
@media (max-width: 860px) {
  .panel {
    grid-template-columns: 1fr;
  }
}
</style>

