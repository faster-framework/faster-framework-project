package cn.org.faster.framework.admin.userRole.entity;

import cn.org.faster.framework.mybatis.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
 * @author zhangbowen
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SysUserRole extends BaseEntity {
    private Long userId;
    @NotNull(message = "请选择角色")
    private Long roleId;
}
