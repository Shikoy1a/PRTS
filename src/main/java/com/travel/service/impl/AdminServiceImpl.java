package com.travel.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.travel.common.PageData;
import com.travel.mapper.BuildingMapper;
import com.travel.mapper.FoodMapper;
import com.travel.mapper.RoadMapper;
import com.travel.mapper.ScenicAreaMapper;
import com.travel.model.entity.Building;
import com.travel.model.entity.Food;
import com.travel.model.entity.Road;
import com.travel.model.entity.ScenicArea;
import com.travel.service.AdminService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 管理端服务实现。
 */
@Service
public class AdminServiceImpl implements AdminService
{

    private final ScenicAreaMapper scenicAreaMapper;
    private final BuildingMapper buildingMapper;
    private final RoadMapper roadMapper;
    private final FoodMapper foodMapper;

    public AdminServiceImpl(ScenicAreaMapper scenicAreaMapper,
                            BuildingMapper buildingMapper,
                            RoadMapper roadMapper,
                            FoodMapper foodMapper)
    {
        this.scenicAreaMapper = scenicAreaMapper;
        this.buildingMapper = buildingMapper;
        this.roadMapper = roadMapper;
        this.foodMapper = foodMapper;
    }

    @Override
    public ScenicArea addScenicArea(ScenicArea scenicArea)
    {
        LocalDateTime now = LocalDateTime.now();
        scenicArea.setCreateTime(now);
        scenicArea.setUpdateTime(now);
        scenicAreaMapper.insert(scenicArea);
        return scenicArea;
    }

    @Override
    public PageData<ScenicArea> listScenicAreas(Integer page, Integer size, String type)
    {
        int p = page == null || page < 1 ? 1 : page;
        int s = size == null || size <= 0 ? 10 : Math.min(size, 50);
        int offset = (p - 1) * s;

        LambdaQueryWrapper<ScenicArea> wrapper = new LambdaQueryWrapper<>();
        if (type != null && !type.isBlank())
        {
            wrapper.eq(ScenicArea::getType, type);
        }
        wrapper.orderByDesc(ScenicArea::getCreateTime);
        Long total = scenicAreaMapper.selectCount(wrapper);
        wrapper.last("limit " + offset + "," + s);
        List<ScenicArea> list = scenicAreaMapper.selectList(wrapper);
        return new PageData<>(list, total);
    }

    @Override
    public Building addBuilding(Building building)
    {
        LocalDateTime now = LocalDateTime.now();
        building.setCreateTime(now);
        building.setUpdateTime(now);
        buildingMapper.insert(building);
        return building;
    }

    @Override
    public Road addRoad(Road road)
    {
        LocalDateTime now = LocalDateTime.now();
        road.setCreateTime(now);
        road.setUpdateTime(now);
        roadMapper.insert(road);
        return road;
    }

    @Override
    public Food addFood(Food food)
    {
        LocalDateTime now = LocalDateTime.now();
        food.setCreateTime(now);
        food.setUpdateTime(now);
        foodMapper.insert(food);
        return food;
    }
}

