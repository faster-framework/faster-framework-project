package cn.org.faster.framework.admin.permission.service;

import cn.org.faster.framework.admin.permission.entity.SysPermission;
import cn.org.faster.framework.admin.permission.error.SysPermissionError;
import cn.org.faster.framework.admin.permission.mapper.SysPermissionMapper;
import cn.org.faster.framework.admin.rolePermission.service.SysRolePermissionService;
import cn.org.faster.framework.admin.shiro.ShiroRealm;
import cn.org.faster.framework.core.entity.TreeNode;
import cn.org.faster.framework.core.utils.tree.TreeUtils;
import cn.org.faster.framework.mybatis.entity.BaseEntity;
import cn.org.faster.framework.web.context.model.SpringAppContextFacade;
import cn.org.faster.framework.web.exception.model.ResponseErrorEntity;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zhangbowen
 */
@Service
@Transactional
public class SysPermissionService extends ServiceImpl<SysPermissionMapper, SysPermission> {
    @Autowired
    private SysRolePermissionService sysRolePermissionService;

    /**
     * @return 权限树
     */
    public List<TreeNode> treeList() {
        List<SysPermission> list = super.baseMapper.selectList(
                new LambdaQueryWrapper<SysPermission>().orderByAsc(SysPermission::getParentIds)
        ).stream().sorted(Comparator.comparing(BaseEntity::getSort)).collect(Collectors.toList());
        return TreeUtils.convertToTree(list);
    }

    /**
     * @return 权限列表
     */
    public List<SysPermission> selectAll() {
        return super.baseMapper.selectList(null);
    }

    /**
     * @param permissionId 权限id
     * @return 权限详情
     */
    public SysPermission info(Long permissionId) {
        return super.baseMapper.selectById(permissionId);
    }

    /**
     * 添加权限
     *
     * @param sysPermission 权限实体
     * @return ResponseEntity
     */
    public ResponseEntity add(SysPermission sysPermission) {
        boolean existParentId = completeParentIds(sysPermission);
        if (!existParentId) {
            return ResponseErrorEntity.error(SysPermissionError.PERMISSION_PARENT_NOT_EXIST, HttpStatus.BAD_REQUEST);
        }
        if (!StringUtils.isEmpty(sysPermission.getCode())) {
            if (existCode(sysPermission.getCode())) {
                return ResponseErrorEntity.error(SysPermissionError.PERMISSION_CODE_EXIST, HttpStatus.BAD_REQUEST);
            }
        }
        sysPermission.preInsert();
        super.baseMapper.insert(sysPermission);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    /**
     * @param sysPermission 填充parentIds
     * @return 如果parentId不存在，返回false
     */
    private boolean completeParentIds(SysPermission sysPermission) {
        if (sysPermission.getParentId() != 0) {
            //获取parentIds
            SysPermission existParent = super.baseMapper.selectById(sysPermission.getParentId());
            if (existParent == null) {
                return false;
            }
            sysPermission.setParentIds(existParent.getParentIds().concat(",").concat("[").concat(existParent.getId().toString()).concat("]"));
        } else {
            sysPermission.setParentIds("[0]");
        }
        return true;
    }


    /**
     * 判断是否已经存在code
     *
     * @param code 权限code
     * @return true or false
     */
    private boolean existCode(String code) {
        return this.getOne(new LambdaQueryWrapper<SysPermission>().eq(SysPermission::getCode, code)) != null;
    }

    /**
     * 编辑权限
     *
     * @param sysPermission 权限实体
     * @return ResponseEntity
     */
    public ResponseEntity update(SysPermission sysPermission) {
        //判断要更新的code是否已经存在
        if (!StringUtils.isEmpty(sysPermission.getCode())) {
            SysPermission oldPermission = super.baseMapper.selectById(sysPermission.getId());
            if (oldPermission != null) {
                //如果旧的code跟要更改的code一致，说明不需要修改。如果不一致，并且数据库已经存在要更改的code，返回错误
                if (!sysPermission.getCode().equals(oldPermission.getCode()) && existCode(sysPermission.getCode())) {
                    return ResponseErrorEntity.error(SysPermissionError.PERMISSION_CODE_EXIST, HttpStatus.BAD_REQUEST);
                }
            }
        }
        sysPermission.preUpdate();
        super.baseMapper.updateById(sysPermission);
        //清空缓存
        SpringAppContextFacade.applicationContext.getBean(ShiroRealm.class).getAuthorizationCache().clear();
        return new ResponseEntity(HttpStatus.CREATED);
    }

    /**
     * 删除
     *
     * @param id 权限id
     */
    public void delete(Long id) {
        //获取该用户子集
        List<Long> delList = super.baseMapper.selectList(new LambdaQueryWrapper<SysPermission>()
                .like(SysPermission::getParentIds, "[".concat(id.toString()).concat("]"))
        ).stream().map(SysPermission::getId).collect(Collectors.toList());
        delList.add(id);
        //删除所有
        super.baseMapper.deleteBatchIds(delList);
        //根据权限列表，删除角色权限关系
        sysRolePermissionService.deleteByPermissionIdList(delList);
        //清空缓存
        SpringAppContextFacade.applicationContext.getBean(ShiroRealm.class).getAuthorizationCache().clear();
    }

    /**
     * 根据id列表查询
     *
     * @param idList 权限id列表
     * @return 权限列表
     */
    public List<SysPermission> selectByIdList(List<Long> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            return Collections.emptyList();
        }
        return super.baseMapper.selectBatchIds(idList);
    }

    /**
     * @return 全部的权限列表
     */
    public List<SysPermission> list() {
        return super.baseMapper.selectList(new LambdaQueryWrapper<SysPermission>().orderByAsc(SysPermission::getParentIds)).stream().sorted(Comparator.comparing(BaseEntity::getSort)).collect(Collectors.toList());
    }
}
