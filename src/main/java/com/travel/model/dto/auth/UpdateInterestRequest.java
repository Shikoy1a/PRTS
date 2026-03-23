package com.travel.model.dto.auth;

import jakarta.validation.constraints.NotEmpty;

import java.util.List;

/**
 * 更新用户兴趣偏好的请求体。
 */
public class UpdateInterestRequest
{

    @NotEmpty(message = "兴趣列表不能为空")
    private List<InterestItemRequest> interests;

    public List<InterestItemRequest> getInterests()
    {
        return interests;
    }

    public void setInterests(List<InterestItemRequest> interests)
    {
        this.interests = interests;
    }
}

