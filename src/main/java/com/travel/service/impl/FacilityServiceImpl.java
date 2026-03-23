package com.travel.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.travel.algorithm.graph.Dijkstra;
import com.travel.algorithm.graph.Edge;
import com.travel.algorithm.graph.Graph;
import com.travel.algorithm.graph.PathResult;
import com.travel.mapper.FacilityMapper;
import com.travel.mapper.RoadMapper;
import com.travel.model.entity.Facility;
import com.travel.model.entity.Road;
import com.travel.model.vo.facility.FacilityNearbyVO;
import com.travel.service.FacilityService;
import com.travel.util.GeoUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * 设施查询服务实现。
 *
 * <p>
 * 说明：需求要求“按实际可达路径距离排序”。这里提供一个可运行的基础实现：
 * <ul>
 *     <li>先按直线距离过滤 radius 范围内的候选设施</li>
 *     <li>再用道路图计算从“用户位置最近的设施节点”到各候选设施的最短路径距离</li>
 * </ul>
 * 该实现不依赖外部地图服务，后续可替换为更精确的“最近道路节点/定位点映射”策略。
 * </p>
 */
@Service
public class FacilityServiceImpl implements FacilityService
{

    private final FacilityMapper facilityMapper;

    private final RoadMapper roadMapper;

    private final Dijkstra dijkstra;

    public FacilityServiceImpl(FacilityMapper facilityMapper, RoadMapper roadMapper)
    {
        this.facilityMapper = facilityMapper;
        this.roadMapper = roadMapper;
        this.dijkstra = new Dijkstra();
    }

    @Override
    public List<FacilityNearbyVO> nearby(double lat, double lng, int radius, String type, Long areaId)
    {
        int r = radius <= 0 ? 500 : radius;

        LambdaQueryWrapper<Facility> wrapper = new LambdaQueryWrapper<>();
        if (areaId != null)
        {
            wrapper.eq(Facility::getAreaId, areaId);
        }
        if (StringUtils.isNotBlank(type))
        {
            wrapper.eq(Facility::getType, type);
        }
        List<Facility> all = facilityMapper.selectList(wrapper);
        if (all.isEmpty())
        {
            return List.of();
        }

        // 先按直线距离筛选
        List<FacilityNearbyVO> candidates = new ArrayList<>();
        for (Facility f : all)
        {
            if (f.getLatitude() == null || f.getLongitude() == null)
            {
                continue;
            }
            double d = GeoUtil.distanceMeters(lat, lng, f.getLatitude(), f.getLongitude());
            if (d <= r)
            {
                FacilityNearbyVO vo = new FacilityNearbyVO();
                vo.setFacility(f);
                vo.setGeoDistance(d);
                candidates.add(vo);
            }
        }
        if (candidates.isEmpty())
        {
            return List.of();
        }

        // 用道路图估算“可达路径距离”
        Graph graph = loadGraph(areaId);
        Long startNode = findNearestFacilityNode(lat, lng, candidates);
        if (startNode != null)
        {
            for (FacilityNearbyVO vo : candidates)
            {
                Long endNode = vo.getFacility().getId();
                PathResult path = dijkstra.shortestPath(graph, startNode, endNode, Edge::getDistance, null);
                if (!path.getPath().isEmpty())
                {
                    vo.setPathDistance(path.getTotalWeight());
                }
            }
        }

        candidates.sort(Comparator.comparingDouble(v ->
        {
            if (v.getPathDistance() != null)
            {
                return v.getPathDistance();
            }
            return v.getGeoDistance() == null ? Double.MAX_VALUE : v.getGeoDistance();
        }));

        return candidates;
    }

    @Override
    public List<Facility> search(String keyword, String type, Long areaId, int limit)
    {
        int l = limit <= 0 ? 50 : Math.min(limit, 200);
        LambdaQueryWrapper<Facility> wrapper = new LambdaQueryWrapper<>();
        if (areaId != null)
        {
            wrapper.eq(Facility::getAreaId, areaId);
        }
        if (StringUtils.isNotBlank(type))
        {
            wrapper.eq(Facility::getType, type);
        }
        if (StringUtils.isNotBlank(keyword))
        {
            wrapper.and(w -> w.like(Facility::getName, keyword).or().like(Facility::getType, keyword));
        }
        wrapper.last("limit " + l);
        return facilityMapper.selectList(wrapper);
    }

    @Override
    public Facility detail(Long id)
    {
        Facility facility = facilityMapper.selectById(id);
        if (facility == null)
        {
            throw new IllegalArgumentException("设施不存在");
        }
        return facility;
    }

    private Graph loadGraph(Long areaId)
    {
        LambdaQueryWrapper<Road> wrapper = new LambdaQueryWrapper<>();
        if (areaId != null)
        {
            wrapper.eq(Road::getAreaId, areaId);
        }
        List<Road> roads = roadMapper.selectList(wrapper);
        Graph graph = new Graph();
        for (Road road : roads)
        {
            double distance = road.getDistance() == null ? 0.0 : road.getDistance();
            double speed = road.getSpeed() == null ? 0.0 : road.getSpeed();
            double congestion = road.getCongestion() == null ? 1.0 : road.getCongestion();
            graph.addUndirectedEdge(road.getStartId(), road.getEndId(), distance, speed, congestion, road.getVehicleType());
        }
        return graph;
    }

    private Long findNearestFacilityNode(double lat, double lng, List<FacilityNearbyVO> candidates)
    {
        FacilityNearbyVO nearest = null;
        for (FacilityNearbyVO vo : candidates)
        {
            if (nearest == null || (vo.getGeoDistance() != null && vo.getGeoDistance() < nearest.getGeoDistance()))
            {
                nearest = vo;
            }
        }
        return nearest == null ? null : nearest.getFacility().getId();
    }
}

