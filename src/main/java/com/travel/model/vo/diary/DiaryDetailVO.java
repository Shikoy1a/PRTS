package com.travel.model.vo.diary;

import com.travel.model.entity.Diary;

import java.util.List;

/**
 * 日记详情返回对象（包含目的地列表）。
 */
public class DiaryDetailVO
{

    private Diary diary;

    private List<Long> destinations;

    public Diary getDiary()
    {
        return diary;
    }

    public void setDiary(Diary diary)
    {
        this.diary = diary;
    }

    public List<Long> getDestinations()
    {
        return destinations;
    }

    public void setDestinations(List<Long> destinations)
    {
        this.destinations = destinations;
    }
}

