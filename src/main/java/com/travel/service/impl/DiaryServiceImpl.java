package com.travel.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.travel.mapper.CommentMapper;
import com.travel.mapper.DiaryDestinationMapper;
import com.travel.mapper.DiaryMapper;
import com.travel.model.dto.diary.DiaryCreateRequest;
import com.travel.model.dto.diary.DiaryUpdateRequest;
import com.travel.model.entity.Comment;
import com.travel.model.entity.Diary;
import com.travel.model.entity.DiaryDestination;
import com.travel.model.vo.diary.DiaryDetailVO;
import com.travel.service.DiaryService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 日记服务实现。
 */
@Service
public class DiaryServiceImpl implements DiaryService
{

    private final DiaryMapper diaryMapper;

    private final DiaryDestinationMapper diaryDestinationMapper;

    private final CommentMapper commentMapper;

    private final ObjectMapper objectMapper;

    public DiaryServiceImpl(DiaryMapper diaryMapper,
                            DiaryDestinationMapper diaryDestinationMapper,
                            CommentMapper commentMapper,
                            ObjectMapper objectMapper)
    {
        this.diaryMapper = diaryMapper;
        this.diaryDestinationMapper = diaryDestinationMapper;
        this.commentMapper = commentMapper;
        this.objectMapper = objectMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long create(Long userId, DiaryCreateRequest request)
    {
        Diary diary = new Diary();
        diary.setUserId(userId);
        diary.setTitle(request.getTitle());
        diary.setContent(request.getContent());
        diary.setImages(toJson(request.getImages()));
        diary.setVideos(toJson(request.getVideos()));
        diary.setHeat(0);
        diary.setRating(0.0);
        LocalDateTime now = LocalDateTime.now();
        diary.setCreateTime(now);
        diary.setUpdateTime(now);
        diaryMapper.insert(diary);

        for (Long destId : request.getDestinations())
        {
            DiaryDestination dd = new DiaryDestination();
            dd.setDiaryId(diary.getId());
            dd.setDestinationId(destId);
            dd.setCreateTime(now);
            diaryDestinationMapper.insert(dd);
        }

        return diary.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Long userId, DiaryUpdateRequest request)
    {
        Diary existing = diaryMapper.selectById(request.getId());
        if (existing == null)
        {
            throw new IllegalArgumentException("日记不存在");
        }
        if (!Objects.equals(existing.getUserId(), userId))
        {
            throw new IllegalArgumentException("无权限操作该日记");
        }

        Diary update = new Diary();
        update.setId(existing.getId());
        update.setTitle(request.getTitle());
        update.setContent(request.getContent());
        update.setImages(toJson(request.getImages()));
        update.setVideos(toJson(request.getVideos()));
        update.setUpdateTime(LocalDateTime.now());
        diaryMapper.updateById(update);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long userId, Long diaryId)
    {
        Diary existing = diaryMapper.selectById(diaryId);
        if (existing == null)
        {
            throw new IllegalArgumentException("日记不存在");
        }
        if (!Objects.equals(existing.getUserId(), userId))
        {
            throw new IllegalArgumentException("无权限操作该日记");
        }
        diaryMapper.deleteById(diaryId);

        LambdaQueryWrapper<DiaryDestination> ddw = new LambdaQueryWrapper<>();
        ddw.eq(DiaryDestination::getDiaryId, diaryId);
        diaryDestinationMapper.delete(ddw);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public DiaryDetailVO detail(Long diaryId)
    {
        Diary diary = diaryMapper.selectById(diaryId);
        if (diary == null)
        {
            throw new IllegalArgumentException("日记不存在");
        }

        // 浏览热度：每次详情访问 heat + 1（后续可改为按 7 天权重计算）
        Diary update = new Diary();
        update.setId(diaryId);
        update.setHeat((diary.getHeat() == null ? 0 : diary.getHeat()) + 1);
        diaryMapper.updateById(update);
        diary.setHeat(update.getHeat());

        LambdaQueryWrapper<DiaryDestination> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DiaryDestination::getDiaryId, diaryId);
        List<DiaryDestination> list = diaryDestinationMapper.selectList(wrapper);
        List<Long> destIds = new ArrayList<>();
        for (DiaryDestination dd : list)
        {
            destIds.add(dd.getDestinationId());
        }

        DiaryDetailVO vo = new DiaryDetailVO();
        vo.setDiary(diary);
        vo.setDestinations(destIds);
        return vo;
    }

    @Override
    public List<Diary> list(Integer page, Integer size, String sortBy)
    {
        int p = page == null || page < 1 ? 1 : page;
        int s = size == null || size <= 0 ? 10 : Math.min(size, 50);
        int offset = (p - 1) * s;

        LambdaQueryWrapper<Diary> wrapper = new LambdaQueryWrapper<>();
        if ("rating".equalsIgnoreCase(sortBy))
        {
            wrapper.orderByDesc(Diary::getRating);
        }
        else
        {
            wrapper.orderByDesc(Diary::getHeat).orderByDesc(Diary::getCreateTime);
        }
        wrapper.last("limit " + offset + "," + s);
        return diaryMapper.selectList(wrapper);
    }

    @Override
    public List<Diary> search(String keyword, Long destinationId, Integer page, Integer size)
    {
        int p = page == null || page < 1 ? 1 : page;
        int s = size == null || size <= 0 ? 10 : Math.min(size, 50);
        int offset = (p - 1) * s;

        // 基础实现：keyword 在 title/content 上模糊匹配；destinationId 通过关联表过滤
        LambdaQueryWrapper<Diary> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(keyword))
        {
            wrapper.and(w -> w.like(Diary::getTitle, keyword).or().like(Diary::getContent, keyword));
        }
        if (destinationId != null)
        {
            LambdaQueryWrapper<DiaryDestination> ddw = new LambdaQueryWrapper<>();
            ddw.eq(DiaryDestination::getDestinationId, destinationId);
            List<DiaryDestination> relations = diaryDestinationMapper.selectList(ddw);
            List<Long> diaryIds = relations.stream().map(DiaryDestination::getDiaryId).distinct().toList();
            if (diaryIds.isEmpty())
            {
                return List.of();
            }
            wrapper.in(Diary::getId, diaryIds);
        }

        wrapper.orderByDesc(Diary::getHeat).orderByDesc(Diary::getRating);
        wrapper.last("limit " + offset + "," + s);
        return diaryMapper.selectList(wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void rate(Long userId, Long diaryId, double rating)
    {
        Diary diary = diaryMapper.selectById(diaryId);
        if (diary == null)
        {
            throw new IllegalArgumentException("日记不存在");
        }

        Comment c = new Comment();
        c.setUserId(userId);
        c.setTargetId(diaryId);
        c.setTargetType("DIARY");
        c.setContent("");
        c.setRating(rating);
        LocalDateTime now = LocalDateTime.now();
        c.setCreateTime(now);
        c.setUpdateTime(now);
        commentMapper.insert(c);

        // 聚合评分回写 diaries.rating
        LambdaQueryWrapper<Comment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Comment::getTargetType, "DIARY").eq(Comment::getTargetId, diaryId);
        List<Comment> comments = commentMapper.selectList(wrapper);
        double avg = comments.stream()
            .map(Comment::getRating)
            .filter(Objects::nonNull)
            .mapToDouble(Double::doubleValue)
            .average()
            .orElse(rating);

        Diary update = new Diary();
        update.setId(diaryId);
        update.setRating(avg);
        diaryMapper.updateById(update);
    }

    private String toJson(List<String> list)
    {
        if (list == null)
        {
            return null;
        }
        try
        {
            return objectMapper.writeValueAsString(list);
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException("图片/视频字段格式错误");
        }
    }

    @SuppressWarnings("unused")
    private List<String> fromJson(String json)
    {
        if (StringUtils.isBlank(json))
        {
            return List.of();
        }
        try
        {
            return objectMapper.readValue(json, new TypeReference<List<String>>() {});
        }
        catch (Exception ex)
        {
            return List.of();
        }
    }
}

