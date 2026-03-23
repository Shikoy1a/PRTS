package com.travel.model.vo.recommendation;

import com.travel.model.entity.ScenicArea;

/**
 * 景区推荐返回对象（包含推荐得分）。
 */
public class ScenicAreaRecommendVO
{

    private ScenicArea scenicArea;

    private Double score;

    public ScenicArea getScenicArea()
    {
        return scenicArea;
    }

    public void setScenicArea(ScenicArea scenicArea)
    {
        this.scenicArea = scenicArea;
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

