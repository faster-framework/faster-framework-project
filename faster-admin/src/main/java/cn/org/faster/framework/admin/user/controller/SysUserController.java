package cn.org.faster.framework.admin.user.controller;

import cn.org.faster.framework.admin.user.entity.SysUser;
import cn.org.faster.framework.admin.user.error.UserError;
import cn.org.faster.framework.admin.user.model.SysUserAddReq;
import cn.org.faster.framework.admin.user.model.SysUserChangePwdReq;
import cn.org.faster.framework.admin.user.model.SysUserUpdateReq;
import cn.org.faster.framework.admin.user.service.SysUserService;
import cn.org.faster.framework.admin.userRole.entity.SysUserRole;
import cn.org.faster.framework.admin.userRole.service.SysUserRoleService;
import cn.org.faster.framework.web.exception.model.ErrorResponseEntity;
import cn.org.faster.framework.web.web.request.ListWrapper;
import lombok.AllArgsConstructor;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author zhangbowen
 */
@RestController
@RequestMapping("/sys/users")
@AllArgsConstructor
public class SysUserController {
    private SysUserService sysUserService;
    private SysUserRoleService sysUserRoleService;

    /**
     * @param sysUser 用户实体
     * @return 用户分页列表
     */
    @GetMapping
    @RequiresPermissions("users:list")
    public ResponseEntity list(SysUser sysUser) {
        return ResponseEntity.ok(sysUserService.list(sysUser));
    }

    /**
     * @param userId 用户id
     * @return 用户详情
     */
    @GetMapping("/{userId}")
    @RequiresPermissions("users:info")
    public ResponseEntity info(@PathVariable Long userId) {
        return ResponseEntity.ok(sysUserService.info(userId));
    }

    /**
     * 添加用户
     *
     * @param sysUserAddReq 添加用户实体
     * @return ResponseEntity
     */
    @PostMapping
    @RequiresPermissions("users:add")
    public ResponseEntity add(@RequestBody @Validated SysUserAddReq sysUserAddReq) {
        SysUser sysUser = new SysUser();
        BeanUtils.copyProperties(sysUserAddReq, sysUser);
        return sysUserService.add(sysUser);
    }

    /**
     * 编辑用户
     *
     * @param sysUserUpdateReq 编辑用户
     * @param userId           用户id
     * @return ResponseEntity
     */
    @PutMapping("/{userId}")
    @RequiresPermissions("users:modify")
    public ResponseEntity update(@RequestBody SysUserUpdateReq sysUserUpdateReq, @PathVariable Long userId) {
        SysUser sysUser = new SysUser();
        BeanUtils.copyProperties(sysUserUpdateReq, sysUser);
        sysUser.setId(userId);
        sysUserService.update(sysUser);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    /**
     * 删除用户
     *
     * @param userId 用户id
     * @return ResponseEntity
     */
    @DeleteMapping("/{userId}")
    @RequiresPermissions("users:delete")
    public ResponseEntity delete(@PathVariable Long userId) {
        if (userId == 0L) {
            return ErrorResponseEntity.error(UserError.ADMIN_CANNOT_DELETE, HttpStatus.BAD_REQUEST);
        }
        sysUserService.delete(userId);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    /**
     * 修改密码
     *
     * @param sysUserChangePwdReq 修改密码实体
     * @param userId              用户id
     * @return ResponseEntity
     */
    @PutMapping("/{userId}/password/change")
    @RequiresPermissions("users:password:change")
    public ResponseEntity changePwd(@Validated @RequestBody SysUserChangePwdReq sysUserChangePwdReq, @PathVariable Long userId) {
        return sysUserService.changePwd(sysUserChangePwdReq, userId);
    }

    /**
     * 重置密码
     *
     * @param userId 用户id
     * @return ResponseEntity
     */
    @PutMapping("/{userId}/password/reset")
    @RequiresPermissions("users:password:reset")
    public ResponseEntity resetPwd(@PathVariable Long userId) {
        sysUserService.resetPwd(userId);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    /**
     * 批量选择角色
     *
     * @param list   角色列表 id
     * @param userId 用户id
     * @return ResponseEntity
     */
    @PutMapping("/{userId}/roles/choose")
    @RequiresPermissions("users:roles:choose")
    public ResponseEntity chooseRoles(@Validated @RequestBody ListWrapper<SysUserRole> list, @PathVariable Long userId) {
        sysUserRoleService.batchChoose(list.getList(), userId);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    /**
     * 获取用户的角色id列表
     * @param userId 用户id
     * @return 角色ids列表
     */
    @GetMapping("/{userId}/roles")
    @RequiresPermissions("users:roles:list")
    public ResponseEntity roles(@PathVariable Long userId) {
        return ResponseEntity.ok(sysUserRoleService.roles(userId));
    }

}
