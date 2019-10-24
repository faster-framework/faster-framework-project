package cn.org.faster.framework.admin.auth.model;

import lombok.Data;

import java.util.Collection;

/**
 * @author zhangbowen
 * @since 2019-09-30
 */
@Data
public class UserInfoRes {
    private String name;
    private Collection<String> permissions;
}
