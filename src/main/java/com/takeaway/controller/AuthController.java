package com.takeaway.controller;

import com.takeaway.common.context.CurrentUser;
import com.takeaway.common.context.UserContext;
import com.takeaway.common.result.Result;
import com.takeaway.dto.LoginDTO;
import com.takeaway.util.JwtUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 管理员认证（骨架演示：登录发 token + 获取当前用户）
 */
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AuthController {

    private final JwtUtil jwtUtil;

    /**
     * 管理员登录（阶段3接入数据库真实校验，当前为骨架演示）
     */
    @PostMapping("/login")
    public Result<String> login(@Valid @RequestBody LoginDTO loginDTO) {
        // TODO 阶段3：查询数据库校验账号密码与状态后签发 token
        String token = jwtUtil.createToken(1L, loginDTO.getUsername(), "ADMIN");
        return Result.success(token);
    }

    /**
     * 获取当前登录用户（验证 JWT 拦截器与用户上下文）
     */
    @GetMapping("/current")
    public Result<CurrentUser> current() {
        return Result.success(UserContext.get());
    }
}
