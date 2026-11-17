package com.takeaway.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.takeaway.entity.User;
import com.takeaway.mapper.UserMapper;
import com.takeaway.service.UserService;
import com.takeaway.dto.UserLoginDTO;
import com.takeaway.util.JwtUtil;
import com.takeaway.vo.UserLoginVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * C 端用户服务实现
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    /** C 端用户角色标识 */
    private static final String USER_ROLE = "USER";

    /** 自动注册的默认昵称 */
    private static final String DEFAULT_NAME = "微信用户";

    private final UserMapper userMapper;
    private final JwtUtil jwtUtil;

    @Override
    public UserLoginVO login(UserLoginDTO dto) {
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getOpenid, dto.getOpenid()));
        // 模拟微信登录：openid 首次出现时自动注册
        if (user == null) {
            user = new User();
            user.setOpenid(dto.getOpenid());
            user.setName(DEFAULT_NAME);
            userMapper.insert(user);
        }
        String token = jwtUtil.createToken(user.getId(), user.getOpenid(), USER_ROLE);

        UserLoginVO vo = new UserLoginVO();
        vo.setId(user.getId());
        vo.setOpenid(user.getOpenid());
        vo.setName(user.getName());
        vo.setToken(token);
        return vo;
    }
}
