<template>
  <div class="page">
    <div class="glass box">
      <div class="title">404</div>
      <div class="muted">页面不存在，可能是路由地址写错了。</div>
      <div style="margin-top: 14px">
        <el-button type="primary" @click="$router.push('/home')">回到首页</el-button>
      </div>
    </div>
  </div>
</template>

<style scoped>
.box {
  padding: 26px;
  max-width: 720px;
}
.title {
  font-size: 44px;
  font-weight: 800;
  letter-spacing: 1px;
}
</style>

