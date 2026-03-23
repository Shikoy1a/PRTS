<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue'
import { apiFacilityDetail, apiFacilityNearby, apiFacilitySearch, type Facility, type FacilityNearbyVO } from '../../lib/api'

const tab = ref<'nearby' | 'search'>('nearby')
const loading = ref(false)

const nearbyForm = reactive({
  lat: 30.0,
  lng: 120.0,
  radius: 500,
  type: '',
  areaId: undefined as number | undefined,
})
const nearbyList = ref<FacilityNearbyVO[]>([])

const searchForm = reactive({
  keyword: '',
  type: '',
  areaId: undefined as number | undefined,
  limit: 50,
})
const searchList = ref<Facility[]>([])

const detail = ref<Facility | null>(null)
const detailOpen = ref(false)

async function loadNearby() {
  loading.value = true
  try {
    nearbyList.value = await apiFacilityNearby({
      lat: nearbyForm.lat,
      lng: nearbyForm.lng,
      radius: nearbyForm.radius,
      type: nearbyForm.type || undefined,
      areaId: nearbyForm.areaId,
    })
  } finally {
    loading.value = false
  }
}

async function loadSearch() {
  loading.value = true
  try {
    searchList.value = await apiFacilitySearch({
      keyword: searchForm.keyword || undefined,
      type: searchForm.type || undefined,
      areaId: searchForm.areaId,
      limit: searchForm.limit,
    })
  } finally {
    loading.value = false
  }
}

async function openDetail(id: number) {
  detail.value = await apiFacilityDetail(id)
  detailOpen.value = true
}

onMounted(loadNearby)
</script>

<template>
  <div class="page">
    <el-card class="glass" shadow="never">
      <template #header>
        <div style="font-weight: 900">设施查询</div>
      </template>

      <el-tabs v-model="tab" @tab-change="(n: string | number)=>{ if(n==='nearby') loadNearby(); else loadSearch(); }">
        <el-tab-pane label="附近设施" name="nearby">
          <div class="formRow">
            <el-input-number v-model="nearbyForm.lat" :step="0.0001" controls-position="right" placeholder="lat" />
            <el-input-number v-model="nearbyForm.lng" :step="0.0001" controls-position="right" placeholder="lng" />
            <el-input-number v-model="nearbyForm.radius" :min="50" :step="50" placeholder="radius(m)" />
            <el-input v-model="nearbyForm.type" placeholder="type(可选)" clearable />
            <el-input-number v-model="nearbyForm.areaId" :min="1" placeholder="areaId(可选)" />
            <el-button type="primary" :loading="loading" @click="loadNearby">查询</el-button>
          </div>

          <el-table :data="nearbyList" v-loading="loading" style="width: 100%; margin-top: 12px" @row-click="(r: FacilityNearbyVO)=>openDetail(r.id)">
            <el-table-column prop="id" label="ID" width="80" />
            <el-table-column prop="name" label="名称" />
            <el-table-column prop="type" label="类型" width="120" />
            <el-table-column prop="distance" label="距离(m)" width="120" />
            <el-table-column prop="location" label="位置" />
          </el-table>
        </el-tab-pane>

        <el-tab-pane label="搜索" name="search">
          <div class="formRow">
            <el-input v-model="searchForm.keyword" placeholder="keyword(可选)" clearable />
            <el-input v-model="searchForm.type" placeholder="type(可选)" clearable />
            <el-input-number v-model="searchForm.areaId" :min="1" placeholder="areaId(可选)" />
            <el-input-number v-model="searchForm.limit" :min="1" :max="200" placeholder="limit" />
            <el-button type="primary" :loading="loading" @click="loadSearch">查询</el-button>
          </div>

          <el-table :data="searchList" v-loading="loading" style="width: 100%; margin-top: 12px" @row-click="(r: Facility)=>openDetail(r.id)">
            <el-table-column prop="id" label="ID" width="80" />
            <el-table-column prop="name" label="名称" />
            <el-table-column prop="type" label="类型" width="120" />
            <el-table-column prop="location" label="位置" />
          </el-table>
        </el-tab-pane>
      </el-tabs>
    </el-card>

    <el-drawer v-model="detailOpen" :with-header="false" size="420px" @close="detail=null">
      <div v-if="detail" class="drawer glass">
        <div style="font-weight: 900; font-size: 18px">{{ detail.name }}</div>
        <div class="muted" style="margin-top: 6px">{{ detail.type }}</div>
        <el-divider />
        <div class="muted">位置：{{ detail.location || '—' }}</div>
        <div class="muted">经纬度：{{ detail.latitude ?? '—' }}, {{ detail.longitude ?? '—' }}</div>
        <div style="margin-top: 10px">{{ detail.description || '暂无描述' }}</div>
      </div>
    </el-drawer>
  </div>
</template>

<style scoped>
.formRow {
  display: flex;
  gap: 10px;
  flex-wrap: wrap;
  align-items: center;
}
.drawer {
  padding: 16px;
  height: 100%;
  box-sizing: border-box;
}
</style>

