package cn.org.faster.framework.admin.rolePermission.service;

import cn.org.faster.framework.admin.rolePermission.entity.SysRolePermission;
import cn.org.faster.framework.admin.rolePermission.mapper.SysRolePermissionMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import org.apache.shiro.realm.AuthorizingRealm;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * @author zhangbowen
 */
@Service
@Transactional
@AllArgsConstructor
public class SysRolePermissionService extends ServiceImpl<SysRolePermissionMapper, SysRolePermission> {
    private AuthorizingRealm authorizingRealm;

    /**
     * 根据角色id列表查询角色权限关系列表
     *
     * @param roleIdList 角色id列表
     * @return 权限列表
     */
    public List<SysRolePermission> selectByRoleIdList(List<Long> roleIdList) {
        return super.baseMapper.selectList(new QueryWrapper<SysRolePermission>()
                .select("distinct(permission_id) as permissionId")
                .in("role_id", roleIdList)
        );
    }

    /**
     * 根据角色删除角色权限关系
     *
     * @param roleId 角色id
     */
    public void deleteByRoleId(Long roleId) {
        super.baseMapper.delete(new LambdaQueryWrapper<SysRolePermission>().eq(SysRolePermission::getRoleId, roleId));
    }

    /**
     * 根据权限删除角色权限关系
     *
     * @param permissionIdList 角色id列表
     */
    public void deleteByPermissionIdList(List<Long> permissionIdList) {
        super.baseMapper.delete(new LambdaQueryWrapper<SysRolePermission>().in(SysRolePermission::getPermissionId, permissionIdList));
    }

    /**
     * 批量为角色选择权限
     *
     * @param list   权限列表
     * @param roleId 角色id
     */
    public void batchChoose(List<SysRolePermission> list, @PathVariable Long roleId) {
        //根据角色删除所有的关系
        this.deleteByRoleId(roleId);
        //清空缓存
        authorizingRealm.getAuthorizationCache().clear();
        if (list == null || list.isEmpty()) {
            return;
        }
        list.forEach(item -> {
            item.setRoleId(roleId);
            item.preInsert();
        });
        //插入本次选择的角色权限关系
        super.saveBatch(list);
    }
}
