package com.travel.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.travel.algorithm.TopKSelector;
import com.travel.mapper.CommentMapper;
import com.travel.mapper.FoodMapper;
import com.travel.mapper.RestaurantMapper;
import com.travel.model.entity.Comment;
import com.travel.model.entity.Food;
import com.travel.model.entity.Restaurant;
import com.travel.model.vo.food.FoodRecommendVO;
import com.travel.service.FoodService;
import com.travel.util.GeoUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 美食服务实现。
 */
@Service
public class FoodServiceImpl implements FoodService
{

    private static final int DEFAULT_RADIUS_METERS = 1000;

    private static final double DEFAULT_WEIGHT_HEAT = 0.3;

    private static final double DEFAULT_WEIGHT_RATING = 0.5;

    private static final double DEFAULT_WEIGHT_DISTANCE = 0.2;

    private final FoodMapper foodMapper;

    private final RestaurantMapper restaurantMapper;

    private final CommentMapper commentMapper;

    private final TopKSelector<FoodRecommendVO> topKSelector;

    public FoodServiceImpl(FoodMapper foodMapper,
                           RestaurantMapper restaurantMapper,
                           CommentMapper commentMapper)
    {
        this.foodMapper = foodMapper;
        this.restaurantMapper = restaurantMapper;
        this.commentMapper = commentMapper;
        this.topKSelector = new TopKSelector<>();
    }

    @Override
    public List<FoodRecommendVO> recommend(Long areaId,
                                          Double lat,
                                          Double lng,
                                          Integer radiusMeters,
                                          Double weightHeat,
                                          Double weightRating,
                                          Double weightDist,
                                          Integer page,
                                          Integer size)
    {
        if (areaId == null)
        {
            throw new IllegalArgumentException("areaId 不能为空");
        }

        int r = radiusMeters == null || radiusMeters <= 0 ? DEFAULT_RADIUS_METERS : radiusMeters;
        double wHeat = weightHeat == null ? DEFAULT_WEIGHT_HEAT : weightHeat;
        double wRating = weightRating == null ? DEFAULT_WEIGHT_RATING : weightRating;
        double wDist = weightDist == null ? DEFAULT_WEIGHT_DISTANCE : weightDist;

        int p = page == null || page < 1 ? 1 : page;
        int s = size == null || size <= 0 ? 10 : Math.min(size, 50);

        // 取需要的 TopN = page * size，避免全排序
        int topN = p * s;

        LambdaQueryWrapper<Food> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Food::getAreaId, areaId);
        List<Food> foods = foodMapper.selectList(wrapper);
        if (foods.isEmpty())
        {
            return List.of();
        }

        Map<Long, Restaurant> restaurantMap = loadRestaurants(foods);

        List<FoodRecommendVO> candidates = new ArrayList<>(foods.size());
        int maxHeat = 0;
        for (Food f : foods)
        {
            if (f.getHeat() != null && f.getHeat() > maxHeat)
            {
                maxHeat = f.getHeat();
            }
        }
        if (maxHeat <= 0)
        {
            maxHeat = 1;
        }

        boolean hasLocation = lat != null && lng != null;
        for (Food food : foods)
        {
            Restaurant restaurant = restaurantMap.get(food.getRestaurantId());
            if (restaurant == null)
            {
                continue;
            }

            Double distance = null;
            if (hasLocation && restaurant.getLatitude() != null && restaurant.getLongitude() != null)
            {
                distance = GeoUtil.distanceMeters(lat, lng, restaurant.getLatitude(), restaurant.getLongitude());
                if (distance > r)
                {
                    continue;
                }
            }

            FoodRecommendVO vo = new FoodRecommendVO();
            vo.setFood(food);
            vo.setRestaurant(restaurant);
            vo.setDistance(distance);

            double score = calcScore(food, distance, r, maxHeat, wHeat, wRating, wDist);
            vo.setScore(score);
            candidates.add(vo);
        }

        if (candidates.isEmpty())
        {
            return List.of();
        }

        Comparator<FoodRecommendVO> comparator = Comparator.comparingDouble(v -> v.getScore() == null ? 0.0 : v.getScore());
        List<FoodRecommendVO> top = topKSelector.selectTopK(candidates, topN, comparator);

