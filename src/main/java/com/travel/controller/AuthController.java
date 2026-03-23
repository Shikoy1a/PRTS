package com.travel.controller;

import com.travel.common.ApiResponse;
import com.travel.model.dto.auth.LoginRequest;
import com.travel.model.dto.auth.RegisterRequest;
import com.travel.model.dto.auth.UpdateInterestRequest;
import com.travel.model.vo.UserVO;
import com.travel.security.JwtUtil;
import com.travel.security.SecurityUtil;
import com.travel.service.UserService;
import io.jsonwebtoken.Claims;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 认证与用户偏好相关接口。
 *
 * <p>
 * 对应文档中的：
 * <ul>
 *     <li>/api/auth/register</li>
 *     <li>/api/auth/login</li>
 *     <li>/api/auth/refresh</li>
 *     <li>/api/auth/interest</li>
 * </ul>
 * </p>
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController
{

    private final UserService userService;

    private final JwtUtil jwtUtil;

    public AuthController(UserService userService, JwtUtil jwtUtil)
    {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public ApiResponse<Map<String, Object>> register(@Valid @RequestBody RegisterRequest request)
    {
        UserVO user = userService.register(request);
        Map<String, Object> data = new HashMap<>();
        data.put("user_id", user.getId());
        data.put("username", user.getUsername());
        return ApiResponse.success(data, "注册成功");
    }

    @PostMapping("/login")
    public ApiResponse<Map<String, Object>> login(@Valid @RequestBody LoginRequest request)
    {
        String token = userService.login(request);
        UserVO user = userService.findByUsername(request.getUsername());
        Map<String, Object> data = new HashMap<>();
        data.put("token", token);
        data.put("user", user);
        return ApiResponse.success(data, "登录成功");
    }

    @PostMapping("/refresh")
    public ApiResponse<Map<String, Object>> refresh(@RequestHeader("Authorization") String authHeader)
    {
        String token = resolveToken(authHeader);
        Claims claims = jwtUtil.parseToken(token);
        if (claims == null)
        {
            return ApiResponse.failure(401, "令牌无效");
        }
        Long userId = jwtUtil.getUserId(token);
        String username = claims.getSubject();
        String newToken = jwtUtil.generateToken(userId, username);

        Map<String, Object> data = new HashMap<>();
        data.put("token", newToken);
        return ApiResponse.success(data, "令牌刷新成功");
    }

    @PutMapping("/interest")
    public ApiResponse<Void> updateInterest(@Valid @RequestBody UpdateInterestRequest request)
    {
        Long userId = SecurityUtil.getCurrentUserId();
        if (userId == null)
        {
            return ApiResponse.failure(401, "未登录或令牌无效");
        }
        userService.updateInterests(userId, request);
        return ApiResponse.successMessage("兴趣更新成功");
    }

    private String resolveToken(String header)
    {
        if (header == null)
        {
            return null;
        }
        if (header.startsWith("Bearer "))
        {
            return header.substring(7);
        }
        return header;
    }
}

