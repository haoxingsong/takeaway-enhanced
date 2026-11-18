package com.takeaway.common.constant;

/**
 * 订单状态与支付状态常量
 */
public class OrderConstant {

    /** 订单状态：1待付款 */
    public static final Integer PENDING_PAYMENT = 1;

    /** 订单状态：2待接单 */
    public static final Integer TO_BE_CONFIRMED = 2;

    /** 订单状态：3已接单 */
    public static final Integer CONFIRMED = 3;

    /** 订单状态：4派送中 */
    public static final Integer DELIVERY_IN_PROGRESS = 4;

    /** 订单状态：5已完成 */
    public static final Integer COMPLETED = 5;

    /** 订单状态：6已取消 */
    public static final Integer CANCELLED = 6;

    /** 支付状态：0未支付 */
    public static final Integer UN_PAID = 0;

    /** 支付状态：1已支付 */
    public static final Integer PAID = 1;

    /** 支付状态：2已退款 */
    public static final Integer REFUNDED = 2;

    private OrderConstant() {
    }
}
