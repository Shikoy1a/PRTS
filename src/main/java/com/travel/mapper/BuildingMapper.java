package com.travel.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.travel.model.entity.Building;
import org.apache.ibatis.annotations.Mapper;

/**
 * 建筑物表 Mapper。
 */
@Mapper
public interface BuildingMapper extends BaseMapper<Building>
{
}

