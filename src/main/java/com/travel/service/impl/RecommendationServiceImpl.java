package com.travel.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.travel.common.PageData;
import com.travel.mapper.ScenicAreaMapper;
import com.travel.mapper.ScenicAreaTagMapper;
import com.travel.mapper.TagMapper;
import com.travel.mapper.UserInterestMapper;
import com.travel.model.entity.ScenicArea;
import com.travel.model.entity.ScenicAreaTag;
import com.travel.model.entity.Tag;
import com.travel.model.entity.UserInterest;
import com.travel.model.vo.recommendation.ScenicAreaRecommendVO;
import com.travel.service.RecommendationService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 推荐服务实现。
 *
 * <p>
 * 个性化推荐核心思想：
 * <ul>
 *     <li>用户兴趣：user_interests(interest_type, weight)</li>
 *     <li>景区标签：scenic_area_tags(tag_id, weight) + tags(name)</li>
 * </ul>
 * 通过标签名与兴趣类型做匹配（约定：标签名与兴趣类型一致或高度相关），计算匹配得分：
 * score = Σ(userWeight * tagWeight) + 0.2 * heatNorm + 0.2 * ratingNorm（基础融合策略，便于后续替换）
 * </p>
 */
@Service
public class RecommendationServiceImpl implements RecommendationService
{

    private final ScenicAreaMapper scenicAreaMapper;

    private final UserInterestMapper userInterestMapper;

    private final ScenicAreaTagMapper scenicAreaTagMapper;

    private final TagMapper tagMapper;

    public RecommendationServiceImpl(ScenicAreaMapper scenicAreaMapper,
                                     UserInterestMapper userInterestMapper,
                                     ScenicAreaTagMapper scenicAreaTagMapper,
                                     TagMapper tagMapper)
    {
        this.scenicAreaMapper = scenicAreaMapper;
        this.userInterestMapper = userInterestMapper;
        this.scenicAreaTagMapper = scenicAreaTagMapper;
        this.tagMapper = tagMapper;
    }

    @Override
    public PageData<ScenicArea> list(Integer page, Integer size, String sortBy, String type)
    {
        int p = page == null || page < 1 ? 1 : page;
        int s = size == null || size <= 0 ? 10 : Math.min(size, 50);
        int offset = (p - 1) * s;

        LambdaQueryWrapper<ScenicArea> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(type))
        {
            wrapper.eq(ScenicArea::getType, type);
        }

        if ("rating".equalsIgnoreCase(sortBy))
        {
            wrapper.orderByDesc(ScenicArea::getRating);
        }
        else if ("heat".equalsIgnoreCase(sortBy))
        {
            wrapper.orderByDesc(ScenicArea::getHeat);
        }
        else
        {
            wrapper.orderByDesc(ScenicArea::getHeat).orderByDesc(ScenicArea::getRating);
        }

