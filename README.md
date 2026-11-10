# 苍穹外卖增强版（Takeaway Enhanced）

面向校招的 Java 后端简历项目。基于 Spring Boot 3 实现，在苍穹外卖基础功能之上，加入并发控制、分布式锁、消息队列等差异化技术点，具备完整工程规范，是一个可深挖、可面试的项目，而非教程复刻版。

## 项目定位

- **单体完整版（v1.0）**：核心业务模块 + 专项技术增强
- **微服务版本（v2.0，可选）**：服务拆分 + Nacos + Gateway + OpenFeign

## 技术栈

- JDK 17 / Spring Boot 3.x / MyBatis-Plus
- MySQL 8 / Redis / RabbitMQ
- JWT 鉴权 / Knife4j 接口文档 / Maven Wrapper

## 差异化亮点

1. Redisson 分布式锁 + Redis 预减库存，解决下单超卖
2. RabbitMQ 延迟队列，实现订单超时自动取消
3. Redis 菜品缓存 + 缓存击穿防护
4. 接口幂等 + 自定义限流注解（基于 Redis + Lua）
5. 操作日志 AOP / 数据权限注解 / 退款流程（可选增强）

## 项目结构

（阶段 2 补充，规划如下）

```
com.takeaway
├── common        # 通用返回体、异常、常量
├── config        # 配置类
├── annotation    # 自定义注解
├── interceptor   # JWT 拦截器
├── aspect        # AOP 切面
├── controller    # 控制器
├── service       # 服务接口
├── service.impl  # 服务实现
├── mapper        # MyBatis-Plus Mapper
├── entity        # 实体
├── dto           # 入参对象
├── vo            # 出参对象
├── util          # 工具类
└── task          # 定时任务
```

## 快速启动

（阶段 5 补充：Docker Compose 一键启动 MySQL + Redis + RabbitMQ + 应用）

## 文档

- [需求清单](docs/需求清单.md)
- [项目执行计划](docs/项目执行计划.md)

## 当前状态

开发中，当前进度：阶段 0（项目基线）。
