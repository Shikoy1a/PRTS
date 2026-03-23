package com.travel.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * JWT 令牌生成与解析工具。
 *
 * <p>
 * 封装了对 jjwt 库的调用，提供：
 * <ul>
 *     <li>根据用户标识生成访问令牌</li>
 *     <li>解析令牌并提取声明信息</li>
 *     <li>校验令牌是否过期或非法</li>
 * </ul>
 * </p>
 */
@Component
public class JwtUtil implements InitializingBean
{

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expire-seconds}")
    private long expireSeconds;

    private SecretKey secretKey;

    @Override
    public void afterPropertiesSet()
    {
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * 生成访问令牌。
     *
     * @param userId   用户标识
     * @param username 用户名
     * @return JWT 字符串
     */
    public String generateToken(Long userId, String username)
    {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + expireSeconds * 1000);

        return Jwts.builder()
            .setSubject(username)
            .claim("uid", userId)
            .setIssuedAt(now)
            .setExpiration(expiry)
            .signWith(secretKey, SignatureAlgorithm.HS256)
            .compact();
    }

    /**
     * 生成访问令牌（包含角色声明）。
     *
     * @param userId   用户标识
     * @param username 用户名
     * @param role     角色（如 USER / ADMIN）
     * @return JWT 字符串
     */
    public String generateToken(Long userId, String username, String role)
    {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + expireSeconds * 1000);

        return Jwts.builder()
            .setSubject(username)
            .claim("uid", userId)
            .claim("role", role)
            .setIssuedAt(now)
            .setExpiration(expiry)
            .signWith(secretKey, SignatureAlgorithm.HS256)
            .compact();
    }

    /**
     * 解析令牌。
     *
     * @param token JWT 字符串
     * @return 声明体，如果解析失败则返回 {@code null}
     */
    public Claims parseToken(String token)
    {
        try
        {
            return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
        }
        catch (Exception ex)
        {
            return null;
        }
    }

    /**
     * 判断令牌是否有效。
     *
     * @param token JWT 字符串
     * @return true 表示合法且未过期
     */
    public boolean validateToken(String token)
    {
        Claims claims = parseToken(token);
        if (claims == null)
        {
            return false;
        }
        Date expiration = claims.getExpiration();
        return expiration != null && expiration.after(new Date());
    }

    /**
     * 从令牌中提取用户 ID。
     *
     * @param token JWT 字符串
     * @return 用户 ID，如果无法解析则返回 null
     */
    public Long getUserId(String token)
    {
        Claims claims = parseToken(token);
        if (claims == null)
        {
            return null;
        }
        Object uid = claims.get("uid");
        if (uid instanceof Number number)
        {
            return number.longValue();
        }
        return null;
    }

    /**
     * 从令牌中提取角色。
     *
     * @param token JWT 字符串
     * @return 角色字符串，解析失败返回 null
     */
    public String getRole(String token)
    {
        Claims claims = parseToken(token);
        if (claims == null)
        {
            return null;
        }
        Object role = claims.get("role");
        return role == null ? null : role.toString();
    }
}

