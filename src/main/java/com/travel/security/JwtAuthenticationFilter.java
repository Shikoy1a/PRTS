package com.travel.security;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

/**
 * JWT 认证过滤器。
 *
 * <p>
 * 从 Authorization 头中读取 Bearer Token，验证后将认证信息写入 SecurityContext，
 * 从而实现真正的无状态鉴权。
 * </p>
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter
{

    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil)
    {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException
    {
        String token = resolveToken(request);
        if (token != null && jwtUtil.validateToken(token))
        {
            Claims claims = jwtUtil.parseToken(token);
            Long userId = jwtUtil.getUserId(token);
            String username = claims == null ? null : claims.getSubject();
            String role = jwtUtil.getRole(token);

            if (userId != null && username != null && SecurityContextHolder.getContext().getAuthentication() == null)
            {
                AuthUser principal = new AuthUser(userId, username, role);
                List<SimpleGrantedAuthority> authorities =
                    role == null ? List.of() : List.of(new SimpleGrantedAuthority("ROLE_" + role));

                UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(principal, null, authorities);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        filterChain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request)
    {
        String header = request.getHeader("Authorization");
        if (header == null || header.isBlank())
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

