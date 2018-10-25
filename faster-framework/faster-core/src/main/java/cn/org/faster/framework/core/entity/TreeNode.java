package cn.org.faster.framework.core.entity;

import java.util.List;

/**
 * @author zhangbowen
 */
public interface TreeNode {
    Long getId();

    Long getParentId();

    List<TreeNode> getChildren();

    void setChildren(List<TreeNode> children);
}
