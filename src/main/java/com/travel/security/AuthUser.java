package com.travel.security;

import java.io.Serial;
import java.io.Serializable;

/**
 * 当前登录用户信息载体。
 *
 * <p>
 * 用于在 SecurityContext 中保存用户的核心身份信息，避免每个业务模块重复解析 JWT。
 * </p>
 */
public class AuthUser implements Serializable
{

    @Serial
    private static final long serialVersionUID = 1L;

    private final Long userId;

    private final String username;

    private final String role;

    public AuthUser(Long userId, String username, String role)
    {
        this.userId = userId;
        this.username = username;
        this.role = role;
    }

    public Long getUserId()
    {
        return userId;
    }

    public String getUsername()
    {
        return username;
    }

    public String getRole()
    {
        return role;
    }
}

