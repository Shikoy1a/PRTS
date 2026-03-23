package com.travel.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.travel.model.entity.UserInterest;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户兴趣表 Mapper。
 */
@Mapper
public interface UserInterestMapper extends BaseMapper<UserInterest>
{
}

