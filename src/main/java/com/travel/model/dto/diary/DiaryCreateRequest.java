package com.travel.model.dto.diary;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

/**
 * 创建日记请求。
 */
public class DiaryCreateRequest
{

    @NotBlank(message = "title 不能为空")
    private String title;

    @NotBlank(message = "content 不能为空")
    private String content;

    /**
     * 图片 URL 列表（前端传数组，后端以 JSON 字符串存储）。
     */
    private List<String> images;

    /**
     * 视频 URL 列表（前端传数组，后端以 JSON 字符串存储）。
     */
    private List<String> videos;

    /**
     * 关联的目的地（景区/校园）ID 列表。
     */
    @NotEmpty(message = "destinations 不能为空")
    private List<Long> destinations;

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public List<String> getImages()
    {
        return images;
    }

    public void setImages(List<String> images)
    {
        this.images = images;
    }

    public List<String> getVideos()
    {
        return videos;
    }

    public void setVideos(List<String> videos)
    {
        this.videos = videos;
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

