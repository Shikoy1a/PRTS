package com.travel.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.travel.model.entity.DiaryDestination;
import org.apache.ibatis.annotations.Mapper;

/**
 * 日记-目的地关联表 Mapper。
 */
@Mapper
public interface DiaryDestinationMapper extends BaseMapper<DiaryDestination>
{
}

