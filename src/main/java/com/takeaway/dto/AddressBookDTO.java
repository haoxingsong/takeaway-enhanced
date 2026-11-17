package com.takeaway.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

/**
 * 收货地址新增/编辑入参
 */
@Data
public class AddressBookDTO {

    /** 主键（编辑时必填） */
    private Long id;

    /** 收货人 */
    @NotBlank(message = "收货人不能为空")
    private String consignee;

    /** 性别（1男 0女） */
    private String sex;

    /** 手机号 */
    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;

    /** 省 */
    private String province;

    /** 市 */
    private String city;

    /** 区 */
    private String district;

    /** 详细地址 */
    @NotBlank(message = "详细地址不能为空")
    private String detail;

    /** 标签（家/学校/公司） */
    private String label;

    /** 是否默认：1是 0否 */
    private Integer isDefault;
}
