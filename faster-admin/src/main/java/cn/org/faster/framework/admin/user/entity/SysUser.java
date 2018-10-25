package cn.org.faster.framework.admin.user.entity;

import cn.org.faster.framework.mybatis.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author zhangbowen
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SysUser extends BaseEntity {
    private String account;
    private String password;
    private String name;
}
