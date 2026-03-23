package com.travel.controller;

import com.travel.common.ApiResponse;
import com.travel.common.PageData;
import com.travel.model.entity.Building;
import com.travel.model.entity.Food;
import com.travel.model.entity.Road;
import com.travel.model.entity.ScenicArea;
import com.travel.security.AuthUser;
import com.travel.security.SecurityUtil;
import com.travel.service.AdminService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 管理端数据管理接口。
 *
 * <p>
 * 简化实现：仅允许 ADMIN 角色访问。
 * </p>
 */
@RestController
@RequestMapping("/api/admin")
public class AdminController
{

    private final AdminService adminService;

    public AdminController(AdminService adminService)
    {
        this.adminService = adminService;
    }

    @PostMapping("/scenic-area")
    public ApiResponse<ScenicArea> addScenicArea(@Valid @RequestBody ScenicArea scenicArea)
    {
        if (!isAdmin())
        {
            return ApiResponse.failure(403, "无权限");
        }
        return ApiResponse.success(adminService.addScenicArea(scenicArea), "添加成功");
    }

    @GetMapping("/scenic-area")
    public ApiResponse<PageData<ScenicArea>> listScenicAreas(@RequestParam(value = "page", required = false) Integer page,
                                                             @RequestParam(value = "size", required = false) Integer size,
                                                             @RequestParam(value = "type", required = false) String type)
    {
        if (!isAdmin())
        {
            return ApiResponse.failure(403, "无权限");
        }
        return ApiResponse.success(adminService.listScenicAreas(page, size, type), "获取成功");
    }

    @PostMapping("/building")
    public ApiResponse<Building> addBuilding(@Valid @RequestBody Building building)
    {
        if (!isAdmin())
        {
            return ApiResponse.failure(403, "无权限");
        }
        return ApiResponse.success(adminService.addBuilding(building), "添加成功");
    }

    @PostMapping("/road")
    public ApiResponse<Road> addRoad(@Valid @RequestBody Road road)
    {
        if (!isAdmin())
        {
            return ApiResponse.failure(403, "无权限");
        }
        return ApiResponse.success(adminService.addRoad(road), "添加成功");
    }

    @PostMapping("/food")
    public ApiResponse<Food> addFood(@Valid @RequestBody Food food)
    {
        if (!isAdmin())
        {
            return ApiResponse.failure(403, "无权限");
        }
        return ApiResponse.success(adminService.addFood(food), "添加成功");
    }

    private boolean isAdmin()
    {
        AuthUser user = SecurityUtil.getCurrentUser();
        return user != null && "ADMIN".equalsIgnoreCase(user.getRole());
    }
}

