-- ============================================================
-- 苍穹外卖增强版（Takeaway Enhanced）建表脚本
-- 数据库：takeaway
-- 字符集：utf8mb4    引擎：InnoDB
--
-- 统一字段规范：
--   主键 id            BIGINT（雪花ID，MyBatis-Plus ASSIGN_ID）
--   创建时间 create_time DATETIME DEFAULT CURRENT_TIMESTAMP
--   更新时间 update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
--   创建人 create_user / 更新人 update_user  BIGINT
--
-- 表清单（12 张）：
--   管理端：employee / category / dish / dish_flavor
--   C 端：  user / address_book / shopping_cart / orders / order_detail
--   增强：  operation_log / refund_record / favorite
-- ============================================================

CREATE DATABASE IF NOT EXISTS `takeaway` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE `takeaway`;

-- ------------------------------------------------------------
-- 1. 员工表 employee（管理员/员工，含登录与数据权限角色）
-- ------------------------------------------------------------
DROP TABLE IF EXISTS `employee`;
CREATE TABLE `employee` (
  `id`          BIGINT       NOT NULL COMMENT '主键',
  `name`        VARCHAR(32)  NOT NULL COMMENT '姓名',
  `username`    VARCHAR(32)  NOT NULL COMMENT '登录账号',
  `password`    VARCHAR(64)  NOT NULL COMMENT '密码(BCrypt加密)',
  `phone`       VARCHAR(11)  DEFAULT NULL COMMENT '手机号',
  `sex`         CHAR(1)      DEFAULT NULL COMMENT '性别(1男 0女)',
  `id_number`   VARCHAR(18)  DEFAULT NULL COMMENT '身份证号',
  `role`        VARCHAR(20)  NOT NULL DEFAULT 'EMPLOYEE' COMMENT '角色:ADMIN管理员/EMPLOYEE普通员工',
  `status`      TINYINT      NOT NULL DEFAULT 1 COMMENT '状态:1启用 0禁用',
  `create_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_user` BIGINT       DEFAULT NULL COMMENT '创建人',
  `update_user` BIGINT       DEFAULT NULL COMMENT '更新人',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='员工表';

-- ------------------------------------------------------------
-- 2. 菜品分类表 category
-- ------------------------------------------------------------
DROP TABLE IF EXISTS `category`;
CREATE TABLE `category` (
  `id`          BIGINT      NOT NULL COMMENT '主键',
  `type`        TINYINT     NOT NULL DEFAULT 1 COMMENT '类型:1菜品分类',
  `name`        VARCHAR(32) NOT NULL COMMENT '分类名称',
  `sort`        INT         NOT NULL DEFAULT 0 COMMENT '排序号(越小越靠前)',
  `status`      TINYINT     NOT NULL DEFAULT 1 COMMENT '状态:1启用 0禁用',
  `create_time` DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_user` BIGINT      DEFAULT NULL COMMENT '创建人',
  `update_user` BIGINT      DEFAULT NULL COMMENT '更新人',
  PRIMARY KEY (`id`),
  KEY `idx_type_status` (`type`, `status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='菜品分类表';

-- ------------------------------------------------------------
-- 3. 菜品表 dish（含库存字段，用于下单超卖控制）
-- ------------------------------------------------------------
DROP TABLE IF EXISTS `dish`;
CREATE TABLE `dish` (
  `id`          BIGINT        NOT NULL COMMENT '主键',
  `name`        VARCHAR(32)   NOT NULL COMMENT '菜品名称',
  `category_id` BIGINT        NOT NULL COMMENT '分类ID',
  `price`       DECIMAL(10,2) NOT NULL COMMENT '价格',
  `image`       VARCHAR(255)  DEFAULT NULL COMMENT '图片地址',
  `description` VARCHAR(255)  DEFAULT NULL COMMENT '描述',
  `status`      TINYINT       NOT NULL DEFAULT 1 COMMENT '状态:1起售 0停售',
  `stock`       INT           NOT NULL DEFAULT 0 COMMENT '库存(扣减预留在Redis,此字段为兜底)',
  `create_time` DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_user` BIGINT        DEFAULT NULL COMMENT '创建人',
  `update_user` BIGINT        DEFAULT NULL COMMENT '更新人',
  PRIMARY KEY (`id`),
  KEY `idx_category_id` (`category_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='菜品表';

-- ------------------------------------------------------------
-- 4. 菜品口味表 dish_flavor
-- ------------------------------------------------------------
DROP TABLE IF EXISTS `dish_flavor`;
CREATE TABLE `dish_flavor` (
  `id`      BIGINT       NOT NULL COMMENT '主键',
  `dish_id` BIGINT       NOT NULL COMMENT '菜品ID',
  `name`    VARCHAR(32)  NOT NULL COMMENT '口味名称(如:辣度)',
  `value`   VARCHAR(255) NOT NULL COMMENT '口味值(JSON数组,如:["不辣","微辣","中辣"])',
  PRIMARY KEY (`id`),
  KEY `idx_dish_id` (`dish_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='菜品口味表';

-- ------------------------------------------------------------
-- 5. C端用户表 user
-- ------------------------------------------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id`          BIGINT       NOT NULL COMMENT '主键',
  `openid`      VARCHAR(45)  DEFAULT NULL COMMENT '微信openid(模拟)',
  `name`        VARCHAR(32)  DEFAULT NULL COMMENT '昵称',
  `phone`       VARCHAR(11)  DEFAULT NULL COMMENT '手机号',
  `sex`         CHAR(1)      DEFAULT NULL COMMENT '性别(1男 0女)',
  `avatar`      VARCHAR(255) DEFAULT NULL COMMENT '头像地址',
  `create_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_openid` (`openid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='C端用户表';

-- ------------------------------------------------------------
-- 6. 收货地址表 address_book
-- ------------------------------------------------------------
DROP TABLE IF EXISTS `address_book`;
CREATE TABLE `address_book` (
  `id`          BIGINT       NOT NULL COMMENT '主键',
  `user_id`     BIGINT       NOT NULL COMMENT '用户ID',
  `consignee`   VARCHAR(50)  NOT NULL COMMENT '收货人',
  `sex`         CHAR(1)      DEFAULT NULL COMMENT '性别(1男 0女)',
  `phone`       VARCHAR(11)  NOT NULL COMMENT '手机号',
  `province`    VARCHAR(32)  DEFAULT NULL COMMENT '省',
  `city`        VARCHAR(32)  DEFAULT NULL COMMENT '市',
  `district`    VARCHAR(32)  DEFAULT NULL COMMENT '区',
  `detail`      VARCHAR(255) NOT NULL COMMENT '详细地址',
  `label`       VARCHAR(10)  DEFAULT NULL COMMENT '标签(家/学校/公司)',
  `is_default`  TINYINT      NOT NULL DEFAULT 0 COMMENT '是否默认:1是 0否',
  `create_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='收货地址表';

-- ------------------------------------------------------------
-- 7. 购物车表 shopping_cart
-- ------------------------------------------------------------
DROP TABLE IF EXISTS `shopping_cart`;
CREATE TABLE `shopping_cart` (
  `id`          BIGINT        NOT NULL COMMENT '主键',
  `user_id`     BIGINT        NOT NULL COMMENT '用户ID',
  `dish_id`     BIGINT        NOT NULL COMMENT '菜品ID',
  `dish_flavor` VARCHAR(200)  DEFAULT NULL COMMENT '口味(JSON)',
  `number`      INT           NOT NULL DEFAULT 1 COMMENT '数量',
  `amount`      DECIMAL(10,2) NOT NULL COMMENT '金额(单价*数量)',
  `create_time` DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='购物车表';

-- ------------------------------------------------------------
-- 8. 订单表 orders（核心，含状态流转）
-- ------------------------------------------------------------
DROP TABLE IF EXISTS `orders`;
CREATE TABLE `orders` (
  `id`              BIGINT        NOT NULL COMMENT '主键',
  `number`          VARCHAR(50)   NOT NULL COMMENT '订单号',
  `status`          TINYINT       NOT NULL DEFAULT 1 COMMENT '订单状态:1待付款 2待接单 3已接单 4派送中 5已完成 6已取消 7退款中 8已退款',
  `user_id`         BIGINT        NOT NULL COMMENT '下单用户ID',
  `address_book_id` BIGINT        NOT NULL COMMENT '收货地址ID',
  `order_time`      DATETIME      NOT NULL COMMENT '下单时间',
  `checkout_time`   DATETIME      DEFAULT NULL COMMENT '支付/结账时间',
  `pay_method`      TINYINT       DEFAULT NULL COMMENT '支付方式:1微信 2支付宝(模拟)',
  `pay_status`      TINYINT       NOT NULL DEFAULT 0 COMMENT '支付状态:0未支付 1已支付 2已退款',
  `amount`          DECIMAL(10,2) NOT NULL COMMENT '订单实收金额',
  `remark`          VARCHAR(255)  DEFAULT NULL COMMENT '备注',
  `phone`           VARCHAR(11)   DEFAULT NULL COMMENT '收货手机号(快照)',
  `address`         VARCHAR(255)  DEFAULT NULL COMMENT '收货地址(快照)',
  `consignee`       VARCHAR(50)   DEFAULT NULL COMMENT '收货人(快照)',
  `cancel_reason`   VARCHAR(255)  DEFAULT NULL COMMENT '取消原因',
  `create_time`     DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time`     DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_number` (`number`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_status` (`status`),
  KEY `idx_order_time` (`order_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单表';

-- ------------------------------------------------------------
-- 9. 订单明细表 order_detail
-- ------------------------------------------------------------
DROP TABLE IF EXISTS `order_detail`;
CREATE TABLE `order_detail` (
  `id`          BIGINT        NOT NULL COMMENT '主键',
  `order_id`    BIGINT        NOT NULL COMMENT '订单ID',
  `dish_id`     BIGINT        NOT NULL COMMENT '菜品ID',
  `name`        VARCHAR(32)   NOT NULL COMMENT '菜品名称(快照)',
  `image`       VARCHAR(255)  DEFAULT NULL COMMENT '菜品图片(快照)',
  `dish_flavor` VARCHAR(200)  DEFAULT NULL COMMENT '口味(快照)',
  `number`      INT           NOT NULL COMMENT '数量',
  `amount`      DECIMAL(10,2) NOT NULL COMMENT '金额(单价*数量)',
  PRIMARY KEY (`id`),
  KEY `idx_order_id` (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单明细表';

-- ------------------------------------------------------------
-- 10. 操作日志表 operation_log（AOP 增强）
-- ------------------------------------------------------------
DROP TABLE IF EXISTS `operation_log`;
CREATE TABLE `operation_log` (
  `id`          BIGINT       NOT NULL COMMENT '主键',
  `module`      VARCHAR(50)  DEFAULT NULL COMMENT '操作模块',
  `operation`   VARCHAR(50)  DEFAULT NULL COMMENT '操作类型(INSERT/UPDATE/DELETE/SELECT)',
  `method`      VARCHAR(255) DEFAULT NULL COMMENT '调用方法(全限定名)',
  `params`      TEXT         COMMENT '请求参数(JSON)',
  `user_id`     BIGINT       DEFAULT NULL COMMENT '操作人ID',
  `user_name`   VARCHAR(32)  DEFAULT NULL COMMENT '操作人姓名',
  `ip`          VARCHAR(45)  DEFAULT NULL COMMENT '操作IP',
  `cost_time`   BIGINT       DEFAULT NULL COMMENT '耗时(毫秒)',
  `create_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
  PRIMARY KEY (`id`),
  KEY `idx_create_time` (`create_time`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='操作日志表';

-- ------------------------------------------------------------
-- 11. 退款记录表 refund_record（退款增强）
-- ------------------------------------------------------------
DROP TABLE IF EXISTS `refund_record`;
CREATE TABLE `refund_record` (
  `id`           BIGINT        NOT NULL COMMENT '主键',
  `order_id`     BIGINT        NOT NULL COMMENT '订单ID',
  `order_number` VARCHAR(50)   NOT NULL COMMENT '订单号',
  `amount`       DECIMAL(10,2) NOT NULL COMMENT '退款金额',
  `reason`       VARCHAR(255)  DEFAULT NULL COMMENT '退款原因',
  `status`       TINYINT       NOT NULL DEFAULT 1 COMMENT '状态:1申请中 2已退款 3已拒绝',
  `create_time`  DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '申请时间',
  `update_time`  DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_order_id` (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='退款记录表';

-- ------------------------------------------------------------
-- 12. 收藏表 favorite（收藏增强）
-- ------------------------------------------------------------
DROP TABLE IF EXISTS `favorite`;
CREATE TABLE `favorite` (
  `id`          BIGINT   NOT NULL COMMENT '主键',
  `user_id`     BIGINT   NOT NULL COMMENT '用户ID',
  `dish_id`     BIGINT   NOT NULL COMMENT '菜品ID',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '收藏时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_dish` (`user_id`, `dish_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='收藏表';
