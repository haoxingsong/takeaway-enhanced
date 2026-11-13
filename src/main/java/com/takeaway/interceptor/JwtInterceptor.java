package com.takeaway.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.takeaway.common.constant.JwtConstant;
import com.takeaway.common.context.CurrentUser;
import com.takeaway.common.context.UserContext;
import com.takeaway.common.result.Result;
import com.takeaway.common.result.ResultCode;
import com.takeaway.util.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * JWT 登录拦截器：校验 Authorization 请求头，解析并写入用户上下文
 */
@Component
@RequiredArgsConstructor
public class JwtInterceptor implements HandlerInterceptor {

    private static final String TOKEN_PREFIX = "Bearer ";

    private final JwtUtil jwtUtil;
    private final ObjectMapper objectMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // CORS 预检请求直接放行
        if (HttpMethod.OPTIONS.name().equals(request.getMethod())) {
            return true;
        }

        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith(TOKEN_PREFIX)) {
            token = token.substring(TOKEN_PREFIX.length());
        }

        try {
            Claims claims = jwtUtil.parseToken(token);
            CurrentUser user = new CurrentUser();
            user.setId(claims.get(JwtConstant.USER_ID, Long.class));
            user.setUsername(claims.get(JwtConstant.USERNAME, String.class));
            user.setRole(claims.get(JwtConstant.ROLE, String.class));
            UserContext.set(user);
            return true;
        } catch (Exception e) {
            // 未登录或 token 无效/过期
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(objectMapper.writeValueAsString(Result.error(ResultCode.UNAUTHORIZED)));
            return false;
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        // 清理 ThreadLocal，防止内存泄漏
        UserContext.remove();
    }
}
