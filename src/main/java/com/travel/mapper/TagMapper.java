package com.travel.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.travel.model.entity.Tag;
import org.apache.ibatis.annotations.Mapper;

/**
 * 标签表 Mapper。
 */
@Mapper
public interface TagMapper extends BaseMapper<Tag>
{
}

