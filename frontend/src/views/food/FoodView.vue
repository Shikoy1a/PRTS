<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue'
import { apiFoodRecommendation, apiFoodSearch, type Food, type FoodRecommendVO } from '../../lib/api'

const loading = ref(false)
const tab = ref<'recommend' | 'search'>('recommend')

const rec = reactive({
  areaId: 1,
  lat: undefined as number | undefined,
  lng: undefined as number | undefined,
  radius: 1000,
  wHeat: 0.3,
  wRating: 0.5,
  wDistance: 0.2,
  page: 1,
  size: 10,
})
const recList = ref<FoodRecommendVO[]>([])

const q = reactive({
  keyword: '',
  cuisine: '',
  areaId: undefined as number | undefined,
  page: 1,
  size: 10,
})
const list = ref<Food[]>([])

async function loadRec() {
  loading.value = true
  try {
    recList.value = await apiFoodRecommendation({
      areaId: rec.areaId,
      lat: rec.lat,
      lng: rec.lng,
      radius: rec.radius,
      wHeat: rec.wHeat,
      wRating: rec.wRating,
      wDistance: rec.wDistance,
      page: rec.page,
      size: rec.size,
    })
  } finally {
    loading.value = false
  }
}

async function loadSearch() {
  loading.value = true
  try {
    list.value = await apiFoodSearch({
      keyword: q.keyword || undefined,
      cuisine: q.cuisine || undefined,
      areaId: q.areaId,
      page: q.page,
      size: q.size,
    })
  } finally {
    loading.value = false
  }
}

onMounted(loadRec)
</script>

<template>
  <div class="page">
    <el-card class="glass" shadow="never">
      <template #header>
        <div style="font-weight: 900">美食</div>
      </template>

      <el-tabs v-model="tab" @tab-change="(n: string | number)=>{ if(n==='recommend') loadRec(); else loadSearch(); }">
        <el-tab-pane label="推荐" name="recommend">
          <div class="formRow">
            <el-input-number v-model="rec.areaId" :min="1" placeholder="areaId" />
            <el-input-number v-model="rec.lat" :step="0.0001" placeholder="lat(可选)" />
            <el-input-number v-model="rec.lng" :step="0.0001" placeholder="lng(可选)" />
            <el-input-number v-model="rec.radius" :min="50" :step="50" placeholder="radius" />
            <el-input-number v-model="rec.wHeat" :step="0.1" :min="0" :max="1" />
            <el-input-number v-model="rec.wRating" :step="0.1" :min="0" :max="1" />
            <el-input-number v-model="rec.wDistance" :step="0.1" :min="0" :max="1" />
            <el-button type="primary" :loading="loading" @click="loadRec">获取推荐</el-button>
          </div>

          <el-table :data="recList" v-loading="loading" style="width: 100%; margin-top: 12px" @row-click="(r: FoodRecommendVO)=>$router.push(`/food/${r.id}`)">
            <el-table-column prop="id" label="ID" width="80" />
            <el-table-column prop="name" label="名称" />
            <el-table-column prop="cuisine" label="菜系" width="120" />
            <el-table-column prop="price" label="价格" width="120" />
            <el-table-column prop="rating" label="评分" width="120" />
            <el-table-column prop="heat" label="热度" width="120" />
          </el-table>
        </el-tab-pane>

        <el-tab-pane label="搜索" name="search">
          <div class="formRow">
            <el-input v-model="q.keyword" placeholder="keyword(可选)" clearable />
            <el-input v-model="q.cuisine" placeholder="cuisine(可选)" clearable />
            <el-input-number v-model="q.areaId" :min="1" placeholder="areaId(可选)" />
            <el-button type="primary" :loading="loading" @click="loadSearch">搜索</el-button>
          </div>
          <el-table :data="list" v-loading="loading" style="width: 100%; margin-top: 12px" @row-click="(r: Food)=>$router.push(`/food/${r.id}`)">
            <el-table-column prop="id" label="ID" width="80" />
            <el-table-column prop="name" label="名称" />
            <el-table-column prop="cuisine" label="菜系" width="120" />
            <el-table-column prop="price" label="价格" width="120" />
            <el-table-column prop="rating" label="评分" width="120" />
            <el-table-column prop="heat" label="热度" width="120" />
          </el-table>
        </el-tab-pane>
      </el-tabs>
    </el-card>
  </div>
</template>

<style scoped>
.formRow {
  display: flex;
  gap: 10px;
  flex-wrap: wrap;
  align-items: center;
}
</style>

