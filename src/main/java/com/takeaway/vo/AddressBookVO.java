package com.takeaway.vo;

import lombok.Data;

/**
 * 收货地址出参
 */
@Data
public class AddressBookVO {

    /** 主键 */
    private Long id;

    /** 收货人 */
    private String consignee;

    /** 性别（1男 0女） */
    private String sex;

    /** 手机号 */
    private String phone;

    /** 省 */
    private String province;

    /** 市 */
    private String city;

    /** 区 */
    private String district;

    /** 详细地址 */
    private String detail;

    /** 标签（家/学校/公司） */
    private String label;

    /** 是否默认：1是 0否 */
    private Integer isDefault;
}
