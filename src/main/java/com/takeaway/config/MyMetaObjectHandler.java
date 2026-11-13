package com.takeaway.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.takeaway.common.context.CurrentUser;
import com.takeaway.common.context.UserContext;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 公共字段自动填充：create_time / update_time / create_user / update_user
 * 实体字段需配合 @TableField(fill = ...) 使用（阶段3引入实体后生效）
 */
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        LocalDateTime now = LocalDateTime.now();
        this.strictInsertFill(metaObject, "createTime", LocalDateTime.class, now);
        this.strictInsertFill(metaObject, "updateTime", LocalDateTime.class, now);
        CurrentUser user = UserContext.get();
        if (user != null) {
            this.strictInsertFill(metaObject, "createUser", Long.class, user.getId());
            this.strictInsertFill(metaObject, "updateUser", Long.class, user.getId());
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.strictUpdateFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
        CurrentUser user = UserContext.get();
        if (user != null) {
            this.strictUpdateFill(metaObject, "updateUser", Long.class, user.getId());
        }
    }
}
