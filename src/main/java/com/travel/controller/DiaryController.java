package com.travel.controller;

import com.travel.common.ApiResponse;
import com.travel.model.dto.diary.DiaryCreateRequest;
import com.travel.model.dto.diary.DiaryRateRequest;
import com.travel.model.dto.diary.DiaryUpdateRequest;
import com.travel.model.entity.Diary;
import com.travel.model.vo.diary.DiaryDetailVO;
import com.travel.security.SecurityUtil;
import com.travel.service.DiaryService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 旅游日记接口。
 */
@RestController
@RequestMapping("/api/diary")
public class DiaryController
{

    private final DiaryService diaryService;

    public DiaryController(DiaryService diaryService)
    {
        this.diaryService = diaryService;
    }

    @PostMapping
    public ApiResponse<Map<String, Object>> create(@Valid @RequestBody DiaryCreateRequest request)
    {
        Long userId = SecurityUtil.getCurrentUserId();
        if (userId == null)
        {
            return ApiResponse.failure(401, "未登录或令牌无效");
        }
        Long diaryId = diaryService.create(userId, request);
        Map<String, Object> data = new HashMap<>();
        data.put("diary_id", diaryId);
        return ApiResponse.success(data, "创建成功");
    }

    @GetMapping
    public ApiResponse<List<Diary>> list(@RequestParam(value = "page", required = false) Integer page,
                                         @RequestParam(value = "size", required = false) Integer size,
                                         @RequestParam(value = "sortBy", required = false) String sortBy)
    {
        return ApiResponse.success(diaryService.list(page, size, sortBy), "获取成功");
    }

    @GetMapping("/{id}")
    public ApiResponse<DiaryDetailVO> detail(@PathVariable("id") @NotNull Long id)
    {
        return ApiResponse.success(diaryService.detail(id), "获取成功");
    }

    @PutMapping("/{id}")
    public ApiResponse<Void> update(@PathVariable("id") @NotNull Long id,
                                    @Valid @RequestBody DiaryUpdateRequest request)
    {
        Long userId = SecurityUtil.getCurrentUserId();
        if (userId == null)
        {
            return ApiResponse.failure(401, "未登录或令牌无效");
        }
        request.setId(id);
        diaryService.update(userId, request);
        return ApiResponse.successMessage("更新成功");
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable("id") @NotNull Long id)
    {
        Long userId = SecurityUtil.getCurrentUserId();
        if (userId == null)
        {
            return ApiResponse.failure(401, "未登录或令牌无效");
        }
        diaryService.delete(userId, id);
        return ApiResponse.successMessage("删除成功");
    }

    @GetMapping("/search")
    public ApiResponse<List<Diary>> search(@RequestParam(value = "keyword", required = false) String keyword,
                                           @RequestParam(value = "destination", required = false) Long destination,
                                           @RequestParam(value = "page", required = false) Integer page,
                                           @RequestParam(value = "size", required = false) Integer size)
    {
        return ApiResponse.success(diaryService.search(keyword, destination, page, size), "查询成功");
    }

    @PostMapping("/rate")
    public ApiResponse<Void> rate(@Valid @RequestBody DiaryRateRequest request)
    {
        Long userId = SecurityUtil.getCurrentUserId();
        if (userId == null)
        {
            return ApiResponse.failure(401, "未登录或令牌无效");
        }
        diaryService.rate(userId, request.getDiaryId(), request.getRating());
        return ApiResponse.successMessage("评分成功");
    }
}

