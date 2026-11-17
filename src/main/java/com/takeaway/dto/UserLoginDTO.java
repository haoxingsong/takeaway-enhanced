package com.takeaway.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * C 端用户登录入参（模拟微信登录，直接传 openid）
 */
@Data
public class UserLoginDTO {

    @NotBlank(message = "openid 不能为空")
    private String openid;
}
