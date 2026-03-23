<script setup lang="ts">
import { reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { apiRegister } from '../../lib/api'

const loading = ref(false)
const form = reactive({
  username: '',
  password: '',
  password2: '',
})

async function submit() {
  if (!form.username || !form.password) {
    ElMessage.warning('请输入用户名和密码')
    return
  }
  if (form.password !== form.password2) {
    ElMessage.warning('两次密码不一致')
    return
  }
  loading.value = true
  try {
    await apiRegister({ username: form.username, password: form.password })
    ElMessage.success('注册成功，请登录')
    location.href = '/login'
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <div class="auth">
    <div class="panel glass">
      <div class="hero">
        <div class="h1">创建账号</div>
        <div class="muted">
          注册后可体验路线规划、设施查询、美食推荐、热门/个性化景区推荐，以及日记发布与评分。
        </div>
      </div>

      <el-form label-position="top" class="form" @submit.prevent="submit">
        <el-form-item label="用户名">
          <el-input v-model="form.username" placeholder="例如：alice" />
        </el-form-item>
        <el-form-item label="密码">
          <el-input v-model="form.password" type="password" show-password placeholder="请输入密码" />
        </el-form-item>
        <el-form-item label="确认密码">
          <el-input v-model="form.password2" type="password" show-password placeholder="再次输入密码" />
        </el-form-item>

        <el-button type="primary" size="large" :loading="loading" style="width: 100%" @click="submit">
          注册
        </el-button>
        <div class="muted links">
          已有账号？
          <a @click.prevent="$router.push('/login')">去登录</a>
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
    radial-gradient(380px 240px at 20% 20%, rgba(251, 113, 133, 0.25), transparent 65%),
    radial-gradient(380px 240px at 80% 40%, rgba(34, 211, 238, 0.18), transparent 65%),
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
  color: rgba(124, 58, 237, 0.95);
  cursor: pointer;
  text-decoration: none;
}
@media (max-width: 860px) {
  .panel {
    grid-template-columns: 1fr;
  }
}
</style>

