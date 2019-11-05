package cn.org.faster.framework.admin.user.service;

import cn.org.faster.framework.admin.shiro.ShiroRealm;
import cn.org.faster.framework.admin.user.entity.SysUser;
import cn.org.faster.framework.admin.user.error.UserError;
import cn.org.faster.framework.admin.user.mapper.SysUserMapper;
import cn.org.faster.framework.admin.user.model.SysUserChangePwdReq;
import cn.org.faster.framework.admin.userRole.service.SysUserRoleService;
import cn.org.faster.framework.core.utils.Utils;
import cn.org.faster.framework.mybatis.entity.BaseEntity;
import cn.org.faster.framework.web.context.model.SpringAppContextFacade;
import cn.org.faster.framework.web.exception.model.ResponseErrorEntity;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * @author zhangbowen
 */
@Service
@Transactional
public class SysUserService extends ServiceImpl<SysUserMapper, SysUser> {
    @Autowired
    private SysUserRoleService sysUserRoleService;

    /**
     * @param sysUser 用户实体
     * @return 分页列表
     */
    public IPage<SysUser> list(SysUser sysUser) {
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        if (!StringUtils.isEmpty(sysUser.getName())) {
            queryWrapper.likeRight(SysUser::getName, sysUser.getName());
        }
        return super.baseMapper.selectPage(sysUser.toPage(), queryWrapper.orderByDesc(BaseEntity::getCreateDate));
    }

    /**
     * @param id 用户id
     * @return 用户详情
     */
    public SysUser info(Long id) {
        return super.baseMapper.selectById(id);
    }

    /**
     * @param sysUser 新增用户
     * @return ResponseEntity
     */
    public ResponseEntity add(SysUser sysUser) {
        //判断当前用户是否存在
        SysUser exist = super.baseMapper.selectOne(new LambdaQueryWrapper<SysUser>().eq(SysUser::getAccount, sysUser.getAccount()));
        if (exist != null) {
            return ResponseErrorEntity.error(UserError.USER_EXIST, HttpStatus.BAD_REQUEST);
        }
        sysUser.setPassword(Utils.md5(sysUser.getPassword()));
        sysUser.preInsert();
        super.baseMapper.insert(sysUser);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    /**
     * 更新用户
     *
     * @param sysUser 用户实体
     */
    public void update(SysUser sysUser) {
        sysUser.preUpdate();
        super.baseMapper.updateById(sysUser);
    }

    /**
     * 删除用户
     *
     * @param userId 用户id
     */
    public void delete(Long userId) {
        super.baseMapper.deleteById(userId);
        //删除用户角色关系
        sysUserRoleService.deleteByUserId(userId);
        //清空缓存
        SpringAppContextFacade.applicationContext.getBean(ShiroRealm.class).getAuthorizationCache().clear();
    }

    /**
     * 修改密码
     *
     * @param sysUserChangePwdReq 修改密码实体
     * @param userId              用户id
     * @return ResponseEntity
     */
    public ResponseEntity changePwd(SysUserChangePwdReq sysUserChangePwdReq, Long userId) {
        SysUser existUser = super.baseMapper.selectById(userId);
        if (existUser == null) {
            return ResponseErrorEntity.error(UserError.USER_NOT_EXIST, HttpStatus.NOT_FOUND);
        }
        if (!existUser.getPassword().equals(Utils.md5(sysUserChangePwdReq.getOldPwd()))) {
            return ResponseErrorEntity.error(UserError.OLD_PASSWORD_ERROR, HttpStatus.BAD_REQUEST);
        }
        SysUser update = new SysUser();
        update.setPassword(Utils.md5(sysUserChangePwdReq.getPassword()));
        update.setId(userId);
        update.preUpdate();
        super.baseMapper.updateById(update);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    /**
     * 重置密码
     *
     * @param userId 用户id
     */
    public void resetPwd(Long userId) {
        SysUser update = new SysUser();
        update.setPassword(Utils.md5("123456"));
        update.setId(userId);
        this.update(update);
    }
}
