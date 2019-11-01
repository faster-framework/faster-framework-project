package cn.org.faster.framework.admin.dict.service;

import cn.org.faster.framework.admin.dict.entity.SysDict;
import cn.org.faster.framework.admin.dict.mapper.DictMapper;
import cn.org.faster.framework.mybatis.entity.BaseEntity;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @author faster-builder
 * 字典Service
 */
@Service
@Transactional
@AllArgsConstructor
public class DictService extends ServiceImpl<DictMapper, SysDict> {

    /**
     * 分页查询
     *
     * @param sysDict 请求参数
     * @return 字典分页列表
     */
    public IPage<SysDict> list(SysDict sysDict) {
        LambdaQueryWrapper<SysDict> queryWrapper = new LambdaQueryWrapper<>();
        if (!StringUtils.isEmpty(sysDict.getName())) {
            queryWrapper.likeRight(SysDict::getName, sysDict.getName());
        }
        if (!StringUtils.isEmpty(sysDict.getType())) {
            queryWrapper.likeRight(SysDict::getType, sysDict.getType());
        }
        if (!StringUtils.isEmpty(sysDict.getRemark())) {
            queryWrapper.likeRight(SysDict::getRemark, sysDict.getRemark());
        }
        if (!StringUtils.isEmpty(sysDict.getDictValue())) {
            queryWrapper.eq(SysDict::getDictValue, sysDict.getDictValue());
        }
        if (sysDict.getShowStatus() != null) {
            queryWrapper.eq(SysDict::getShowStatus, sysDict.getShowStatus());
        }
        queryWrapper.orderByAsc(BaseEntity::getSort);
        return super.baseMapper.selectPage(sysDict.toPage(), queryWrapper);
    }

    /**
     * 分页查询
     *
     * @param sysDict 请求参数
     * @return 字典分页列表
     */
    public List<SysDict> listAll(SysDict sysDict) {
        LambdaQueryWrapper<SysDict> queryWrapper = new LambdaQueryWrapper<>();
        if (!StringUtils.isEmpty(sysDict.getName())) {
            queryWrapper.eq(SysDict::getName, sysDict.getName());
        }
        if (!StringUtils.isEmpty(sysDict.getType())) {
            queryWrapper.eq(SysDict::getType, sysDict.getType());
        }
        if (!StringUtils.isEmpty(sysDict.getDictValue())) {
            queryWrapper.eq(SysDict::getDictValue, sysDict.getDictValue());
        }
        if (sysDict.getShowStatus() != null) {
            queryWrapper.eq(SysDict::getShowStatus, sysDict.getShowStatus());
        }
        queryWrapper.orderByAsc(BaseEntity::getSort);
        return super.baseMapper.selectList(queryWrapper);
    }

    /**
     * 根据主键id查询详情
     *
     * @param id 字典id
     * @return 字典详情
     */
    public SysDict queryById(Long id) {
        return super.baseMapper.selectById(id);
    }

    /**
     * 根据条件查询详情
     *
     * @param sysDict 请求参数
     * @return 字典详情
     */
    public SysDict query(SysDict sysDict) {
        LambdaQueryWrapper<SysDict> queryWrapper = new LambdaQueryWrapper<>();
        if (!StringUtils.isEmpty(sysDict.getName())) {
            queryWrapper.eq(SysDict::getName, sysDict.getName());
        }
        if (!StringUtils.isEmpty(sysDict.getType())) {
            queryWrapper.eq(SysDict::getType, sysDict.getType());
        }
        if (!StringUtils.isEmpty(sysDict.getDictValue())) {
            queryWrapper.eq(SysDict::getDictValue, sysDict.getDictValue());
        }
        if (sysDict.getShowStatus() != null) {
            queryWrapper.eq(SysDict::getShowStatus, sysDict.getShowStatus());
        }
        return this.getOne(queryWrapper);
    }

    /**
     * 添加字典表
     *
     * @param sysDict 实体
     * @return ResponseEntity
     */
    public ResponseEntity add(SysDict sysDict) {
        sysDict.preInsert();
        super.baseMapper.insert(sysDict);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    /**
     * 修改字典表
     *
     * @param sysDict 实体
     * @return ResponseEntity
     */
    public ResponseEntity update(SysDict sysDict) {
        sysDict.preUpdate();
        super.baseMapper.updateById(sysDict);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    /**
     * 删除字典表
     *
     * @param id 主键id
     * @return ResponseEntity
     */
    public ResponseEntity delete(Long id) {
        super.baseMapper.deleteById(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}