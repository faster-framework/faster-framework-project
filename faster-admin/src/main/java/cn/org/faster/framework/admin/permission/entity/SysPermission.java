package cn.org.faster.framework.admin.permission.entity;

import cn.org.faster.framework.core.entity.TreeNode;
import cn.org.faster.framework.mybatis.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author zhangbowen
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SysPermission extends BaseEntity implements TreeNode {
    private String name;
    private String code;
    private Long parentId;
    private String parentIds;
    @TableField(exist = false)
    private List<TreeNode> children;
}
