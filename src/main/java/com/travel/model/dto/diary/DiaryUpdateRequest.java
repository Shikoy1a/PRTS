package com.travel.model.dto.diary;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

/**
 * 更新日记请求。
 */
public class DiaryUpdateRequest
{

    @NotNull(message = "id 不能为空")
    private Long id;

    @NotBlank(message = "title 不能为空")
    private String title;

    @NotBlank(message = "content 不能为空")
    private String content;

    private List<String> images;

    private List<String> videos;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

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
}

