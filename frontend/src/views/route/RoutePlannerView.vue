<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue'
import * as echarts from 'echarts'
import { apiMapData, apiPlanRoute, apiPlanRouteMulti } from '../../lib/api'

type Edge = {
  startId: number
  endId: number
  distance: number
  speed: number
  congestion: number
  vehicleType?: string
}

const loading = ref(false)
const map = ref<{ nodes: number[]; edges: Edge[] } | null>(null)
const chartEl = ref<HTMLDivElement | null>(null)
let chart: echarts.ECharts | null = null

const form = reactive({
  areaId: undefined as number | undefined,
  startId: 1,
  endId: 2,
  vehicle: 'walk',
  strategy: 'distance' as 'distance' | 'time',
  multiPoints: '1,2,3',
})

const result = ref<{ path: number[]; distance: number; time: number } | null>(null)

function renderGraph(highlightPath?: number[]) {
  if (!chartEl.value || !map.value) return
  if (!chart) chart = echarts.init(chartEl.value)

  const nodes = map.value.nodes.map((id) => ({
    id: String(id),
    name: String(id),
    symbolSize: highlightPath?.includes(id) ? 18 : 10,
    itemStyle: highlightPath?.includes(id)
      ? { color: 'rgba(34,211,238,0.95)' }
      : { color: 'rgba(124,58,237,0.85)' },
  }))

  const pathSet = new Set<string>()
  if (highlightPath && highlightPath.length > 1) {
    for (let i = 0; i < highlightPath.length - 1; i++) {
      pathSet.add(`${highlightPath[i]}-${highlightPath[i + 1]}`)
      pathSet.add(`${highlightPath[i + 1]}-${highlightPath[i]}`)
    }
  }

  const links = map.value.edges.map((e) => {
    const key = `${e.startId}-${e.endId}`
    const isOnPath = pathSet.has(key)
    return {
      source: String(e.startId),
      target: String(e.endId),
      value: e.distance,
      lineStyle: isOnPath
        ? { width: 4, color: 'rgba(34,211,238,0.95)' }
        : { width: 1, color: 'rgba(255,255,255,0.18)' },
    }
  })

  chart.setOption({
    tooltip: { trigger: 'item' },
    series: [
      {
        type: 'graph',
        layout: 'force',
        roam: true,
        draggable: true,
        label: { show: true, color: 'rgba(255,255,255,0.8)' },
        force: { repulsion: 90, edgeLength: [40, 120] },
        data: nodes,
        links,
      },
    ],
  })
}

async function loadMap() {
  loading.value = true
  try {
    map.value = await apiMapData({ areaId: form.areaId })
    result.value = null
    renderGraph()
  } finally {
    loading.value = false
  }
}

async function plan() {
  loading.value = true
  try {
    result.value = await apiPlanRoute({
      areaId: form.areaId,
      startId: Number(form.startId),
      endId: Number(form.endId),
      vehicle: form.vehicle,
      strategy: form.strategy,
    })
    renderGraph(result.value.path)
  } finally {
    loading.value = false
  }
}

async function planMulti() {
  const points = form.multiPoints
    .split(',')
    .map((s) => Number(s.trim()))
    .filter((n) => Number.isFinite(n))
  if (points.length < 2) return

  loading.value = true
  try {
    result.value = await apiPlanRouteMulti({
      areaId: form.areaId,
      points,
      vehicle: form.vehicle,
      strategy: form.strategy,
    })
    renderGraph(result.value.path)
  } finally {
    loading.value = false
  }
}

onMounted(loadMap)
</script>

<template>
  <div class="page">
    <div class="grid">
      <el-card class="glass" shadow="never">
        <template #header>
          <div style="font-weight: 900">路线规划</div>
        </template>

        <el-form label-position="top">
          <el-form-item label="areaId（可选）">
            <el-input-number v-model="form.areaId" :min="1" :controls="true" placeholder="不填则全局图" style="width: 100%" />
          </el-form-item>
          <el-form-item label="交通工具">
            <el-segmented
              v-model="form.vehicle"
              :options="[
                { label: '步行', value: 'walk' },
                { label: '自行车', value: 'bike' },
                { label: '电瓶车', value: 'shuttle' },
              ]"
            />
          </el-form-item>
          <el-form-item label="策略">
            <el-radio-group v-model="form.strategy">
              <el-radio-button label="distance">最短距离</el-radio-button>
              <el-radio-button label="time">最短时间</el-radio-button>
            </el-radio-group>
          </el-form-item>

          <div class="row">
            <el-form-item label="起点ID">
              <el-input-number v-model="form.startId" :min="1" style="width: 100%" />
            </el-form-item>
            <el-form-item label="终点ID">
              <el-input-number v-model="form.endId" :min="1" style="width: 100%" />
            </el-form-item>
          </div>

          <div class="actions">
            <el-button @click="loadMap" :loading="loading">刷新地图数据</el-button>
            <el-button type="primary" @click="plan" :loading="loading">两点规划</el-button>
          </div>

          <el-divider />

          <el-form-item label="多点规划（points 用逗号分隔）">
            <el-input v-model="form.multiPoints" placeholder="例如：1,2,3,4" />
          </el-form-item>
          <el-button type="primary" plain @click="planMulti" :loading="loading">多点规划</el-button>

          <div v-if="result" class="glass result">
            <div style="font-weight: 900">结果</div>
            <div class="muted">path：{{ result.path.join(' → ') }}</div>
            <div class="muted">distance：{{ result.distance.toFixed(2) }} m</div>
            <div class="muted">time：{{ result.time.toFixed(2) }} s</div>
          </div>
        </el-form>
      </el-card>

      <el-card class="glass" shadow="never">
        <template #header>
          <div style="display: flex; justify-content: space-between; align-items: center">
            <div style="font-weight: 900">节点/边可视化</div>
            <div class="muted" style="font-size: 12px">
              后端 <code>/api/route/map-data</code> 返回 nodes/edges（节点为ID列表）
            </div>
          </div>
        </template>
        <div ref="chartEl" class="chart" />
      </el-card>
    </div>
  </div>
</template>

<style scoped>
.grid {
  display: grid;
  grid-template-columns: 420px 1fr;
  gap: 12px;
}
.row {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 12px;
}
.actions {
  display: flex;
  gap: 10px;
  flex-wrap: wrap;
}
.result {
  margin-top: 12px;
  padding: 12px;
}
.chart {
  height: 560px;
  width: 100%;
}
@media (max-width: 1080px) {
  .grid {
    grid-template-columns: 1fr;
  }
  .chart {
    height: 420px;
  }
}
</style>

