package cn.org.faster.framework.admin.role.controller;

import cn.org.faster.framework.admin.role.entity.SysRole;
import cn.org.faster.framework.admin.role.model.SysRoleReq;
import cn.org.faster.framework.admin.role.service.SysRoleService;
import cn.org.faster.framework.admin.rolePermission.entity.SysRolePermission;
import cn.org.faster.framework.admin.rolePermission.error.RolePermissionError;
import cn.org.faster.framework.admin.rolePermission.service.SysRolePermissionService;
import cn.org.faster.framework.web.exception.model.ResponseErrorEntity;
import cn.org.faster.framework.web.model.ListWrapper;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author zhangbowen
 */
@RestController
@RequestMapping("/sys/roles")
public class SysRoleController {
    @Autowired
    private SysRoleService sysRoleService;
    @Autowired
    private SysRolePermissionService sysRolePermissionService;

    /**
     * 角色列表
     *
     * @param sysRole 角色实体
     * @return 角色列表
     */
    @GetMapping
    @RequiresPermissions("roles:list")
    public ResponseEntity list(SysRole sysRole) {
        return ResponseEntity.ok(sysRoleService.list(sysRole));
    }

    /**
     * 角色详情
     *
     * @param roleId 角色id
     * @return 角色详情
     */
    @GetMapping("/{roleId}")
    @RequiresPermissions("roles:info")
    public ResponseEntity info(@PathVariable Long roleId) {
        return ResponseEntity.ok(sysRoleService.info(roleId));
    }

    /**
     * 新增角色
     *
     * @param request 角色添加实体
     * @return ResponseEntity
     */
    @PostMapping
    @RequiresPermissions("roles:add")
    public ResponseEntity add(@Validated @RequestBody SysRoleReq request) {
        SysRole insert = new SysRole();
        BeanUtils.copyProperties(request, insert);
        sysRoleService.add(insert);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * 更新角色
     *
     * @param request 角色修改实体
     * @param roleId  角色id
     * @return ResponseEntity
     */
    @PutMapping("/{roleId}")
    @RequiresPermissions("roles:modify")
    public ResponseEntity update(@RequestBody SysRoleReq request, @PathVariable Long roleId) {
        SysRole update = new SysRole();
        BeanUtils.copyProperties(request, update);
        update.setId(roleId);
        sysRoleService.update(update);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * 删除角色
     *
     * @param roleIds 角色id列表
     * @return ResponseEntity
     */
    @DeleteMapping("/delete")
    @RequiresPermissions("roles:delete")
    public ResponseEntity delete(@RequestBody List<Long> roleIds) {
        roleIds.forEach(item -> {
            if (item == 0L) {
                return;
            }
            sysRoleService.delete(item);
        });
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    /**
     * 批量为角色选择权限
     *
     * @param list   权限id列表
     * @param roleId 角色id
     * @return ResponseEntity
     */
    @PutMapping("/{roleId}/permissions/choose")
    @RequiresPermissions("roles:permissions:choose")
    public ResponseEntity choosePermissions(@Validated @RequestBody ListWrapper<SysRolePermission> list, @PathVariable Long roleId) {
        if (roleId == 0L) {
            return ResponseErrorEntity.error(RolePermissionError.CANNOT_CHOOSE_ADMIN_PERMISSION, HttpStatus.BAD_REQUEST);
        }
        sysRolePermissionService.batchChoose(list.getList(), roleId);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    /**
     * 获取角色的权限id列表
     *
     * @param roleId 角色id
     * @return 权限ids列表
     */
    @GetMapping("/{roleId}/permissions")
    @RequiresPermissions("roles:permissions:list")
    public ResponseEntity permissions(@PathVariable Long roleId) {
        return ResponseEntity.ok(sysRoleService.permissions(roleId));
    }
}
