package com.takeaway.common.exception;

import com.takeaway.common.result.ResultCode;
import lombok.Getter;

/**
 * 业务异常基类
 */
@Getter
public class BaseException extends RuntimeException {

    private final Integer code;

    public BaseException(String message) {
        super(message);
        this.code = ResultCode.ERROR.getCode();
    }

    public BaseException(ResultCode resultCode) {
        super(resultCode.getMsg());
        this.code = resultCode.getCode();
    }

    public BaseException(Integer code, String message) {
        super(message);
        this.code = code;
    }
}
