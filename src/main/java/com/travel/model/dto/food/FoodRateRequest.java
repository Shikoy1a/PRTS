package com.travel.model.dto.food;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * 美食评分请求。
 */
public class FoodRateRequest
{

    @NotNull(message = "foodId 不能为空")
    private Long foodId;

    @NotNull(message = "rating 不能为空")
    @Min(value = 1, message = "rating 最小为 1")
    @Max(value = 5, message = "rating 最大为 5")
    private Double rating;

    /**
     * 可选评论内容。
     */
    private String comment;

    public Long getFoodId()
    {
        return foodId;
    }

    public void setFoodId(Long foodId)
    {
        this.foodId = foodId;
    }

    public Double getRating()
    {
        return rating;
    }

    public void setRating(Double rating)
    {
        this.rating = rating;
    }

    public String getComment()
    {
        return comment;
    }

    public void setComment(String comment)
    {
        this.comment = comment;
    }
}