        int from = (p - 1) * s;
        if (from >= top.size())
        {
            return List.of();
        }
        int to = Math.min(from + s, top.size());
        return top.subList(from, to);
    }

    @Override
    public List<Food> search(String keyword, String cuisine, Long areaId, Integer page, Integer size)
    {
        int p = page == null || page < 1 ? 1 : page;
        int s = size == null || size <= 0 ? 10 : Math.min(size, 50);
        int offset = (p - 1) * s;

        LambdaQueryWrapper<Food> wrapper = new LambdaQueryWrapper<>();
        if (areaId != null)
        {
            wrapper.eq(Food::getAreaId, areaId);
        }
        if (StringUtils.isNotBlank(cuisine))
        {
            wrapper.eq(Food::getCuisine, cuisine);
        }
        if (StringUtils.isNotBlank(keyword))
        {
            wrapper.and(w -> w.like(Food::getName, keyword).or().like(Food::getDescription, keyword));
        }
        wrapper.orderByDesc(Food::getHeat).orderByDesc(Food::getRating);
        wrapper.last("limit " + offset + "," + s);
        return foodMapper.selectList(wrapper);
    }

    @Override
    public Food detail(Long id)
    {
        Food food = foodMapper.selectById(id);
        if (food == null)
        {
            throw new IllegalArgumentException("美食不存在");
        }
        return food;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void rate(Long userId, Long foodId, double rating, String comment)
    {
        Food food = foodMapper.selectById(foodId);
        if (food == null)
        {
            throw new IllegalArgumentException("美食不存在");
        }

        Comment c = new Comment();
        c.setUserId(userId);
        c.setTargetId(foodId);
        c.setTargetType("FOOD");
        c.setContent(comment == null ? "" : comment);
        c.setRating(rating);
        LocalDateTime now = LocalDateTime.now();
        c.setCreateTime(now);
        c.setUpdateTime(now);
        commentMapper.insert(c);

        // 评分聚合：用 comments 表对 FOOD 的评分做平均回写 foods.rating
        LambdaQueryWrapper<Comment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Comment::getTargetType, "FOOD").eq(Comment::getTargetId, foodId);
        List<Comment> comments = commentMapper.selectList(wrapper);
        double avg = comments.stream()
            .map(Comment::getRating)
            .filter(Objects::nonNull)
            .mapToDouble(Double::doubleValue)
            .average()
            .orElse(rating);

        Food update = new Food();
        update.setId(foodId);
        update.setRating(avg);
        foodMapper.updateById(update);
    }

    private Map<Long, Restaurant> loadRestaurants(List<Food> foods)
    {
        List<Long> ids = foods.stream()
            .map(Food::getRestaurantId)
            .filter(Objects::nonNull)
            .distinct()
            .collect(Collectors.toList());
        if (ids.isEmpty())
        {
            return Map.of();
        }
        List<Restaurant> list = restaurantMapper.selectBatchIds(ids);
        Map<Long, Restaurant> map = new HashMap<>(list.size());
        for (Restaurant r : list)
        {
            map.put(r.getId(), r);
        }
        return map;
    }

    private double calcScore(Food food,
                             Double distance,
                             int radiusMeters,
                             int maxHeat,
                             double wHeat,
                             double wRating,
                             double wDist)
    {
        double heat = food.getHeat() == null ? 0.0 : food.getHeat();
        double rating = food.getRating() == null ? 0.0 : food.getRating();

        // 归一化：热度以 maxHeat 为基准；评分以 5 为上限；距离越近得分越高
        double heatScore = heat / maxHeat;
        double ratingScore = Math.min(Math.max(rating / 5.0, 0.0), 1.0);
        double distScore = 0.0;
        if (distance != null)
        {
            double d = Math.min(Math.max(distance, 0.0), radiusMeters);
            distScore = 1.0 - (d / radiusMeters);
        }

        double sum = wHeat + wRating + wDist;
        if (sum <= 0)
        {
            sum = 1.0;
        }
        double wh = wHeat / sum;
        double wr = wRating / sum;
        double wd = wDist / sum;

        return wh * heatScore + wr * ratingScore + wd * distScore;
    }
}

