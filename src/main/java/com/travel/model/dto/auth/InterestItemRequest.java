package com.travel.model.dto.auth;

import jakarta.validation.constraints.NotBlank;

/**
 * 单个兴趣项的更新请求。
 */
public class InterestItemRequest
{

    @NotBlank(message = "兴趣类型不能为空")
    private String type;

    private Double weight;

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public Double getWeight()
    {
        return weight;
    }

    public void setWeight(Double weight)
    {
        this.weight = weight;
    }
}