        Long total = scenicAreaMapper.selectCount(wrapper);
        wrapper.last("limit " + offset + "," + s);
        List<ScenicArea> list = scenicAreaMapper.selectList(wrapper);
        return new PageData<>(list, total);
    }

    @Override
    public PageData<ScenicArea> hot(Integer page, Integer size, String type)
    {
        // 热门：优先热度，其次评分（基础实现）
        return list(page, size, "heat", type);
    }

    @Override
    public PageData<ScenicAreaRecommendVO> personalized(Long userId, Integer page, Integer size, String type)
    {
        if (userId == null)
        {
            throw new IllegalArgumentException("userId 不能为空");
        }
        int p = page == null || page < 1 ? 1 : page;
        int s = size == null || size <= 0 ? 10 : Math.min(size, 50);

        // 候选集：取热度靠前的一批再做个性化计算，避免全量计算
        int candidateLimit = Math.min(300, p * s * 10);
        LambdaQueryWrapper<ScenicArea> candidateWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(type))
        {
            candidateWrapper.eq(ScenicArea::getType, type);
        }
        candidateWrapper.orderByDesc(ScenicArea::getHeat).orderByDesc(ScenicArea::getRating);
        candidateWrapper.last("limit " + candidateLimit);
        List<ScenicArea> candidates = scenicAreaMapper.selectList(candidateWrapper);
        if (candidates.isEmpty())
        {
            return new PageData<>(List.of(), 0L);
        }

        Map<String, Double> interestWeights = loadUserInterests(userId);
        Map<Long, Map<String, Double>> scenicTagWeights = loadScenicTagWeights(candidates);

        int maxHeat = candidates.stream().map(ScenicArea::getHeat).filter(Objects::nonNull).max(Integer::compareTo).orElse(1);
        if (maxHeat <= 0)
        {
            maxHeat = 1;
        }

        List<ScenicAreaRecommendVO> scored = new ArrayList<>(candidates.size());
        for (ScenicArea scenic : candidates)
        {
            double matchScore = 0.0;
            Map<String, Double> tags = scenicTagWeights.getOrDefault(scenic.getId(), Map.of());
            for (Map.Entry<String, Double> e : tags.entrySet())
            {
                Double uw = interestWeights.get(e.getKey());
                if (uw != null)
                {
                    matchScore += uw * (e.getValue() == null ? 1.0 : e.getValue());
                }
            }

            double heatNorm = (scenic.getHeat() == null ? 0.0 : scenic.getHeat()) / maxHeat;
            double ratingNorm = Math.min(Math.max((scenic.getRating() == null ? 0.0 : scenic.getRating()) / 5.0, 0.0), 1.0);

            double score = matchScore + 0.2 * heatNorm + 0.2 * ratingNorm;

            ScenicAreaRecommendVO vo = new ScenicAreaRecommendVO();
            vo.setScenicArea(scenic);
            vo.setScore(score);
            scored.add(vo);
        }

        Comparator<ScenicAreaRecommendVO> comparator =
            Comparator.comparingDouble((ScenicAreaRecommendVO v) -> v.getScore() == null ? 0.0 : v.getScore()).reversed();
        scored.sort(comparator);

        int from = (p - 1) * s;
        if (from >= scored.size())
        {
            return new PageData<>(List.of(), (long) scored.size());
        }
        int to = Math.min(from + s, scored.size());
        return new PageData<>(scored.subList(from, to), (long) scored.size());
    }

    @Override
    public ScenicArea detail(Long id)
    {
        ScenicArea scenicArea = scenicAreaMapper.selectById(id);
        if (scenicArea == null)
        {
            throw new IllegalArgumentException("景区不存在");
        }
        return scenicArea;
    }

    private Map<String, Double> loadUserInterests(Long userId)
    {
        LambdaQueryWrapper<UserInterest> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserInterest::getUserId, userId);
        List<UserInterest> list = userInterestMapper.selectList(wrapper);
        Map<String, Double> map = new HashMap<>();
        for (UserInterest ui : list)
        {
            if (StringUtils.isBlank(ui.getInterestType()))
            {
                continue;
            }
            map.put(ui.getInterestType(), ui.getWeight() == null ? 1.0 : ui.getWeight());
        }
        return map;
    }

    private Map<Long, Map<String, Double>> loadScenicTagWeights(List<ScenicArea> candidates)
    {
        Set<Long> scenicIds = candidates.stream().map(ScenicArea::getId).filter(Objects::nonNull).collect(Collectors.toSet());
        if (scenicIds.isEmpty())
        {
            return Map.of();
        }

        LambdaQueryWrapper<ScenicAreaTag> satw = new LambdaQueryWrapper<>();
        satw.in(ScenicAreaTag::getScenicAreaId, scenicIds);
        List<ScenicAreaTag> relations = scenicAreaTagMapper.selectList(satw);
        if (relations.isEmpty())
        {
            return Map.of();
        }

        Set<Long> tagIds = relations.stream().map(ScenicAreaTag::getTagId).filter(Objects::nonNull).collect(Collectors.toSet());
        if (tagIds.isEmpty())
        {
            return Map.of();
        }

        List<Tag> tags = tagMapper.selectBatchIds(tagIds);
        Map<Long, Tag> tagMap = new HashMap<>(tags.size());
        for (Tag t : tags)
        {
            tagMap.put(t.getId(), t);
        }

        Map<Long, Map<String, Double>> scenicTags = new HashMap<>();
        for (ScenicAreaTag rel : relations)
        {
            Tag tag = tagMap.get(rel.getTagId());
            if (tag == null || StringUtils.isBlank(tag.getName()))
            {
                continue;
            }
            scenicTags.computeIfAbsent(rel.getScenicAreaId(), k -> new HashMap<>())
                .put(tag.getName(), rel.getWeight() == null ? 1.0 : rel.getWeight());
        }
        return scenicTags;
    }
}

