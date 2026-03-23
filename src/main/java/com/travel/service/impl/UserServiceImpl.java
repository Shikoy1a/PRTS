package com.travel.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.travel.mapper.UserInterestMapper;
import com.travel.mapper.UserMapper;
import com.travel.model.dto.auth.InterestItemRequest;
import com.travel.model.dto.auth.LoginRequest;
import com.travel.model.dto.auth.RegisterRequest;
import com.travel.model.dto.auth.UpdateInterestRequest;
import com.travel.model.entity.User;
import com.travel.model.entity.UserInterest;
import com.travel.model.vo.UserVO;
import com.travel.security.JwtUtil;
import com.travel.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户服务实现。
 */
@Service
public class UserServiceImpl implements UserService
{

    private final UserMapper userMapper;

    private final UserInterestMapper userInterestMapper;

    private final PasswordEncoder passwordEncoder;

    private final JwtUtil jwtUtil;

    public UserServiceImpl(UserMapper userMapper,
                           UserInterestMapper userInterestMapper,
                           PasswordEncoder passwordEncoder,
                           JwtUtil jwtUtil)
    {
        this.userMapper = userMapper;
        this.userInterestMapper = userInterestMapper;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserVO register(RegisterRequest request)
    {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, request.getUsername())
            .or()
            .eq(User::getEmail, request.getEmail());
        long count = userMapper.selectCount(wrapper);
        if (count > 0)
        {
            throw new IllegalArgumentException("用户名或邮箱已存在");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setNickname(request.getNickname());
        user.setRole("USER");
        LocalDateTime now = LocalDateTime.now();
        user.setCreateTime(now);
        user.setUpdateTime(now);

        userMapper.insert(user);
        return toUserVO(user);
    }

    @Override
    public String login(LoginRequest request)
    {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, request.getUsername());
        User user = userMapper.selectOne(wrapper);
        if (user == null)
        {
            throw new IllegalArgumentException("用户不存在");
        }
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword()))
        {
            throw new IllegalArgumentException("用户名或密码错误");
        }
        return jwtUtil.generateToken(user.getId(), user.getUsername(), user.getRole());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateInterests(Long userId, UpdateInterestRequest request)
    {
        LambdaQueryWrapper<UserInterest> deleteWrapper = new LambdaQueryWrapper<>();
        deleteWrapper.eq(UserInterest::getUserId, userId);
        userInterestMapper.delete(deleteWrapper);

        List<InterestItemRequest> items = request.getInterests();
        LocalDateTime now = LocalDateTime.now();
        for (InterestItemRequest item : items)
        {
            if (StringUtils.isBlank(item.getType()))
            {
                continue;
            }
            UserInterest interest = new UserInterest();
            interest.setUserId(userId);
            interest.setInterestType(item.getType());
            interest.setWeight(item.getWeight() == null ? 1.0 : item.getWeight());
            interest.setCreateTime(now);
            userInterestMapper.insert(interest);
        }
    }

    @Override
    public UserVO findByUsername(String username)
    {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, username);
        User user = userMapper.selectOne(wrapper);
        if (user == null)
        {
            return null;
        }
        return toUserVO(user);
    }

    private UserVO toUserVO(User user)
    {
        UserVO vo = new UserVO();
        vo.setId(user.getId());
        vo.setUsername(user.getUsername());
        vo.setEmail(user.getEmail());
        vo.setNickname(user.getNickname());
        vo.setAvatar(user.getAvatar());
        vo.setRole(user.getRole());
        return vo;
    }
}

