package com.travel.model.vo.food;

import com.travel.model.entity.Food;
import com.travel.model.entity.Restaurant;

/**
 * 美食推荐返回对象（包含距离与综合得分）。
 */
public class FoodRecommendVO
{

    private Food food;

    private Restaurant restaurant;

    /**
     * 直线距离（米）。
     */
    private Double distance;

    /**
     * 综合评分（用于排序）。
     */
    private Double score;

    public Food getFood()
    {
        return food;
    }

    public void setFood(Food food)
    {
        this.food = food;
    }

    public Restaurant getRestaurant()
    {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant)
    {
        this.restaurant = restaurant;
    }

    public Double getDistance()
    {
        return distance;
    }

    public void setDistance(Double distance)
    {
        this.distance = distance;
    }

    public Double getScore()
    {
        return score;
    }

    public void setScore(Double score)
    {
        this.score = score;
    }
}

