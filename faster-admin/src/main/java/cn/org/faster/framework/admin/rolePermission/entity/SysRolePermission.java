package cn.org.faster.framework.admin.rolePermission.entity;

import cn.org.faster.framework.mybatis.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
 * @author zhangbowen
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SysRolePermission extends BaseEntity {
    private Long roleId;
    @NotNull(message = "请选择权限")
    private Long permissionId;
}
