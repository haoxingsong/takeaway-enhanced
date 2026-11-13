package com.takeaway.common.result;

import lombok.Getter;

/**
 * 统一状态码
 */
@Getter
public enum ResultCode {

    SUCCESS(1, "成功"),
    ERROR(0, "失败"),
    PARAM_ERROR(400, "参数错误"),
    UNAUTHORIZED(401, "未登录或登录已过期"),
    FORBIDDEN(403, "无权限"),
    NOT_FOUND(404, "资源不存在"),
    SYSTEM_ERROR(500, "系统繁忙，请稍后重试");

    private final Integer code;
    private final String msg;

    ResultCode(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
