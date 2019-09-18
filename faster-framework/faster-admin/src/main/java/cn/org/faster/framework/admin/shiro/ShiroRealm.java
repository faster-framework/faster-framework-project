package cn.org.faster.framework.admin.shiro;

import cn.org.faster.framework.admin.permission.entity.SysPermission;
import cn.org.faster.framework.admin.permission.service.SysPermissionService;
import cn.org.faster.framework.admin.rolePermission.entity.SysRolePermission;
import cn.org.faster.framework.admin.rolePermission.service.SysRolePermissionService;
import cn.org.faster.framework.admin.user.entity.SysUser;
import cn.org.faster.framework.admin.user.service.SysUserService;
import cn.org.faster.framework.admin.userRole.entity.SysUserRole;
import cn.org.faster.framework.admin.userRole.service.SysUserRoleService;
import cn.org.faster.framework.core.cache.context.CacheFacade;
import cn.org.faster.framework.web.auth.app.service.AuthService;
import cn.org.faster.framework.web.context.model.RequestContext;
import cn.org.faster.framework.web.context.model.WebContextFacade;
import cn.org.faster.framework.web.exception.model.BasicErrorCode;
import io.jsonwebtoken.Claims;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zhangbowen
 */
@Service
public class ShiroRealm extends AuthorizingRealm {
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private AuthService authService;
    @Autowired
    private SysUserRoleService sysUserRoleService;
    @Autowired
    private SysPermissionService sysPermissionService;
    @Autowired
    private SysRolePermissionService sysRolePermissionService;

    @Override
    public boolean supports(AuthenticationToken token) {
        return token != null;
    }

    /**
     * 授权
     *
     * @param principalCollection 授权列表
     * @return 授权信息
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        String jwtToken = (String) super.getAvailablePrincipal(principalCollection);
        Claims claims = authService.parseToken(jwtToken);
        if (claims == null) {
            throw new AuthenticationException(BasicErrorCode.TOKEN_INVALID.getDescription());
        }
        SysUser user = sysUserService.info(Long.parseLong(claims.getAudience()));
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        if (user == null) {
            return info;
        }

        List<String> permissionCodeList;
        SysUserRole sysUserRoleQuery = new SysUserRole();
        sysUserRoleQuery.setUserId(user.getId());
        //根据用户id，获取当前用户的角色
        List<SysUserRole> sysUserRoleList = sysUserRoleService.list(sysUserRoleQuery);
        boolean isAdmin = sysUserRoleList.stream().anyMatch(userRole -> userRole.getRoleId() == 0);
        info.setRoles(sysUserRoleList.stream().map(userRole -> userRole.getRoleId().toString()).collect(Collectors.toSet()));
        //如果是管理员用户，查询全部权限
        if (isAdmin) {
            permissionCodeList = sysPermissionService.selectAll().stream().map(SysPermission::getCode).collect(Collectors.toList());
        } else {
            //如果不是管理员用户，查询该用户所有角色id
            List<Long> roleIds = sysUserRoleList.stream().map(SysUserRole::getRoleId).collect(Collectors.toList());
            //根据角色id列表查询所有权限id
            List<Long> permissionIds = sysRolePermissionService.selectByRoleIdList(roleIds).stream().map(SysRolePermission::getPermissionId).collect(Collectors.toList());
            //根据权限id列表查询所有权限code
            permissionCodeList = sysPermissionService.selectByIdList(permissionIds).stream().map(SysPermission::getCode).collect(Collectors.toList());
        }
        info.addStringPermissions(permissionCodeList);
        return info;
    }

    /**
     * 认证
     *
     * @param authenticationToken 认证token
     * @return 认证信息
     * @throws AuthenticationException 异常
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        try {
            String token = (String) authenticationToken.getCredentials();
            Claims claims = authService.parseToken(token);
            if (claims == null) {
                throw new AuthenticationException(BasicErrorCode.TOKEN_INVALID.getDescription());
            }
            String userId = claims.getAudience();
            if (!authService.isMultipartTerminal()) {
                String cacheToken = CacheFacade.get(AuthService.AUTH_TOKEN_PREFIX + userId);
                if (StringUtils.isEmpty(cacheToken) || !token.equals(cacheToken)) {
                    throw new AuthenticationException(BasicErrorCode.TOKEN_INVALID.getDescription());
                }
            }
            RequestContext requestContext = WebContextFacade.getRequestContext();
            requestContext.setUserId(Long.parseLong(userId));
            WebContextFacade.setRequestContext(requestContext);
        } catch (Exception e) {
            throw new AuthenticationException(BasicErrorCode.TOKEN_INVALID.getDescription());
        }
        return new SimpleAuthenticationInfo(authenticationToken.getPrincipal(), authenticationToken.getCredentials(), getName());
    }
}
