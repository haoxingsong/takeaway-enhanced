package com.takeaway.service;

import com.takeaway.dto.UserLoginDTO;
import com.takeaway.vo.UserLoginVO;

/**
 * C 端用户服务
 */
public interface UserService {

    /** 模拟微信登录（首次登录自动注册） */
    UserLoginVO login(UserLoginDTO dto);
}
