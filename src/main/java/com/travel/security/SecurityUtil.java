package com.travel.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * 安全上下文辅助工具。
 */
public final class SecurityUtil
{

    private SecurityUtil()
    {
    }

    /**
     * 获取当前登录用户，如果未登录返回 null。
     *
     * @return AuthUser
     */
    public static AuthUser getCurrentUser()
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated())
        {
            return null;
        }
        Object principal = authentication.getPrincipal();
        if (principal instanceof AuthUser authUser)
        {
            return authUser;
        }
        return null;
    }

    /**
     * 获取当前登录用户 ID，如果未登录返回 null。
     *
     * @return userId
     */
    public static Long getCurrentUserId()
    {
        AuthUser user = getCurrentUser();
        return user == null ? null : user.getUserId();
    }
}

